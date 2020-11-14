package pl.jmiernowski.domain.book;

import pl.jmiernowski.external.book.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    BookEntity create(BookEntity entity);
    BookEntity update(BookEntity entity);
    void delete(Long id);
    Optional<BookEntity> getById(Long id);
    List<BookEntity> getAll();
    Optional<BookEntity> findByTitle(String username);
    Optional<BookEntity> findByTAuthor(String username);

}
