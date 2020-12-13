package pl.jmiernowski.web.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;

import java.util.Optional;

@Controller
@RequestMapping("/expired")
@RequiredArgsConstructor
public class ExpiredController {

    private final TokenRepository tokenRepository;
    private final UserService userService;

    @GetMapping("/{token}")
    public String sendTokenAgain(@PathVariable String token){
        Optional<Token> byToken = tokenRepository.getByTokenValue(token);
        if(byToken.isPresent()){
            String username = byToken.get().getUsername();
            Optional<UserDto> byUsername = userService.findByUsername(username);
            tokenRepository.deleteToken(token);
            userService.sendRestartPasswordEmail(byUsername.get());
            return "newTokenWasSent.html";
        }
        return "newTokenWasntSent.html";
    }
}
