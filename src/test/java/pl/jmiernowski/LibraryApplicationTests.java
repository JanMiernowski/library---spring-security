package pl.jmiernowski;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponse;
import pl.jmiernowski.external.exchangeRate.ExchangeRateClient;

import java.time.LocalDate;

@SpringBootTest
class LibraryApplicationTests {

    @Autowired
    private ExchangeRateClient exchangeRateClient;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldFetchExchangeRate() throws JsonProcessingException {
        //given
        String currency = "eur";
        LocalDate forDate = LocalDate.now();
        //when
        CurrencyRateResponse rate = exchangeRateClient.findCurrencyRate(currency, forDate);
        //then
        Assertions.assertNotNull(rate);
    }

}
