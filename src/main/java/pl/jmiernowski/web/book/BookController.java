package pl.jmiernowski.web.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    ModelAndView displayBooksPage(){
        ModelAndView mav = new ModelAndView();
        List<BookDto> all = bookService.getAll();
        mav.addObject("books", all);
        mav.setViewName("books.html");

        return mav;
    }

    @GetMapping("/addBook")
    ModelAndView displayAddBookPage(){
        ModelAndView mav = new ModelAndView();
        BookDto dto = new BookDto();
        mav.addObject("book", dto);
        mav.setViewName("addBook.html");
        return mav;
    }

    @PostMapping("/addBook")
    String addBookToDb(@ModelAttribute("book") BookDto dto){

        bookService.create(dto);
        return "redirect:/books";
    }

}
