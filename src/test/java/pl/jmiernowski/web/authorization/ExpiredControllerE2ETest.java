package pl.jmiernowski.web.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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
@WithMockUser(username = "username@wp.pl")
class ExpiredControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserService userService;

    @Test
    void shouldSendTokenAgain() throws Exception {
    //given
        UserDto dto = new UserDto(1L, "username@wp.pl","password","ADMIN",new HashSet<>());
        userService.create(dto);
        Token token = tokenRepository.generateForUser(dto.getUsername());
    //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/expired/" + token.getTokenValue()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("newTokenWasSent.html"));
    //then
    }

    @Test
    void shouldNotSendTokenAgainWhenTokenHasNotExpired() throws Exception {
        //given
        UserDto dto = new UserDto(1L, "username@wp.pl","password","ADMIN",new HashSet<>());
        userService.create(dto);
        Token token = new Token(1L, "asc","username@wp.pl", LocalDateTime.now());
        //when
        mockMvc
                .perform(MockMvcRequestBuilders.get("/expired/" + token.getTokenValue()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.view().name("contactWithAdmin.html"));
        //then
    }

}
