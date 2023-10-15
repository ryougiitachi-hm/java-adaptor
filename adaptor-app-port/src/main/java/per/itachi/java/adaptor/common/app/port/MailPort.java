package per.itachi.java.adaptor.common.app.port;

import java.nio.file.Path;
import java.util.List;

public interface MailPort {

    /**
     * @param mailServerName The mail server name configured in config file.
     * @param mailPointName The mail sender and recipient name configured in config file.
     * @param mailTitle The email's title.
     * @param mailContent The content of mail to send.
     * @param attachments The list of attachment files.
     * */
    void send(String mailServerName, String mailPointName, String mailTitle, String mailContent, List<Path> attachments);
}
