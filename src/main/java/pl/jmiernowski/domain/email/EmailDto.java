package pl.jmiernowski.domain.email;

import lombok.*;
import pl.jmiernowski.external.email.EmailEntity;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailDto {

    private Long id;
    private String username;
    private String content;
    private LocalDate date;

    public EmailEntity toEntity(){
        return EmailEntity.builder()
                .id(this.getId())
                .content(this.getContent())
                .date(this.getDate())
                .username(this.getUsername())
                .build();
    }

    public static  EmailDto toDto(EmailEntity entity){
        return EmailDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .date(entity.getDate())
                .username(entity.getUsername())
                .build();
    }

}
