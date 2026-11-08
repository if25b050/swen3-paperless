package at.fh.technikum.paperless_common.business.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties Bean for all S3-Properties.
 */
@Component
@ConfigurationProperties(prefix = "s3")
@Getter
@Setter
public class S3Properties {
    private String url;
    private String bucket;
    private String accesskey;
    private String secretkey;
}
