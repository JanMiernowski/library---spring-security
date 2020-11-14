package pl.jmiernowski.domain.user;

import lombok.*;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.user.UserEntity;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String role;

    private List<BookDto> borrowedBooks = new ArrayList<>();


    public static UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .borrowedBooks(UserDto.toDtoList(userEntity.getBorrowedBooks()))
                .build();
    }

    private static List<BookDto> toDtoList(List<BookEntity> entities) {
        return Objects.isNull(entities) ?
                List.of() :
                entities.stream()
                        .map(BookDto::toDto)
                        .collect(Collectors.toList());

    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.getId())
                .username(this.username)
                .password(this.getPassword())
                .role(this.getRole())
                .borrowedBooks(UserDto.toEntitesList(this.borrowedBooks))
                .build();
    }

    private static List<BookEntity> toEntitesList(List<BookDto> dtos) {
        return Objects.isNull(dtos) ?
                List.of() :
                dtos.stream()
                        .map(BookDto::toEntity)
                        .collect(Collectors.toList());

    }


}