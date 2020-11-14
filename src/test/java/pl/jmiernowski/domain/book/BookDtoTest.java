package pl.jmiernowski.domain.book;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.user.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {

    @Test
    void shouldChangeBookDtoToBookEntity(){
        //given
        BookDto dto = new BookDto(1L,"title","author","isbn",new UserDto());
        //when
        BookEntity entity = dto.toEntity();
        //then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getAuthor(), dto.getAuthor());
        assertEquals(entity.getTitle(), dto.getTitle());
        assertEquals(entity.getIsbn(), dto.getIsbn());
        assertEquals(entity.getUser(),  dto.getUser().toEntity());
    }

    @Test
    void shouldChangeBookDtoToBookEntityWhenUserIsNull(){
        //given
        BookDto dto = new BookDto(1L,"title","author","isbn",null);
        //when
        BookEntity entity = dto.toEntity();
        //then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getAuthor(), dto.getAuthor());
        assertEquals(entity.getTitle(), dto.getTitle());
        assertEquals(entity.getIsbn(), dto.getIsbn());
        assertNull(entity.getUser());
    }

    @Test
    void shouldChangeEntityToDto(){
        //given
        BookEntity entity = new BookEntity(1L,"title","author","isbn", new UserEntity());
        //when
        BookDto dto = BookDto.toDto(entity);
        //then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getAuthor(), entity.getAuthor());
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getIsbn(), entity.getIsbn());
        assertEquals(dto.getUser().getUsername(), UserDto.toDto(entity.getUser()).getUsername());
    }

    @Test
    void shouldChangeEntityToDtoWhenUserIsNull(){
        //given
        BookEntity entity = new BookEntity(1L,"title","author","isbn", null);
        //when
        BookDto dto = BookDto.toDto(entity);
        //then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getAuthor(), entity.getAuthor());
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getIsbn(), entity.getIsbn());
        assertNull(dto.getUser());
    }

}
