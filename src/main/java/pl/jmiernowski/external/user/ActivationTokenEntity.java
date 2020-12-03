package pl.jmiernowski.external.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ActivationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String token;

    private LocalDateTime validTo;
}
