package at.fh.technikum.paperless_common.business.integration.mqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {
    // Name der Queue, die der OCR-Worker abhört
    public static final String TRIGGER_OCR_QUEUE = "trigger_ocr_queue";

    // Name der Queue, in welche der OCR-Worker schreibt, wenn er fertig ist
    public static final String OCR_FINISHED_QUEUE = "ocr_finished_queue";

    // Name der Queue, die der Gen-AI-Worker abhört
    public static final String TRIGGER_GEN_AI_QUEUE = "trigger_gen_ai_queue";

    // Name der Queue, in welche der Gen-AI-Worker schreibt, wenn er fertig ist
    public static final String GEN_AI_FINISHED_QUEUE = "gen_ai_finished_queue";

    @Bean
    public Queue triggerOcrQueue() {
        return new Queue(TRIGGER_OCR_QUEUE, true);
    }

    @Bean
    public Queue ocrFinishedQueue() {
        return new Queue(OCR_FINISHED_QUEUE, true);
    }

    @Bean
    public Queue triggerGenAiQueue() {
        return new Queue(TRIGGER_GEN_AI_QUEUE, true);
    }

    @Bean
    public Queue genAiFinishedQueue() {
        return new Queue(GEN_AI_FINISHED_QUEUE, true);
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("at.fh.technikum.paperless_common.business.integration.mqueue.GenAiFinishedMessage", "java.util.*"));
        return converter;
    }


}
