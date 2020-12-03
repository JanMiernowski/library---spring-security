package pl.jmiernowski.external.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DatabaseTokenRepository implements TokenRepository {

    private final JpaActivationTokenRepository activationTokenRepository;

    @Override
    public Token generateForUser(String user) {
        ActivationTokenEntity entity = new ActivationTokenEntity(null, user, UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(1));

        activationTokenRepository.save(entity);

        return new Token(entity.getToken(), entity.getUsername(), entity.getValidTo());
    }

    @Override
    public Optional<Token> getByToken(String token) {
        return activationTokenRepository.findByToken(token)
                .map(entity -> new Token(entity.getToken(),
                        entity.getUsername(),entity.getValidTo()));
    }
}
