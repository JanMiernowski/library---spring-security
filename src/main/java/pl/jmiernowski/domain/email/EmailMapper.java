package pl.jmiernowski.domain.email;

import org.springframework.stereotype.Component;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.email.EmailEntity;

@Component
public class EmailMapper {

    public EmailDto toDto(EmailEntity entity){
        if(entity == null) return null;
        return EmailDto.toDto(entity);
    }


    public EmailEntity toEntity(EmailDto dto){
        if(dto == null) return null;
        return dto.toEntity();
    }

}
