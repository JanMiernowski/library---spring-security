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
    private Long id;
    private String tokenValue;
    private String username;
    private LocalDateTime validTo;

    static public Token toToken(ActivationTokenEntity entity){
        return Token.builder()
                .id(entity.getId())
                .tokenValue(entity.getTokenValue())
                .username(entity.getUsername())
                .validTo(entity.getValidTo())
                .build();
    }

    public ActivationTokenEntity toEntity(){
        return ActivationTokenEntity.builder()
                .id(this.id)
                .tokenValue(this.getTokenValue())
                .username(this.username)
                .validTo(this.validTo)
                .build();
    }

}
