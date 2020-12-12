package pl.jmiernowski.web.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.annotation.DirtiesContext;
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
import org.springframework.web.util.NestedServletException;
import pl.jmiernowski.config.RestTemplateConfig;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.book.BookRepository;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.book.JpaBookRepository;
import pl.jmiernowski.external.user.DatabaseUserRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
@SpringBootTest(classes = RestTemplateConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Autowired
    private DatabaseUserRepository databaseUserRepository;

    @Test
    void shouldAddBookToDbWithRestTemplate() {
        //given
        BookEntity book = new BookEntity(1L, "title", "author", "isbn",false, 20.0);

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); // ta metoda powinna lapac walidacje?

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
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

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
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("addBook.html"));
        //then
        assertEquals(bookRepository.getAll().size(),1);
    }

    @Test
    void shouldDeleteBook() throws Exception {
    //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        bookRepository.create(dto.toEntity());
    //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/books/removeBook/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    //then
        assertEquals(0,bookRepository.getAll().size());
    }

    @Test
    void shouldNotDeleteBookWhenIdIsNotInDb() throws Exception {
        //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        bookRepository.create(dto.toEntity());
        //when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/books/removeBook/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "contactWithAdmin.html");
        assertEquals(1,bookRepository.getAll().size());
    }

    @Test
    void shouldDisplayEditBookPage() throws Exception {
    //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        bookRepository.create(dto.toEntity());
        //when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/books/editBook/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "editBook.html");
        BookDto book = (BookDto) mav.getModel().get("book");
        assertEquals(book.getId(), dto.getId());
        assertEquals(book.getTitle(), dto.getTitle());
        assertEquals(book.getAuthor(), dto.getAuthor());
        assertEquals(book.getIsbn(), dto.getIsbn());
    }

    @Test
    void shouldNotDisplayEditBookPage() throws Exception {
        //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        bookRepository.create(dto.toEntity());
        //when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/books/editBook/3"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "contactWithAdmin.html");
        assertEquals(1,bookRepository.getAll().size());
    }

    @Test
    void shouldEditBook() throws Exception {
    //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        BookEntity entity = bookRepository.create(dto.toEntity());
        BookDto bookDto = BookDto.toDto(entity);

        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/books/editBook/1")
                .param("id", "1")
                .param("title", "title2")
                .param("author", "author2")
                .param("isbn", "isbn2")
                .param("isBorrow", "false")
                .param("price", "20.02")
                .flashAttr("book", new BookDto());

        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        //then
        assertEquals(1, bookRepository.getAll().size());
        BookEntity entityFromDb = bookRepository.getById(1L).get();
        assertEquals(entityFromDb.getTitle(), "title2");
        assertEquals(entityFromDb.getIsbn(), "isbn2");
        assertEquals(entityFromDb.getAuthor(), "author2");
        assertEquals(entityFromDb.getPrice().toString(), "20.02");
    }

    @Test
    void shouldNotEditBookWhenIdIsNotInDb() throws Exception {
        //given
        BookDto dto = new BookDto(1L, "title", "author", "isbn");
        BookEntity entity = bookRepository.create(dto.toEntity());
        BookDto bookDto = BookDto.toDto(entity);

        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/books/editBook/2")
                .param("id", "2")
                .param("title", "title2")
                .param("author", "author2")
                .param("isbn", "isbn2")
                .param("isBorrow", "false")
                .param("price", "20.02")
                .flashAttr("book", new BookDto());

        MvcResult mvcResult = mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        //then
        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "contactWithAdmin.html");


        assertEquals(1, bookRepository.getAll().size());
        BookEntity entityFromDb = bookRepository.getById(1L).get();
        assertEquals(entityFromDb.getTitle(), "title");
        assertEquals(entityFromDb.getIsbn(), "isbn");
        assertEquals(entityFromDb.getAuthor(), "author");
    }

    @Test
    void shouldNotEditBookWhenIsbnIsAlreadyInDb() throws Exception {
        //given
        BookDto dto1 = new BookDto(1L, "title1", "author1", "isbn1");
        BookDto dto2 = new BookDto(2L, "title2", "author2", "isbn2");
        bookRepository.create(dto1.toEntity());
        bookRepository.create(dto2.toEntity());

        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/books/editBook/1")
                .param("id", "1")
                .param("title", "title")
                .param("author", "author")
                .param("isbn", "isbn2")
                .param("isBorrow", "false")
                .param("price", "20.0")
                .flashAttr("book", new BookDto());

        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        //then


        assertEquals(2, bookRepository.getAll().size());
        BookEntity entityFromDb = bookRepository.getById(1L).get();
        assertEquals(entityFromDb.getTitle(), "title1");
        assertEquals(entityFromDb.getIsbn(), "isbn1");
        assertEquals(entityFromDb.getAuthor(), "author1");
    }

    @Test
    void shouldNotEditBookWhenIdIsNull() throws Exception {
        //given

        //when

       assertThrows(IllegalArgumentException.class, () -> mockMvc.perform(MockMvcRequestBuilders.post("/books/editBook/1")
               .param("id", null)
               .param("title", "title")
               .param("author", "author")
               .param("isbn", "isbn2")
               .param("isBorrow", "false")
               .param("price", "20.0")
               .flashAttr("book", new BookDto())));

        //then


        assertEquals(0, bookRepository.getAll().size());
    }

    @Test
    @WithMockUser(roles = "USER", username = "username")
    void shouldDisplayUserBooks() throws Exception {
    //given
        BookEntity book = new BookEntity(1L,"title","author","isbn", true, 20.0);
        BookEntity entityBook = bookRepository.create(book);
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",Set.of(entityBook), UUID.randomUUID());
        databaseUserRepository.create(userEntity);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/books/userBooks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("userBooks.html"))
                .andReturn();
    //then
        ModelAndView mav = mvcResult.getModelAndView();
        Set<BookEntity> books = (Set<BookEntity>) mav.getModel().get("books");
        assertEquals(1, books.size());
    }
    @Test
    @WithMockUser(roles = "USER", username = "username")
    void shouldNtoDisplayUserBooksWhenUserIsNotInDB() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/books/userBooks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("contactWithAdmin.html"))
                .andReturn();
        //then
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "username")
    void shouldNtoDisplayUserBooksWhenUserDoesNotHaveUserRole() throws Exception {
        //given
        BookEntity book = new BookEntity(1L,"title","author","isbn", true, 20.0);
        BookEntity entityBook = bookRepository.create(book);
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",Set.of(entityBook), UUID.randomUUID());
        databaseUserRepository.create(userEntity);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/books/userBooks"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("contactWithAdmin.html"))
                .andReturn();
        //then
    }

}
