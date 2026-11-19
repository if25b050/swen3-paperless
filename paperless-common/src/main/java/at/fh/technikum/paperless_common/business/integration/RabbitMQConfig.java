package at.fh.technikum.paperless_common.business.integration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Name der Queue, die der OCR-Worker abhört
    public static final String TRIGGER_OCR_QUEUE = "trigger_ocr_queue";

    // Name der Queue, in welche der OCR-Worker schreibt, wenn er fertig ist
    public static final String OCR_FINISHED_QUEUE = "ocr_finished_queue";

    @Bean
    public Queue triggerOcrQueue() {
        return new Queue(TRIGGER_OCR_QUEUE, true);
    }

    @Bean
    public Queue ocrFinishedQueue() {
        return new Queue(OCR_FINISHED_QUEUE, true);
    }

}
