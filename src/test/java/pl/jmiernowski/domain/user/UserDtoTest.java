package pl.jmiernowski.domain.user;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import pl.jmiernowski.domain.book.BookDto;
import pl.jmiernowski.external.user.UserEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void shouldReturnUserDtoFromUserEntity(){
        //given
        UserEntity entity = new UserEntity(1L,"username","password","ADMIN", new ArrayList<>(), UUID.randomUUID());
        //when
        UserDto dto = UserDto.toDto(entity);
        //then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(dto.getPassword(), entity.getPassword());
        assertEquals(dto.getRole(), entity.getRole());
        assertIterableEquals(dto.getBorrowedBooks(),
                entity.getBorrowedBooks().stream().map(BookDto::toDto).collect(Collectors.toList()));
    }

    @Test
    void shouldReturnUserEntityFromUserDto(){
        //given
        UserDto dto = new UserDto(1L,"username","password","ADMIN", new ArrayList<>());
        //when
       UserEntity entity = dto.toEntity();
        //then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getPassword(), dto.getPassword());
        assertEquals(entity.getRole(), dto.getRole());
        assertIterableEquals(entity.getBorrowedBooks(),
                dto.getBorrowedBooks().stream().map(BookDto::toEntity).collect(Collectors.toList()));
    }

}
