package at.fh.technikum.paperless_gen_ai.business.integration;

import at.fh.technikum.paperless_common.business.integration.mqueue.GenAiFinishedMessage;
import at.fh.technikum.paperless_common.business.integration.mqueue.RabbitMQConfig;
import at.fh.technikum.paperless_common.business.integration.search.SearchIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

/**
 * Worker which works Gen-AI-Requests by retrieving the text-content of a document
 * via {@link SearchIntegration} and generating an AI-Summary.
 * <p>
 * The response is sent back via MessageQueue.
 */
@Component
@Slf4j
public class GenAiWorker {

    private final SearchIntegration searchIntegration;
    private final RabbitTemplate rabbitTemplate;

    public GenAiWorker(SearchIntegration searchIntegration, RabbitTemplate rabbitTemplate) {
        this.searchIntegration = searchIntegration;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.TRIGGER_GEN_AI_QUEUE)
    public void receiveMessage(UUID uuid) {
        log.info("Starting AI processing for document {}", kv("uuid", uuid));
        String content = searchIntegration.getContent(uuid);

        log.info("Retrieved content for AI processing for document {}", kv("uuid", uuid));
        // TODO Generate content summary and send it

        log.info("Sending AI-Summary of document {}", kv("uuid", uuid));
        rabbitTemplate.convertAndSend(RabbitMQConfig.GEN_AI_FINISHED_QUEUE,
                new GenAiFinishedMessage(uuid, "Test Generierte Summary"));

        log.info("Finished AI-Processing of document {}", kv("uuid", uuid));
    }
}