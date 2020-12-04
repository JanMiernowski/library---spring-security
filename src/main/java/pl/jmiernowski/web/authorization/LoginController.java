package pl.jmiernowski.web.authorization;

import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    String displayLogin(){
        return "login.html";
    }

    @GetMapping("/forgottenPassword")
    String displayForgottenPasswordPage(){
//        ModelAndView mav = new ModelAndView();
//        UserDto user = new UserDto();
//        mav.addObject("user", user);
        return "forgottenPassword.html";
    }

    @PostMapping("/forgottenPassword")
    ModelAndView restartPassword( @RequestParam String user){
        Optional<UserDto> byUsername = userService.findByUsername(user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        if(byUsername.isPresent()){
            userService.sendRestartPasswordEmail(byUsername.get());
            mav.setViewName("restartEmailSuccess.html");
            return mav;
        }
        mav.setViewName("restartEmailFailure.html");
        return mav;
    }

    @GetMapping("/{token}")
    public ModelAndView restartPasswordDisokayPage(@PathVariable String token) {
        Optional<Token> byToken = tokenRepository.getByToken(token);
        ModelAndView mav = new ModelAndView();
        if (byToken.isPresent()) {
            if(byToken.get().getValidTo().isAfter(LocalDateTime.now())) {
                String username = byToken.get().getUsername();
                Optional<UserDto> byUsername = userService.findByUsername(username);
                if (byUsername.isPresent()) {
                    UserDto userDto = byUsername.get();
                    mav.addObject("user", userDto);
                    mav.setViewName("restartPassword.html");
                    return mav;
                }
            }else{
                mav.addObject("token", byToken.get().toString());
                mav.setViewName("tokenHasExpired.html");
                return mav;
            }
        }
        mav.setViewName("contactWithAdmin.html");
        return mav;
    }

    @PostMapping("/newPassword")
    String newPassword( @RequestParam String password){

        return "forgottenPassword.html";
    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(@RequestParam String password, @RequestParam String username){
        UserDto userDto = userService.findByUsername(username).get();
        userDto.setPassword(password);
        UserEntity entity = userDto.toEntity();
        entity.encodePassword(passwordEncoder);
        UserDto userDto1 = UserDto.toDto(entity);
        userService.update(userDto1);
        return "setNewPassword.html";
    }

    @PostMapping("/login/sendTokenAgain")
    public String sendTokenAgain(@RequestParam String token){
        Optional<Token> byToken = tokenRepository.getByToken(token);
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
