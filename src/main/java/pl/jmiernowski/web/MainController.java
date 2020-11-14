package pl.jmiernowski.web;

import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.user.UserDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BookService bookService;

    @GetMapping
    public ModelAndView displayMainPage() {
        ModelAndView mav = new ModelAndView();
        List<BookDto> all = bookService.getAll();
        mav.addObject("books", all);
        mav.setViewName("index.html");

        return mav;
    }



}
