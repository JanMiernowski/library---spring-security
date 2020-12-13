package pl.jmiernowski.domain.user.token;

import java.util.Optional;

public interface TokenRepository {

    Token generateForUser(String user);
    Optional<Token> getByTokenValue(String token);
    void deleteToken(String token);
    void update(Token token);

}
