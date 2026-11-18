package at.fh.technikum.paperless_common.business.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties Bean for all S3-Properties.
 */
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
@Validated
public class ElasticsearchProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
