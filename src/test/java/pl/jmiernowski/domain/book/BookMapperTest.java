package pl.jmiernowski.domain.book;

import org.junit.jupiter.api.Test;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.user.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    void shouldMapEntityToDto(){
        //given
        BookEntity entity = new BookEntity(1L,"title","author","isbn", new UserEntity());
        //when
        BookDto dto = mapper.toDto(entity);
        //then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getAuthor(), entity.getAuthor());
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getIsbn(), entity.getIsbn());
        assertEquals(dto.getUser().getUsername(), UserDto.toDto(entity.getUser()).getUsername());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull(){
        //given
        BookEntity entity = null;
        //when
        BookDto dto = mapper.toDto(entity);
        //then
        assertNull(dto); ;
    }

    @Test
    void shouldMapDtoToEntity(){
        //given
        BookDto dto = new BookDto(1L,"title","author","isbn",new UserDto());
        //when
        BookEntity entity = mapper.toEntity(dto);
        //then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getAuthor(), dto.getAuthor());
        assertEquals(entity.getTitle(), dto.getTitle());
        assertEquals(entity.getIsbn(), dto.getIsbn());
        assertEquals(entity.getUser(),  dto.getUser().toEntity());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull(){
        BookDto dto = null;
        //when
        BookEntity entity = mapper.toEntity(dto);
        //then
        assertNull(entity);
    }

}
