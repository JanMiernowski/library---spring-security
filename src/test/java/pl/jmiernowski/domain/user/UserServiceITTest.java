package pl.jmiernowski.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import pl.jmiernowski.external.user.JpaUserRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceITTest {

    @Autowired
    private UserService userService;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldSaveUserInDb(){
        //given
        UserDto dto = new UserDto("username", "password", "ADMIN");
        //when
        userService.create(dto);
        //then
        List<UserEntity> all = jpaUserRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(1, all.get(0).getId());
        assertEquals("username", all.get(0).getUsername());
        assertTrue( passwordEncoder.matches( "password", all.get(0).getPassword()));
        assertEquals("ADMIN", all.get(0).getRole());
    }


    @Test
    void shouldUpdateUser() {
        //given
        UserDto dto = new UserDto("username", "password", "ADMIN");
        userService.create(dto);
        UserDto foundDto = userService.getById(1L).orElse(new UserDto());
        foundDto.setUsername("user");
        userService.update(foundDto);

        //when
        userService.update(foundDto);
        //then
        assertEquals("user", foundDto.getUsername());
    }

    @Test
    void shouldThrowAnExceprionWhenDtoIsntInDb() {
        //given
        UserDto dto = new UserDto(2L,"username", "password", "ADMIN");
        //when
        //then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.update(dto));
        assertEquals("Updated object not exists", exception.getMessage());
    }

    @Test
    void shouldDeleteUserById() {
        //given
        UserDto dto = new UserDto("username", "password", "ADMIN");
        userService.create(dto);
        //when
        userService.delete(1L);
        //then
        List<UserEntity> all = jpaUserRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void shouldFindUserById() {
        //given
        UserDto dto = new UserDto("username", "password", "ADMIN");
        userService.create(dto);
        //when
        UserDto foundDto = userService.getById(1L).orElse(new UserDto());
        //then
        List<UserEntity> all = jpaUserRepository.findAll();
        assertEquals("username", foundDto.getUsername());
        assertTrue(passwordEncoder.matches("password", foundDto.getPassword()));
        assertEquals("ADMIN",foundDto.getRole());
    }

    @Test
    void getAll() {
        //given
        UserDto dto1 = new UserDto("username1", "password1", "ADMIN1");
        UserDto dto2 = new UserDto("username2", "password2", "ADMIN2");
        userService.create(dto1);
        userService.create(dto2);
        //when
        List<UserDto> all = userService.getAll();
        //then
        assertEquals(2, all.size());
        assertEquals("username2", all.get(1).getUsername());
    }

    @Test
    void findByUsername() {
        //given
        UserDto dto1 = new UserDto("username1", "password1", "ADMIN1");
        UserDto dto2 = new UserDto("username2", "password2", "ADMIN2");
        userService.create(dto1);
        userService.create(dto2);
        //when
        UserDto foundDto = userService.findByUsername("username2").orElse(new UserDto());
        //then
        assertEquals("ADMIN2", foundDto.getRole());
        assertTrue(passwordEncoder.matches("password2", foundDto.getPassword()));
    }
}
