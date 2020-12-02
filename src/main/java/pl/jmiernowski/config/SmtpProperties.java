package pl.jmiernowski.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.jmiernowski.domain.book.BookService;

@Getter
@Setter
@ConfigurationProperties(prefix = "email.smtp")
public class SmtpProperties {

    private Boolean auth = true;
    private Boolean starttls = true;
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String from;

//    email.smtp.auth=true
//    email.smtp.starttls.enable=true
//    email.smtp.host=smtp.gmail.com
//    email.smtp.port=587
//    email.smtp.user=javalub22test@gmail.com
//    email.smtp.password=Sda2022!
//    email.smtp.from=javalub22Test@sda.pl

}
