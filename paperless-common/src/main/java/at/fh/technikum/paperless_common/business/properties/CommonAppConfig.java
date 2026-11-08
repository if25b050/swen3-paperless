package at.fh.technikum.paperless_common.business.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application-common.properties")
public class CommonAppConfig {
}
