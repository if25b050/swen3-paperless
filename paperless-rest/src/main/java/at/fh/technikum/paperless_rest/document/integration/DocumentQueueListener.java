package at.fh.technikum.paperless_rest.document.integration;

import at.fh.technikum.paperless_common.business.integration.mqueue.GenAiFinishedMessage;
import at.fh.technikum.paperless_common.business.integration.mqueue.RabbitMQConfig;
import at.fh.technikum.paperless_common.business.integration.search.SearchIntegration;
import at.fh.technikum.paperless_rest.document.business.model.DocumentStatus;
import at.fh.technikum.paperless_rest.document.business.model.DocumentUpdateAiSummaryModel;
import at.fh.technikum.paperless_rest.document.business.model.DocumentUpdateStatusModel;
import at.fh.technikum.paperless_rest.document.dal.DocumentRepoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

/**
 * Component which listens for Queue-Events for Documents.
 */
@Component
@Slf4j
public class DocumentQueueListener {

    private final DocumentRepoService documentRepoService;
    private final SearchIntegration searchIntegration;

    public DocumentQueueListener(DocumentRepoService documentRepoService, SearchIntegration searchIntegration) {
        this.documentRepoService = documentRepoService;
        this.searchIntegration = searchIntegration;
    }

    /**
     * Reacts to the {@link RabbitMQConfig#OCR_FINISHED_QUEUE}.
     */
    @RabbitListener(queues = RabbitMQConfig.OCR_FINISHED_QUEUE)
    @Transactional
    public void receiveOcrFinishedMessage(UUID documentUuid) {
        log.info("OCR-Finished message received for document {}", kv("uuid", documentUuid));

        if (searchIntegration.getContent(documentUuid).isBlank()) {
            log.warn("OCR-Finished apparently indexed no content for document {}", kv("uuid", documentUuid));
        } else {
            documentRepoService.updateDocument(new DocumentUpdateStatusModel(documentUuid, DocumentStatus.OCR_FINISHED));
            log.info("Updated status for document {} to OCR_FINISHED", kv("uuid", documentUuid));
        }
    }

    /**
     * Reacts to the {@link RabbitMQConfig#GEN_AI_FINISHED_QUEUE}.
     */
    @RabbitListener(queues = RabbitMQConfig.GEN_AI_FINISHED_QUEUE)
    @Transactional
    public void receiveGenAiFinishedMessage(@NotNull @Valid GenAiFinishedMessage message) {
        log.info("Gen-AI-Finished message received for document uuid: {}", message.uuid());

        if (message.summary().isBlank()) {
            log.warn("Gen-AI summary was empty for document {}", kv("uuid", message.uuid()));
        } else {
            documentRepoService.updateDocument(new DocumentUpdateAiSummaryModel(message.uuid(), message.summary()));
            log.info("Updated AI-Summary and status for document {}", kv("uuid", message.uuid()));
        }
    }

}
