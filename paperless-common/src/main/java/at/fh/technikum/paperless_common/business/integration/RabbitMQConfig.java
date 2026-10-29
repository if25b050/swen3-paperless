package at.fh.technikum.paperless_common.business.integration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Name der Queue, die der OCR-Worker abhört
    public static final String OCR_QUEUE = "ocr_queue";

    @Bean
    public Queue ocrQueue() {
        return new Queue(OCR_QUEUE, true);
    }
}
