package per.itachi.java.adaptor.mail.infra.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import per.itachi.java.adaptor.mail.infra.config.MailProperties;

@Configuration
public class EmailConfig {

    @Bean
    @ConfigurationProperties("infra.mail")
    public MailProperties mailProperties() {
        return new MailProperties();
    }
}
