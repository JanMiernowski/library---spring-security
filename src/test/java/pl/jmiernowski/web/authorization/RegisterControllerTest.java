package pl.jmiernowski.web.authorization;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BindingResult mockBindingResult;

    @Mock
    private Model model;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;


    @Test
    void shouldDisplayRegisterPage() throws Exception {
    //given

    //when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("register.html"))
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        UserDto userDto = (UserDto) mav.getModel().get("user");
        assertEquals(userDto.getBorrowedBooks().size(),0);
        assertNull(userDto.getUsername());
        assertNull(userDto.getRole());
        assertNull(userDto.getPassword());
        assertNull(userDto.getId());
        assertEquals(userDto.getEnabled(),false);
    }

    @Test
    void shouldNotHandleUserRegistration() throws Exception {
    //given
        when(mockBindingResult.hasErrors()).thenReturn(false);
    //when
        RequestBuilder request = MockMvcRequestBuilders.post("/register")
                .param("username", "username@wp.pl")
                .param("password", "password")
                .param("role", "role")
                .flashAttr("user", new UserDto());
        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    //then
    }

    @Test
    void shouldHandleUserRegistration() throws Exception {
        //given
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getGlobalErrors()).thenReturn(List.of());
        when(model.addAttribute(anyString(),anyObject())).thenReturn(null);
        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("role", "role")
                .flashAttr("user", new UserDto());
        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("register.html"));
        //then
    }

    @Test
    void shouldActivateAccount() throws Exception {
    //given
        UserDto userDto = new UserDto(1L, "username","password","ADMIN",new HashSet<>());
        userService.create(userDto);
        Token token = tokenRepository.generateForUser(userDto.getUsername());
        //when
        RequestBuilder request = MockMvcRequestBuilders.get("/register/" + token.getTokenValue());
        MvcResult mvcResult = mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav , "activateSuccess.html");
    }


    @Test
    void shouldNotActivateAccount() throws Exception {
        //given

        //when
        RequestBuilder request = MockMvcRequestBuilders.get("/register/asd");
        MvcResult mvcResult = mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //then
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertModelAttributeValue(mav, "token", "asd");
        ModelAndViewAssert.assertViewName(mav , "tokenHasExpired.html");
    }

}
