package at.fh.technikum.paperless_ocr.business.integration;

import at.fh.technikum.paperless_common.business.BusinessUtil;
import at.fh.technikum.paperless_common.business.integration.file.FileIntegration;
import at.fh.technikum.paperless_common.business.integration.mqueue.RabbitMQConfig;
import at.fh.technikum.paperless_common.business.integration.search.SearchIntegration;
import at.fh.technikum.paperless_ocr.business.ocr.PdfRenderer;
import at.fh.technikum.paperless_ocr.business.ocr.TextRecognizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@Component
@Slf4j
public class OcrWorker {

    private final SearchIntegration searchIntegration;
    private final RabbitTemplate rabbitTemplate;
    private final FileIntegration fileIntegration;
    private final PdfRenderer pdfRenderer;
    private final TextRecognizer textRecognizer;

    public OcrWorker(SearchIntegration searchIntegration, RabbitTemplate rabbitTemplate,
                     FileIntegration fileIntegration, PdfRenderer pdfRenderer, TextRecognizer textRecognizer) {
        this.searchIntegration = searchIntegration;
        this.rabbitTemplate = rabbitTemplate;
        this.fileIntegration = fileIntegration;
        this.pdfRenderer = pdfRenderer;
        this.textRecognizer = textRecognizer;
    }

    // lauscht auf die TRIGGER_OCR_QUEUE
    @RabbitListener(queues = RabbitMQConfig.TRIGGER_OCR_QUEUE)
    public void receiveMessage(String documentUuid) throws IOException, InterruptedException {
        UUID uuid = BusinessUtil.convertToUUID(documentUuid);
        log.info("Starting OCR processing for document {}", kv("uuid", uuid));

        Path jobDir = Files.createTempDirectory("ocr-" + uuid + "-");
        Path inputFile = jobDir.resolve(uuid.toString());

        try {
            try (OutputStream out = Files.newOutputStream(inputFile)) {
                fileIntegration.getFile(uuid, out);
                log.debug("Downloaded {} bytes for document {}", Files.size(inputFile), kv("uuid", uuid));
            }

            List<Path> imageFiles = pdfRenderer.renderToPng(inputFile, jobDir);

            StringBuilder extractedText = new StringBuilder();

            for (int i = 0; i < imageFiles.size(); i++) {
                Path imageFile = imageFiles.get(i);
                Path outputBase = jobDir.resolve("page-%03d".formatted(i + 1));
                String pageText = textRecognizer.recognizeText(imageFile, outputBase);
                extractedText.append(pageText).append(System.lineSeparator());
            }

            String fullText = extractedText.toString().trim();
            searchIntegration.indexDocument(uuid, fullText);
            log.info("Indexed OCR text ({} chars) for document {}", fullText.length(), kv("uuid", uuid));

            rabbitTemplate.convertAndSend(RabbitMQConfig.OCR_FINISHED_QUEUE, documentUuid);
            rabbitTemplate.convertAndSend(RabbitMQConfig.TRIGGER_GEN_AI_QUEUE, documentUuid);
            log.info("Published OCR finished events for document {}", kv("uuid", uuid));
        } finally {
            deleteDirectory(jobDir);
        }
    }

    private void deleteDirectory(Path dir) {
        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.warn("Could not delete temporary path {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.warn("Could not walk temporary directory {}", dir, e);
        }
    }
}