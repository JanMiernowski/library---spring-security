package pl.jmiernowski.external.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;
import pl.jmiernowski.config.SmtpProperties;
import pl.jmiernowski.domain.email.Email;
import pl.jmiernowski.domain.email.EmailRepository;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailSender implements EmailRepository {

    private final SmtpProperties smtpProperties;

    @Override
    public void sendEmail(Email email) {
        Message msg = new MimeMessage(createSession());
        try {
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getSendTo(),false));
            msg.setSubject(email.getTitle());
            msg.setFrom(new InternetAddress(smtpProperties.getFrom(),false));
            //msg.setContent(email.getContent(), "text/html; charset=UTF-8");

            //teraz bedziemy tworzyc calosc maila z podzielonych czesci
            MimeBodyPart content = new MimeBodyPart();//dodajemy tresc
            content.setContent(email.getContent(),"text/html; charset=UTF-8");

            //dzielimy naszego maila na wiele czesci
            Multipart multipart = new MimeMultipart();// nasza calosc maila
            multipart.addBodyPart(content);

            if(email.getAttachment() != null && !email.getAttachment().isEmpty()){
                MimeBodyPart attachment = new MimeBodyPart();
                for (String path : email.getAttachment()) {
                    //dodawanie kolejnych zalacznikow
                    attachment.attachFile(
                            ResourceUtils.getFile("classpath:" + path));
                    //tak mozemy zrobic zeby byla sciezka do pliku wzgledna
                }
                multipart.addBodyPart(attachment);// dodajemy zalaczniki
            }

            msg.setContent(multipart);

            Transport.send(msg);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }




    //przygotowujemy obiekt, ktory zawiera wszytkie potrzebne
    // propertiesy potrzebne do polaczenia z serwerem smtp
    private Properties prepareProperties(){
        Properties properties = new Properties();
        //korzystamy z gmail, wiec musimy sie autentykowac
        properties.put("mail.smtp.auth", smtpProperties.getAuth());
        //jaki serwer
        properties.put("mail.smtp.starttls.enable", smtpProperties.getStarttls());
        //gdzie chcemy sie polaczyc, jaki hots
        properties.put("mail.smtp.host", smtpProperties.getHost());
        //na jakim porcie moj serwer jest wystawiony
        properties.put("mail.smtp.port", smtpProperties.getPort());
        //sciagniete z neta, zeby dzialalo!!!!!!!!!!!!
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return  properties;
    }
    //tworzymy sesje
    private Session createSession(){
        Session session = Session.getInstance(prepareProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpProperties.getUser(), smtpProperties.getPassword());
            }
        });
        return session;
    }


}
