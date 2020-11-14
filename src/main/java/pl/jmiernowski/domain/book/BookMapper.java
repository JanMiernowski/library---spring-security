package pl.jmiernowski.domain.book;

import org.springframework.stereotype.Component;
import pl.jmiernowski.external.book.BookEntity;
@Component
public class BookMapper {

    public BookDto toDto(BookEntity entity){
        if(entity == null) return null;
        return BookDto.toDto(entity);
    }


    public BookEntity toEntity(BookDto dto){
        if(dto == null) return null;
        return dto.toEntity();
    }

}
