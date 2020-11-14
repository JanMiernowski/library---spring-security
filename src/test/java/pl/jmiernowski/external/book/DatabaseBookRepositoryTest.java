package pl.jmiernowski.external.book;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.jmiernowski.domain.book.BookMapper;
import pl.jmiernowski.external.user.DatabaseUserRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseBookRepositoryTest {

    private final JpaBookRepository jpaBookRepository = mock(JpaBookRepository.class);
    private final DatabaseBookRepository repository = new DatabaseBookRepository(jpaBookRepository);
    private final ArgumentCaptor<BookEntity> userCaptor = ArgumentCaptor.forClass(BookEntity.class);
    private final ArgumentCaptor<Long> longArgumentCaptor= ArgumentCaptor.forClass(Long.class);
    private final ArgumentCaptor<String> stringArgumentCaptor= ArgumentCaptor.forClass(String.class);



    @Test
    void shouldPersistUserInRepository(){
        //given
        BookEntity entity = new BookEntity(1L,"title","author","isbn");
        //when
        repository.create(entity);
        //then
        Mockito.verify(jpaBookRepository).save(userCaptor.capture());
        BookEntity capture = userCaptor.getValue();

        assertEquals(capture.getAuthor(), "author");
        assertEquals(capture.getTitle(), "title");
        assertEquals(capture.getIsbn(), "isbn");
    }
    @Test
    void shouldUpdateUserInRepository(){
        //given
        BookEntity entity = new BookEntity(1L,"title","author","isbn");
        //when
        repository.update(entity);
        //then
        Mockito.verify(jpaBookRepository).save(userCaptor.capture());
        BookEntity capture = userCaptor.getValue();

        assertEquals(capture.getAuthor(), "author");
        assertEquals(capture.getTitle(), "title");
        assertEquals(capture.getIsbn(), "isbn");
    }

    @Test
    void shouldDeleteUserInRepository(){
        //given
        //when
        repository.delete(1L);
        //then
        Mockito.verify(jpaBookRepository).deleteById(longArgumentCaptor.capture());
        Long value = longArgumentCaptor.getValue();
        assertEquals(value, 1L);
        Mockito.verify(jpaBookRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldFindOptionalEntityById(){
        //given
        //when
        repository.getById(1L);
        //then
        Mockito.verify(jpaBookRepository).findById(longArgumentCaptor.capture());
        Long value = longArgumentCaptor.getValue();
        assertEquals(value, 1L);
        Mockito.verify(jpaBookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindAllEntieties(){
        //given
        //when
        repository.getAll();
        //then
        Mockito.verify(jpaBookRepository, times(1)).findAll();
    }

    @Test
    void shouldFindEntityByTitle(){
        //given
        //when
        repository.findByTitle("title");
        //then
        Mockito.verify(jpaBookRepository).findByTitle(stringArgumentCaptor.capture());
        String value = stringArgumentCaptor.getValue();
        assertEquals(value, "title");
        Mockito.verify(jpaBookRepository, times(1)).findByTitle("title");
    }

    @Test
    void shouldFindEntityByAuthor(){
        //given
        //when
        repository.findByTAuthor("author");
        //then
        Mockito.verify(jpaBookRepository).findByAuthor(stringArgumentCaptor.capture());
        String value = stringArgumentCaptor.getValue();
        assertEquals(value, "author");
        Mockito.verify(jpaBookRepository, times(1)).findByAuthor("author");
    }




}
