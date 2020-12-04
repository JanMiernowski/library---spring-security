package pl.jmiernowski.domain.user.token;

import java.util.Optional;

public interface TokenRepository {

    Token generateForUser(String user);
    Optional<Token> getByToken(String token);
    void deleteToken(String token);
}
