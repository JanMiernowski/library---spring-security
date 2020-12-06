package pl.jmiernowski.external.user;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.email.EmailEntity;

import javax.persistence.*;
import java.util.*;

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

    private Boolean enabled = false;

    public void activate(){
        enabled = true;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<BookEntity> borrowedBooks;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<EmailEntity> emails;
    @Transient
    private UUID uuid = UUID.randomUUID();

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.borrowedBooks = new HashSet<>();
    }

    public UserEntity(Long id, String username, String password, String role, Set<BookEntity> borrowedBooks, UUID uuid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.borrowedBooks = borrowedBooks;
        this.uuid = uuid;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

}
