package per.itachi.java.adaptor.mail.infra.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailServerProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private Boolean sessionDebug = Boolean.FALSE;
}
