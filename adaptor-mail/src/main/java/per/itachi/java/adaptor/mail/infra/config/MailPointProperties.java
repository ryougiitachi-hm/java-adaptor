package per.itachi.java.adaptor.mail.infra.config;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailPointProperties {

    private String sender;

    private List<String> recipientTo = Collections.emptyList();

    private List<String> recipientCc = Collections.emptyList();

    private List<String> recipientBcc = Collections.emptyList();
}
