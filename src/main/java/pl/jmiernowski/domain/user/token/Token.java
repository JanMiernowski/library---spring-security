package pl.jmiernowski.domain.user.token;

import lombok.*;
import pl.jmiernowski.external.user.ActivationTokenEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Token {

    private String token;
    private String username;
    private LocalDateTime validTo;

    static public Token toToken(ActivationTokenEntity entity){
        return Token.builder()
                .token(entity.getToken())
                .username(entity.getUsername())
                .validTo(entity.getValidTo())
                .build();
    }

    public ActivationTokenEntity toEntity(){
        return ActivationTokenEntity.builder()
                .token(this.getToken())
                .username(this.username)
                .validTo(this.validTo)
                .build();
    }

}
