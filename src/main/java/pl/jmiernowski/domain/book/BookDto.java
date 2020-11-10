package pl.jmiernowski.domain.book;

import lombok.*;
import pl.jmiernowski.external.book.BookEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String isbn;

    public BookEntity toEntity(){
        return BookEntity.builder()
                .id(this.getId())
                .author(this.author)
                .title(this.getAuthor())
                .isbn(this.getIsbn())
                .build();
    }

    public static BookDto toDto(BookEntity entity){
        return BookDto.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getAuthor())
                .isbn(entity.getIsbn())
                .build();
    }

}
