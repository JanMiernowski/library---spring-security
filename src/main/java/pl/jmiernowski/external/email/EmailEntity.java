package pl.jmiernowski.external.email;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"uuid"})
@Builder
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(length = 10000)
    private String content;
    private LocalDate date;

    @Transient
    private UUID uuid = UUID.randomUUID();


}
