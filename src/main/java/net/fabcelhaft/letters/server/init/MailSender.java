package net.fabcelhaft.letters.server.init;

import lombok.extern.slf4j.Slf4j;
import net.fabcelhaft.letters.server.config.LettersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class MailSender implements  UserNotificationSender{

    private final LettersConfig lettersConfig;
    private final JavaMailSender emailSender;


    @Autowired
    public MailSender(LettersConfig lettersConfig, JavaMailSender emailSender) {
        this.lettersConfig = lettersConfig;
        this.emailSender = emailSender;
    }

    @Override
    public void sendNotificationToUser(String displayname, String mail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(lettersConfig.getMailfrom());
        message.setTo(mail);
        message.setSubject(lettersConfig.getMailsubject());
        message.setText(getMailText(displayname, mail, password));
        emailSender.send(message);
    }

    private String getMailText(String displayname, String mail, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lettersConfig.getMailgreeting());
        stringBuilder.append(" ");
        stringBuilder.append(displayname);
        stringBuilder.append(",\n\n");
        stringBuilder.append(lettersConfig.getMailtext());
        stringBuilder.append("\n\n");
        stringBuilder.append("URL: ");
        stringBuilder.append(lettersConfig.getUrl());
        stringBuilder.append("\n");
        stringBuilder.append("Mail: ");
        stringBuilder.append(mail);
        stringBuilder.append("\n");
        stringBuilder.append("Password: ");
        stringBuilder.append(password);
        return stringBuilder.toString();
    }

    @Bean
    public static JavaMailSender getJavaMailSender(LettersConfig lettersConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(lettersConfig.getMailserver());
        mailSender.setPort(lettersConfig.getMailport());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
