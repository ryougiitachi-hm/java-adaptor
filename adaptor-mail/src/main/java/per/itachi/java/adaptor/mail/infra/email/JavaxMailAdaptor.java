package per.itachi.java.adaptor.mail.infra.email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import per.itachi.java.adaptor.common.app.port.MailPort;
import per.itachi.java.adaptor.mail.infra.config.MailPointProperties;
import per.itachi.java.adaptor.mail.infra.config.MailProperties;
import per.itachi.java.adaptor.mail.infra.config.MailServerProperties;

@Slf4j
@Component
public class JavaxMailAdaptor implements MailPort {

    @Autowired
    private MailProperties mailProperties;

    @Override
    public void send(String mailServerName, String mailPointName, String mailTitle, String mailContent, List<Path> attachments) {
        // check availability of mail server and mail topic
        MailServerProperties mailServerProperties = mailProperties.getServers().get(mailServerName);
        if (mailServerProperties == null) {
            log.error("Failed to send email due to invalid mailServerName, mailServerName={}, mailServers={}. ",
                    mailServerName, mailProperties.getServers().keySet());
            return;
        }
        MailPointProperties mailPointProperties = mailProperties.getPoints().get(mailPointName);
        if (mailPointProperties == null) {
            log.error("Failed to send email due to invalid mailPointName, mailPointName={}, mailTopics={}. ",
                    mailPointName, mailProperties.getPoints().keySet());
            return;
        }
        // initialize email session.
        Session session = createSession(mailServerProperties);
        try {
            MimeMessage message = createMessage(session, mailPointProperties, mailTitle, mailContent, attachments);
            Transport.send(message);
        }
        catch (MessagingException | IOException e) {
            log.error("Failed to send email due to exception, mailServerName={}, mailPointName={}. ",
                    mailServerName, mailPointName, e);
        }
    }

    private Session createSession(MailServerProperties mailServerProperties) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailServerProperties.getHost());
//        properties.put("mail.smtp.port", String.valueOf(mailServerProperties.getPort()));
        properties.put("mail.smtp.port", mailServerProperties.getPort());
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
        // username and password are authorized in the Authenticator.
//        Session session = Session.getDefaultInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(mailServerProperties.getUsername(), mailServerProperties.getPassword());
//            }
//        });
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(Boolean.TRUE.equals(mailServerProperties.getSessionDebug())); // print debug information into console.
        return session;
    }

    private MimeMessage createMessage(Session session, MailPointProperties mailPointProperties,
                                      String mailTitle,
                                      String mailContent)
            throws MessagingException, IOException {
        return createMessage(session, mailPointProperties, mailTitle, mailContent, Collections.emptyList());
    }

    private MimeMessage createMessage(Session session, MailPointProperties mailPointProperties,
                                      String mailTitle, String mailContent, List<Path> attachments)
            throws MessagingException, IOException {
        // handle email mail body content as MimeBodyPart
//        MimeBodyPart mimeBodyPartContent = new MimeBodyPart();
//        mimeBodyPartContent.setContent(mailContent, "text/html; charset=utf-8");

        // handle attachments
        List<MimeBodyPart> bodyAttachments = new ArrayList<>();
        for (Path attachment : attachments) {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setFileName(attachment.getFileName().toString()); // the name shown in the email.
//            mimeBodyPart.setContent(mailContent, "application/octet-stream");
            mimeBodyPart.setDataHandler(new DataHandler(
                    new ByteArrayDataSource(Files.readAllBytes(attachment),
                            "application/octet-stream")));
            bodyAttachments.add(mimeBodyPart);
        }

//        Multipart multipart = new MimeMultipart("mixed");
        Multipart multipart = new MimeMultipart();
        for (MimeBodyPart bodyAttachment : bodyAttachments) {
            multipart.addBodyPart(bodyAttachment);
        }

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailPointProperties.getSender()));
        handleMessageRecipient(message, Message.RecipientType.TO, mailPointProperties.getRecipientTo());
        handleMessageRecipient(message, Message.RecipientType.CC, mailPointProperties.getRecipientTo());
        handleMessageRecipient(message, Message.RecipientType.BCC, mailPointProperties.getRecipientTo());
        message.setSubject(mailTitle);
        message.setText(mailContent);
//        message.setText(mailContent, "text/html; charset=utf-8");
        if (!CollectionUtils.isEmpty(bodyAttachments)) {
            message.setContent(multipart);
        }
        return message;
    }

    private void handleMessageRecipient(MimeMessage message, Message.RecipientType recipientType, List<String> recipients)
            throws MessagingException {
        if (CollectionUtils.isEmpty(recipients)) {
            log.debug("The list of {} is empty and no need to add. ", recipientType);
            return;
        }
        for (String recipient: recipients) {
//            message.addRecipients(recipientType, recipient); // recipient is a list in this case.
            message.addRecipient(recipientType, new InternetAddress(recipient));
        }
    }
}
