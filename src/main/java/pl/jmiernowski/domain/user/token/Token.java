package pl.jmiernowski.domain.user.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {

    private String token;
    private String username;
    private LocalDateTime validTo;

}
