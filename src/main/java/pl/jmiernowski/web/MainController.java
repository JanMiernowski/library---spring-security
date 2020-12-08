package pl.jmiernowski.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.model.Currency;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponse;
import pl.jmiernowski.external.exchangeRate.ExchangeRateClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BookService bookService;
    private final UserService userService;
    private boolean init = true;
    private Double mid = 1.0;
    private final ExchangeRateClient exchangeRateClient;

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
        Currency currency = new Currency();


        mav.addObject("mid", mid);
        mav.addObject("currency", currency);
        mav.addObject("books", allAvailableBooks);
        mav.addObject("currency", new Currency());
        mav.setViewName("index.html");

        return mav;
    }

    @PostMapping("/changeCurrency")
    String changeCurrency(@ModelAttribute("currency") Currency currency) throws JsonProcessingException {
        String currencyCode = currency.getCurrencyCode();
        if(currencyCode.equals("PLN")){
            mid=1.0;
        }else {
            CurrencyRateResponse currencyRate;

                currencyRate = exchangeRateClient.findCurrencyRate(currencyCode, LocalDate.now().minusDays(2));

            mid = currencyRate.getRates().get(0).getBid() * 100 / 100;
        }
        return "redirect:/";
    }

    private void setInit() {
        BookDto dto1 = new BookDto( "title1", "author1", "isbn1", 20.0);
        BookDto dto2 = new BookDto( "title2", "author2", "isbn2", 25.0);
        BookDto dto3 = new BookDto( "title3", "author3", "isbn3",30.0);
        BookDto dto4 = new BookDto( "title4", "author4", "isbn4", 35.0);
        bookService.create(dto1);
        bookService.create(dto2);
        bookService.create(dto3);
        bookService.create(dto4);

    }


}
