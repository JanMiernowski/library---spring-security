package pl.jmiernowski.domain.sendEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.jmiernowski.domain.email.EmailDto;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Email {

    private String sendTo;
    private String title;
    private String content;
    private Set<String> attachment;

    public EmailDto toEmailDto(){
        return EmailDto.builder()
                .content(this.content)
                .date(LocalDate.now())
                .username(this.sendTo)
                .build();
    }

}
