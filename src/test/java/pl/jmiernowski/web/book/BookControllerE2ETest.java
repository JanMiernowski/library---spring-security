package pl.jmiernowski.web.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.jmiernowski.config.RestTemplateConfig;
import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.book.JpaBookRepository;
import pl.jmiernowski.external.user.UserEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = RestTemplateConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerE2ETest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JpaBookRepository jpaBookRepository;



    @Test
    void shouldAddBookToDb(){
        //given
        BookEntity book = new BookEntity(1L,"title","author","isbn");

        BookEntity newBook = jpaBookRepository.save(book);
        HttpEntity<BookEntity> entity = new HttpEntity<>(newBook);
        //when
        ResponseEntity<Void> rsp = restTemplate.exchange(String.format("http://localhost:%d/books/addBook", port),
                HttpMethod.POST, entity, Void.class);
        //then
        Assertions.assertEquals(200, rsp.getStatusCodeValue());
        List<BookEntity> all = jpaBookRepository.findAll();
        assertEquals(all.size(),1);
    }

}
