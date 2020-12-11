package pl.jmiernowski.web.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;
import pl.jmiernowski.domain.user.UserRepository;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.book.DatabaseBookRepository;
import pl.jmiernowski.external.user.DatabaseUserRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.*;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.ModelAndViewAssert.assertAndReturnModelAttributeOfType;
import static org.springframework.test.web.ModelAndViewAssert.assertCompareListModelAttribute;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER", username = "username")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private DatabaseBookRepository databaseBookRepository;

    @Autowired
    private DatabaseUserRepository databaseUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldDisplayAllUserPage() throws Exception {
        //given
        List<UserEntity> all = databaseUserRepository.getAll();

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        //then
        ModelAndView mav = mvcResult.getModelAndView();
        assertCompareListModelAttribute(mav, "usersList", all);

    }

    @Test
    void shouldAddBookToUser() throws Exception {
    //given
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",new HashSet<>(), UUID.randomUUID());
        BookEntity bookEntity = new BookEntity(1L,"title","author","isbn", true, 20.0);
        databaseUserRepository.create(userEntity);
        databaseBookRepository.create(bookEntity);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andReturn();
        //then
        Set<BookEntity> usernameBooks = databaseUserRepository.findByUsername("username").get().getBorrowedBooks();
        assertEquals(1, usernameBooks.size());


    }
    @Test
    void shouldNotAddBookToUserWhenBooksIdIsNotInDb() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",new HashSet<>(), UUID.randomUUID());
        BookEntity bookEntity = new BookEntity(1L,"title","author","isbn", true, 20.0);
        databaseUserRepository.create(userEntity);
        databaseBookRepository.create(bookEntity);
        //when
        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/users/2")));
        //then
        Set<BookEntity> usernameBooks = databaseUserRepository.findByUsername("username").get().getBorrowedBooks();
        assertTrue(usernameBooks.isEmpty());
    }

    @Test
    void shouldGiveBackBook() throws Exception {
    //given
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",new HashSet<>(), UUID.randomUUID());
        BookEntity bookEntity = new BookEntity(1L,"title","author","isbn", true, 20.0);
        databaseUserRepository.create(userEntity);
        databaseBookRepository.create(bookEntity);
        userController.addBookToUser(1L);
    //when
        mockMvc.perform(MockMvcRequestBuilders.get("/users/giveBack/isbn"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/userBooks"))
                .andReturn();
    //then
        Set<BookEntity> usernameBooks = databaseUserRepository.findByUsername("username").get().getBorrowedBooks();

        assertEquals(0, usernameBooks.size());
    }

    @Test
    void shouldNotGiveBackBookWhenBookIsbnIsNotInDb() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username","password","ADMIN",new HashSet<>(), UUID.randomUUID());
        BookEntity bookEntity = new BookEntity(1L,"title","author","isbn", true, 20.0);
        databaseUserRepository.create(userEntity);
        databaseBookRepository.create(bookEntity);
        userController.addBookToUser(1L);
        //when
        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.get("/users/giveBack/isbn1")));

        //then
        Set<BookEntity> usernameBooks = databaseUserRepository.findByUsername("username").get().getBorrowedBooks();
        assertEquals(usernameBooks.size(), 1);
    }

}
