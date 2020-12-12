package pl.jmiernowski.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.model.Currency;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponse;
import pl.jmiernowski.external.exchangeRate.ExchangeRateClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Getter
public class MainController {

    private final BookService bookService;
    public static Double mid = 1.0;
    private final ExchangeRateClient exchangeRateClient;

    @GetMapping
    public ModelAndView displayMainPage() {

        ModelAndView mav = new ModelAndView();
        List<BookDto> allAvailableBooks = new ArrayList<>();
        List<BookDto> allBooks = bookService.getAll();

        allAvailableBooks(allAvailableBooks, allBooks);
        Currency currency = new Currency();


        mav.addObject("mid", mid);
        mav.addObject("currency", currency);
        mav.addObject("books", allAvailableBooks);
        mav.addObject("currency", new Currency());
        mav.setViewName("index.html");

        return mav;
    }

    private void allAvailableBooks(List<BookDto> allAvailableBooks, List<BookDto> allBooks) {
        for (BookDto dtoBook: allBooks
             ) {
            if(!dtoBook.getIsBorrow()){
                allAvailableBooks.add(dtoBook);
            }
        }
    }

    @PostMapping("/changeCurrency")
    String changeCurrency(@ModelAttribute("currency") Currency currency) throws JsonProcessingException {
        String currencyCode = currency.getCurrencyCode();
        if(currencyCode.equals("PLN")){
            mid=1.0;
        }else {
            CurrencyRateResponse currencyRate;

                currencyRate = exchangeRateClient.findCurrencyRate(currencyCode, LocalDate.now());

            mid = currencyRate.getRates().get(0).getBid() * 100 / 100;
        }
        return "redirect:/";
    }
}
