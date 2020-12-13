package pl.jmiernowski.web.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.domain.user.UserService;
import pl.jmiernowski.domain.user.token.Token;
import pl.jmiernowski.domain.user.token.TokenRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LoginControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void shouldRestartPassword() throws Exception {
    //given
        UserDto userDto = new UserDto(1L, "username@wp.pl","password","ADMIN",new HashSet<>());
        userService.create(userDto);
        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/login/forgottenPassword")
                .param("user", "username@wp.pl");
        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("restartEmailSuccess.html"));
    //then
    }
    @Test
    void shouldNotRestartPasswordWhenUserIsNotInDb() throws Exception {
        //given

        //when
        RequestBuilder request = MockMvcRequestBuilders.post("/login/forgottenPassword")
                .param("user", "username@wp.pl");
        mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("restartEmailFailure.html"));
        //then
    }

    @Test
    void shouldDisplayRestartPasswordPage() throws Exception {
        //given
        UserDto userDto = new UserDto(1L, "username@wp.pl","password","ADMIN",false, new HashSet<>(), new HashSet<>());
        userService.create(userDto);
        Token token = tokenRepository.generateForUser(userDto.getUsername());
        token.setValidTo(LocalDateTime.now().plusDays(2));
        tokenRepository.update(token);
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/login/" + token.getTokenValue()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("restartPassword.html"));
        //then
    }


    @Test
    void shouldNotDisplayRestartPasswordPageWhenTokenIsExpired() throws Exception {
        //given
        UserDto userDto = new UserDto(1L, "username@wp.pl","password","ADMIN",false, new HashSet<>(), new HashSet<>());
        userService.create(userDto);
        Token token = tokenRepository.generateForUser(userDto.getUsername());
        token.setValidTo(LocalDateTime.now().minusDays(2));
        tokenRepository.update(token);
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/login/" + token.getTokenValue()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("tokenHasExpired.html"));
        //then
    }
    @Test
    void shouldNotDisplayRestartPasswordPageWhenTokenIsNotPresent() throws Exception {
        //given

        //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/login/" + "asda"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("contactWithAdmin.html"));
        //then
    }

    @Test
    void shouldSetNewPassword() throws Exception {
        //given
        UserDto userDto = new UserDto(1L, "username@wp.pl","password","ADMIN",false, new HashSet<>(), new HashSet<>());
        userService.create(userDto);
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.post("/login/setNewPassword")
                        .param("password", "password1")
                        .param("username", "username@wp.pl"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("setNewPassword.html"));
        //then
    }

    @Test
    void shouldNotSetNewPasswordWhenUsernameIsNotInDb() throws Exception {
        //given

        //when
        mockMvc
                .perform(MockMvcRequestBuilders.post("/login/setNewPassword")
                        .param("password", "password1")
                        .param("username", "username@wp.pl"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("contactWithAdmin.html"));
        //then
    }

}
