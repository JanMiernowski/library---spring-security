package pl.jmiernowski.web.nbp;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.model.UserMoneyExchange;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponseForTableAandB;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponseForTableC;
import pl.jmiernowski.external.exchangeRate.ExchangeRateClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/nbp")
public class NbpController {

    private final ExchangeRateClient exchangeRateClient;
    private List<CurrencyRateResponseForTableC.Rate> rates;
    private Double userAmount = 1.0;

    @GetMapping
    ModelAndView displayNbpPage(){
        ModelAndView mav = new ModelAndView();
        rates = new ArrayList<>();
        mav.addObject("userInput", new UserMoneyExchange());
        mav.addObject("rates", rates);

        mav.setViewName("nbp/nbp.html");
        return mav;
    }

    @PostMapping
    ModelAndView exchangeUserAmount(@ModelAttribute("userInput")UserMoneyExchange userMoneyExchange) throws JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = userMoneyExchange.getLocalDate();
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<CurrencyRateResponseForTableC> currencyRateForUser = exchangeRateClient.findCurrencyRateForTableC(localDate);

        rates = currencyRateForUser.get(0).getRates();

        ModelAndView mav = new ModelAndView();
        userAmount = userMoneyExchange.getAmount();
        mav.addObject("rates", rates);
        mav.addObject("userAmount", userAmount);
        mav.setViewName("nbp/nbp.html");
        return mav;
    }


}
