package pl.jmiernowski.web;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.model.Currency;
import pl.jmiernowski.external.exchangeRate.CurrencyRateResponse;
import pl.jmiernowski.external.exchangeRate.ExchangeRateClient;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc

class MainControllerTest {

    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private BookService bookService;
    @Autowired
    private final ExchangeRateClient exchangeRateClient = mock(ExchangeRateClient.class);

    @Test
    void shouldDisplayMainPage() throws Exception {
    //given
        List<BookDto> allAvailableBooks = new ArrayList<>();
        BookDto bookDto1 = new BookDto(1L,"title1","author1","isbn1", true, 20.0);
        BookDto bookDto2 = new BookDto(2L,"title2","author2","isbn2", false, 20.0);
        BookDto bookDto3 = new BookDto(3L,"title3","author3","isbn3", true, 20.0);
        BookDto bookDto4 = new BookDto(4L,"title4","author4","isbn4", false, 20.0);

        bookService.create(bookDto1);
        bookService.create(bookDto2);
        bookService.create(bookDto3);
        bookService.create(bookDto4);

    //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("index.html"))
                .andReturn();
    //then
        ModelAndView mav = mvcResult.getModelAndView();
        allAvailableBooks = (List<BookDto>)mav.getModel().get("books");
        assertEquals(2, allAvailableBooks.size());
    }

    @Test
    void shouldChangeCurrencyOn1WhenCurrencyCodeIsPLN() throws Exception {
        //given

        //when

        RequestBuilder request = MockMvcRequestBuilders.post("/changeCurrency")
                .param("currencyRate", "0")
                .param("currencyCode", "PLN")
                .flashAttr("currency", new Currency());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andReturn();
        //then
        assertEquals(MainController.mid, 1);
    }

//    @Test
//    void shouldChangeCurrencyOnDownloadedValueWhenCurrencyCodeIsNotPLN() throws Exception {
//        //given
//        final CurrencyRateResponse currencyRateResponse = new CurrencyRateResponse();
//        final ArgumentCaptor<LocalDate> localDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
//
//        when(exchangeRateClient.findCurrencyRate(anyString(), localDateArgumentCaptor.capture())).thenReturn(currencyRateResponse);
//        when(currencyRateResponse.getRates().get(0).getBid()).thenReturn(4.5);
//
//        //when
//
//        RequestBuilder request = MockMvcRequestBuilders.post("/changeCurrency")
//                .param("currencyRate", "0")
//                .param("currencyCode", "EUR")
//                .flashAttr("currency", new Currency());
//
//        MvcResult mvcResult = mockMvc.perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
//                .andReturn();
//        //then
//        assertEquals(MainController.mid, 4.5);
//    }

}
