package pl.jmiernowski.external.user;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jmiernowski.external.book.BookEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@EqualsAndHashCode(of = {"uuid"})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BookEntity> borrowedBooks;
    @Transient
    private UUID uuid = UUID.randomUUID();

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

}
