package at.fh.technikum.paperless_ocr.business.integration;

import at.fh.technikum.paperless_common.business.BusinessUtil;
import at.fh.technikum.paperless_common.business.integration.RabbitMQConfig;
import at.fh.technikum.paperless_common.business.integration.search.SearchIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OcrWorker {

    private final SearchIntegration searchIntegration;
    private final RabbitTemplate rabbitTemplate;

    public OcrWorker(SearchIntegration searchIntegration, RabbitTemplate rabbitTemplate) {
        this.searchIntegration = searchIntegration;
        this.rabbitTemplate = rabbitTemplate;
    }

    // lauscht auf die TRIGGER_OCR_QUEUE
    @RabbitListener(queues = at.fh.technikum.paperless_common.business.integration.RabbitMQConfig.TRIGGER_OCR_QUEUE)
    public void receiveMessage(String documentUuid) {
        log.info("Empty OCR Worker: Message received for document uuid: {}", documentUuid);

        // TODO index real content here
        searchIntegration.indexDocument(BusinessUtil.convertToUUID(documentUuid), "TEST CONTENT");

        rabbitTemplate.convertAndSend(RabbitMQConfig.OCR_FINISHED_QUEUE, documentUuid);
    }
}
