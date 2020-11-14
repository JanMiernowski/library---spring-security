package pl.jmiernowski.web.user;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.config.SecurityUserDetailsService;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookService bookService;



    @GetMapping
    ModelAndView displayAllUsersPage(){
        ModelAndView mav = new ModelAndView("users.html");
        List<UserDto> all = userService.getAll().stream()
                .filter(dto -> !dto.getRole().equalsIgnoreCase("admin"))
                .collect(Collectors.toList());;
        mav.addObject("usersList", all);
        return mav;
    }

    @PostMapping("/{id}")
    String addBookToUser(@PathVariable Long id){
        BookDto book = bookService.getById(id).orElse(new BookDto());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        UserDto dto = userService.findByUsername(name).orElse(new UserDto());
        dto.getBorrowedBooks().add(book);
        userService.update(dto); //tu jest blad!!!!
        bookService.delete(id);
        return "redirect:/";
    }

}
