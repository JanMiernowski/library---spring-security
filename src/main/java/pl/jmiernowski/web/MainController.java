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
import pl.jmiernowski.domain.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BookService bookService;
    private final UserService userService;
    private boolean init = true;

    @GetMapping
    public ModelAndView displayMainPage() {
        if (init) {
            setInit();
            init = false;
        }
        ModelAndView mav = new ModelAndView();
        List<BookDto> allAvailableBooks = new ArrayList<>();
        List<BookDto> allBooks = bookService.getAll();

        for (BookDto dtoBook:allBooks
             ) {
            if(!dtoBook.getIsBorrow()){
                allAvailableBooks.add(dtoBook);
            }
        }



        mav.addObject("books", allAvailableBooks);
        mav.setViewName("index.html");

        return mav;
    }

    private void setInit() {
        BookDto dto1 = new BookDto( "title1", "author1", "isbn1");
        BookDto dto2 = new BookDto( "title2", "author2", "isbn2");
        BookDto dto3 = new BookDto( "title3", "author3", "isbn3");
        BookDto dto4 = new BookDto( "title4", "author4", "isbn4");
        bookService.create(dto1);
        bookService.create(dto2);
        bookService.create(dto3);
        bookService.create(dto4);

    }


}
