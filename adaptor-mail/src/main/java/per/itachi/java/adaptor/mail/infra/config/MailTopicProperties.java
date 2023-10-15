package per.itachi.java.adaptor.mail.infra.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailTopicProperties {

    private String titleTemplate;

    private String contentTemplate;
}
