package at.fh.technikum.paperless_common.business.integration.search;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Object which is used to create a document in the index in Elasticsearch.
 */
record ElasticsearchDocument(@NotNull UUID id, @NotNull String content, @NotBlank @DateTimeFormat String timestamp) {

    ElasticsearchDocument(UUID id, String content) {
        this(id, content, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    LocalDateTime getTimestamp() {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }
}
