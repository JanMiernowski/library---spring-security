package pl.jmiernowski.web.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public String handleUserRegistration(@ModelAttribute("user") @Valid UserDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> globalErrors = bindingResult.getGlobalErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("globalErrors", globalErrors);
            model.addAttribute("user", dto);
            return "register.html";
        }
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
