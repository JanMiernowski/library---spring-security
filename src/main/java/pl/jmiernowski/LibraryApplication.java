package pl.jmiernowski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.jmiernowski.config.NbpProperties;
import pl.jmiernowski.config.SmtpProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {SmtpProperties.class, NbpProperties.class})
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
