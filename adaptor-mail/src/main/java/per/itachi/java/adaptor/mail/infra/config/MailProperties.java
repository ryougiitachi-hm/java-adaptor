package per.itachi.java.adaptor.mail.infra.config;

import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailProperties {

    private Map<String, MailTopicProperties> topics = Collections.emptyMap();

    private Map<String, MailServerProperties> servers = Collections.emptyMap();

    private Map<String, MailPointProperties> points = Collections.emptyMap();
}
