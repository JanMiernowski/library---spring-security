package pl.jmiernowski.domain.book;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.domain.validator.UniqueIsbn;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.user.UserEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {

    private Long id;
    @NotBlank
    @NotNull
    private String title;
    @NotBlank
    @NotNull
    private String author;
    @NotBlank
    @NotNull
    @UniqueIsbn
    private String isbn;

    private UserDto user;


    public BookEntity toEntity(){
        if(user==null){
            return BookEntity.builder()
                    .id(this.getId())
                    .author(this.author)
                    .title(this.getTitle())
                    .isbn(this.getIsbn())
                    .build();
        }
        return BookEntity.builder()
                .id(this.getId())
                .author(this.author)
                .title(this.getTitle())
                .isbn(this.getIsbn())
                .user(this.getUser().toEntity())
                .build();
    }


    public static BookDto toDto(BookEntity entity){
        if(entity==null){
            return null;
        }
        if(entity.getUser()==null){
            return BookDto.builder()
                    .id(entity.getId())
                    .author(entity.getAuthor())
                    .title(entity.getTitle())
                    .isbn(entity.getIsbn())
                    .build();
        }
        return BookDto.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .user(UserDto.toDto(entity.getUser()))
                .build();
    }

}
