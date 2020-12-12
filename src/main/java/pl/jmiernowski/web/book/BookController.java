package pl.jmiernowski.web.book;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping
    ModelAndView displayBooksPage(){
        ModelAndView mav = new ModelAndView();
        List<BookDto> all = bookService.getAll();
        mav.addObject("books", all);
        mav.setViewName("books.html");

        return mav;
    }
    @GetMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    ModelAndView displayAddBookPage(){
        ModelAndView mav = new ModelAndView();
        BookDto dto = new BookDto();
        mav.addObject("book", dto);
        mav.setViewName("addBook.html");
        return mav;
    }

    @PostMapping("/addBook")
    String addBookToDb(@ModelAttribute("book") @Valid BookDto dto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){

            List<String> globalErrors = bindingResult.getGlobalErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("globalErrors", globalErrors);

            return "addBook.html";
        }
        bookService.create(dto);
        return "redirect:/";
    }

    @GetMapping("/removeBook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    String deleteBook(@PathVariable Long id){
        bookService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/editBook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ModelAndView editBookPage(@PathVariable Long id){
        BookDto editBook = bookService.getById(id).orElseThrow(RuntimeException::new);
        ModelAndView mav = new ModelAndView();
        mav.addObject("book", editBook);
        mav.setViewName("editBook.html");
        return mav;
    }

    @PostMapping("/editBook/{id}")
    String editBook(@ModelAttribute("book") BookDto dto){
        bookService.update(dto);
        return "redirect:/";
    }

    @GetMapping("/userBooks")
    @PreAuthorize("hasRole('USER')")
    ModelAndView displayUserBooks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        UserDto user = userService.findByUsername(name).orElseThrow(RuntimeException::new);
        ModelAndView mav = new ModelAndView("userBooks.html");
        mav.addObject("books", user.getBorrowedBooks());
        return mav;

    }

}
