package pl.jmiernowski.external.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.jmiernowski.domain.book.BookService;
import pl.jmiernowski.domain.user.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DatabaseUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldSaveUserInDb() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        //when
        userRepository.create(userEntity);
        //then
        assertEquals(1, userRepository.getAll().size());
    }

    @Test
    void shouldUpdateUserInDb() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        String username = userEntity.getUsername();
        userRepository.create(userEntity);
        userEntity.setUsername("newUsername");
        //when
        userRepository.update(userEntity);
        //then
        UserEntity entity = userRepository.getById(1L).get();
        assertNotEquals(entity.getUsername(), username);
        assertEquals(1, userRepository.getAll().size());
    }

    @Test
    void shouldDeleteUserByIdFromDb() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity);
        //when
        userRepository.delete(1L);
        //then
        assertEquals(0, userRepository.getAll().size());
    }

    @Test
    void shouldGetUserById() {
        //given
        UserEntity userEntity = new UserEntity(1L, "username", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity);
        //when
        UserEntity entity = userRepository.getById(1L).get();
        //then
        assertEquals(userEntity.getId(), entity.getId());
        assertEquals(userEntity.getBorrowedBooks(), entity.getBorrowedBooks());
        assertEquals(userEntity.getEnabled(), entity.getEnabled());
        assertEquals(userEntity.getPassword(), entity.getPassword());
        assertEquals(userEntity.getUsername(), entity.getUsername());
        assertEquals(userEntity.getRole(), entity.getRole());
    }

    @Test
    void shouldGetAllUsers() {
        //given
        UserEntity userEntity1 = new UserEntity(1L, "username1", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        UserEntity userEntity2 = new UserEntity(2L, "username2", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity1);
        userRepository.create(userEntity2);
        //when
        List<UserEntity> all = userRepository.getAll();
        //then
        assertEquals(2, all.size());
    }

    @Test
    void shouldFindUserByUsername(){
    //given
        UserEntity userEntity = new UserEntity(1L, "username1", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        UserEntity userEntity2 = new UserEntity(2L, "username2", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity);
        userRepository.create(userEntity2);
    //when
        UserEntity entity = userRepository.findByUsername("username1").get();
        //then
        assertEquals(userEntity.getId(), entity.getId());
        assertEquals(userEntity.getBorrowedBooks(), entity.getBorrowedBooks());
        assertEquals(userEntity.getEnabled(), entity.getEnabled());
        assertEquals(userEntity.getPassword(), entity.getPassword());
        assertEquals(userEntity.getUsername(), entity.getUsername());
        assertEquals(userEntity.getRole(), entity.getRole());
    }
    @Test
    void shouldNotFindUserByUsername(){
        //given
        UserEntity userEntity = new UserEntity(1L, "username1", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        UserEntity userEntity2 = new UserEntity(2L, "username2", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity);
        userRepository.create(userEntity2);
        //when
        Optional<UserEntity> byUsername = userRepository.findByUsername("");
        //then
        assertFalse(byUsername.isPresent());
    }

    @Test
    void shouldActivateUser(){
        //given
        UserEntity userEntity = new UserEntity(1L, "username1", "password", "ADMIN", new HashSet<>(), UUID.randomUUID());
        userRepository.create(userEntity);

        //when
        userRepository.activate("username1");
        //then
        UserEntity entity = userRepository.getById(1L).get();
        assertTrue(entity.getEnabled());
    }

}
