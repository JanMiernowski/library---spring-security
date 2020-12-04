package pl.jmiernowski.external.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ActivationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String token;

    private LocalDateTime validTo;
}
