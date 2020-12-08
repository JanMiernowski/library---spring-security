package pl.jmiernowski.domain.user;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.email.EmailDto;
import pl.jmiernowski.domain.validator.Email;
import pl.jmiernowski.domain.validator.UniqueIsbn;
import pl.jmiernowski.domain.validator.UniqueUsername;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.email.EmailEntity;
import pl.jmiernowski.external.user.UserEntity;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    @NotBlank
    @UniqueUsername
    @Email
    private String username;
    private String password;
    private String role;
    private Boolean enabled = false;

    private Set<BookDto> borrowedBooks = new HashSet<>();
    private Set<EmailDto> emails = new HashSet<>();

    public UserDto(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDto(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDto(Long id, String username, String password, String role, Set<BookDto> borrowedBooks) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.borrowedBooks = borrowedBooks;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .enabled(userEntity.getEnabled())
                .borrowedBooks(UserDto.toDtoListBooks(userEntity.getBorrowedBooks()))
                .emails(UserDto.toDtoListEmails(userEntity.getEmails()))
                .build();
    }

    private static Set<BookDto> toDtoListBooks(Set<BookEntity> entities) {
        return Objects.isNull(entities) ?
                Set.of() :
                entities.stream()
                        .map(BookDto::toDto)
                        .collect(Collectors.toSet());

    }
    private static Set<EmailDto> toDtoListEmails(Set<EmailEntity> entities) {
        return Objects.isNull(entities) ?
                Set.of() :
                entities.stream()
                        .map(EmailDto::toDto)
                        .collect(Collectors.toSet());

    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.getId())
                .username(this.username)
                .password(this.getPassword())
                .role(this.getRole())
                .enabled(this.enabled)
                .borrowedBooks(UserDto.toEntitesListBook(this.borrowedBooks))
                .emails(UserDto.toEntitesListEmail(this.emails))
                .build();
    }

    private static Set<BookEntity> toEntitesListBook(Set<BookDto> dtos) {
        return Objects.isNull(dtos) ?
                Set.of() :
                dtos.stream()
                        .map(BookDto::toEntity)
                        .collect(Collectors.toSet());

    }

    private static Set<EmailEntity> toEntitesListEmail (Set<EmailDto> dtos) {
        return Objects.isNull(dtos) ?
                Set.of() :
                dtos.stream()
                        .map(EmailDto::toEntity)
                        .collect(Collectors.toSet());

    }
}
