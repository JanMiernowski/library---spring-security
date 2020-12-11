package pl.jmiernowski.web.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import pl.jmiernowski.config.RestTemplateConfig;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookRepository;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.book.JpaBookRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
@SpringBootTest(classes = RestTemplateConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerE2ETest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldAddBookToDbWithRestTemplate() {
        //given
        BookEntity book = new BookEntity(1L, "title", "author", "isbn");

        BookEntity newBook = jpaBookRepository.save(book);
        HttpEntity<BookEntity> entity = new HttpEntity<>(newBook);
        //when
        ResponseEntity<Void> rsp = restTemplate.exchange(String.format("http://localhost:%d/books/addBook", port),
                HttpMethod.POST, entity, Void.class);
        //then
        Assertions.assertEquals(200, rsp.getStatusCodeValue());
        List<BookEntity> all = jpaBookRepository.findAll();
        assertEquals(all.size(), 1);
    }

    @Test
    void shouldDisplayBooksPage() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "books.html");
    }

    @Test
    void shouldDisplayAddBookPage() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/books/addBook"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "addBook.html");
    }

    @Test
    void shouldAddBookToDb() throws Exception {
        //given
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        RequestBuilder request = MockMvcRequestBuilders.post("/books/addBook")
                .param("id", "1")
                .param("title", "title")
                .param("author", "author")
                .param("isbn", "isbn")
                .param("isBorrow", "false")
                .param("price", "20.0")
                .flashAttr("book", new BookDto());
        //when

        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
        //then
        assertEquals(bookRepository.getAll().size(),1);
    }

    @Test
    void shouldNotAddBookToDb() throws Exception {
        //given

        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        bookRepository.create(dto.toEntity());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        RequestBuilder request = MockMvcRequestBuilders.post("/books/addBook")
                .param("id", "1")
                .param("title", "title")
                .param("author", "author")
                .param("isbn", "isbn")
                .param("isBorrow", "false")
                .param("price", "20.0")
                .flashAttr("book", new BookDto());
        //when

        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.model().attributeHasErrors("book"))
                .andExpect(MockMvcResultMatchers.view().name("addBook.html"));
        //then
        assertEquals(bookRepository.getAll().size(),1);
    }

}
