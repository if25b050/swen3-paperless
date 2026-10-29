package at.fh.technikum.paperless_rest.business.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OcrWorker {
    // lauscht auf die Queue
    @RabbitListener(queues = RabbitMQConfig.OCR_QUEUE)
    public void receiveMessage(String documentUuid) {
        log.info("Empty OCR Worker: Message received for document uuid: {}", documentUuid);
    }
}
