package pl.jmiernowski.web.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public ModelAndView displayRegisterPage() {
        ModelAndView mav = new ModelAndView("register.html");
        mav.addObject("user", new UserDto());
        return mav;
    }

    @PostMapping
    public String handleUserRegistration(@ModelAttribute UserDto dto) {
        userService.create(dto);
        return "redirect:/login";
    }

    @GetMapping("/{token}")
    public ModelAndView activateAccount(@PathVariable String token){
        ModelAndView mav = new ModelAndView();
        if(userService.activate(token)){
            mav.setViewName("activateSuccess.html");
            return mav;
        }
        mav.addObject("token", token);
        mav.setViewName("tokenHasExpired.html");
        return mav;
    }

}
