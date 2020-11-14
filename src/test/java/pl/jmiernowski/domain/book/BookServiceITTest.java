package pl.jmiernowski.domain.book;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.jmiernowski.domain.user.UserDto;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.book.JpaBookRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@SpringBootTest
class BookServiceITTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;
    @Autowired
    private BookService bookService;

    @AfterEach
    void init(){
        jpaBookRepository.deleteAll();
    }


    @Test
    void shouldSaveBookInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        //when
        bookService.create(dto);
        //then
        List<BookEntity> all = jpaBookRepository.findAll();
        assertEquals(all.size(), 1);
        BookEntity entity = all.get(0);
        assertEquals(entity.getAuthor(), "author");
        assertEquals(entity.getTitle(), "title");
        assertEquals(entity.getIsbn(), "isbn");

    }

    @Test
    void shouldUptadeBookInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        BookEntity createdEntity = bookService.create(dto);
        BookDto dto1 = new BookDto(createdEntity.getId(), "title1", "author1", "isbn1");
        //when
        bookService.update(dto1);
        //then
        List<BookEntity> all = jpaBookRepository.findAll();
        assertEquals(all.size(), 1);
        BookEntity entity = all.get(0);
        assertEquals(entity.getAuthor(), "author1");
        assertEquals(entity.getTitle(), "title1");
        assertEquals(entity.getIsbn(), "isbn1");

    }

    @Test
    void shouldThrowsExceptionWhenDtoIDIsntInDb() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(2L, "title1", "author1", "isbn1");
        //when
        //then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> bookService.update(dto1));
        assertEquals(exception.getMessage(), "Updated object not exists");
    }

    @Test
    void shouldDeleteBookInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        BookEntity createdEntity = bookService.create(dto1);
        //when
        bookService.delete(createdEntity.getId());
        //then
        List<BookEntity> all = jpaBookRepository.findAll();
        assertEquals(all.size(), 1);
    }

    @Test
    void shouldFindBookByIdWhenIdIsInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        BookEntity createdEntity = bookService.create(dto1);
        //when
        BookDto entity = bookService.getById(createdEntity.getId()).orElse(null);
        //then
        assertEquals(entity.getAuthor(), "author1");
        assertEquals(entity.getTitle(), "title1");
        assertEquals(entity.getIsbn(), "isbn1");

    }

    @Test
    void shouldFindReturnOptionalEmptyWhenIdIsNotInDb() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        bookService.create(dto1);
        //when
        BookDto entity = bookService.getById(5L).orElse(null);
        //then

        assertNull(entity);
    }
    @Test
    void shouldFindBookByTitleWhenTitleIsInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        bookService.create(dto1);
        //when
        BookDto entity = bookService.findByTitle("title1").orElse(null);
        //then
        assertEquals(entity.getAuthor(), "author1");
        assertEquals(entity.getTitle(), "title1");
        assertEquals(entity.getIsbn(), "isbn1");

    }
    @Test
    void shouldFindReturnOptionalEmptyWhenTitleIsNotInDb() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        bookService.create(dto1);
        //when
        BookDto entity = bookService.findByTitle("title2").orElse(null);
        //then

        assertNull(entity);
    }

    @Test
    void shouldFindBookByAuthorWhenAuthorIsInDatabase() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        bookService.create(dto1);
        //when
        BookDto entity = bookService.findByAuthor("author1").orElse(null);
        //then
        assertEquals(entity.getAuthor(), "author1");
        assertEquals(entity.getTitle(), "title1");
        assertEquals(entity.getIsbn(), "isbn1");

    }

    @Test
    void shouldFindReturnOptionalEmptyWhenAuthorIsNotInDb() {
        //given
        UserDto userDto = mock(UserDto.class);
        BookDto dto = new BookDto(null, "title", "author", "isbn");
        bookService.create(dto);
        BookDto dto1 = new BookDto(null, "title1", "author1", "isbn1");
        bookService.create(dto1);
        //when
        BookDto entity = bookService.findByAuthor("author2").orElse(null);
        //then

        assertNull(entity);
    }




}
