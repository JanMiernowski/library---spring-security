package pl.jmiernowski.domain.book;

import pl.jmiernowski.external.book.BookEntity;
import pl.jmiernowski.external.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    void create(BookEntity entity);
    void update(BookEntity entity);
    void delete(Long id);
    Optional<BookEntity> getById(Long id);
    List<BookEntity> getAll();
    Optional<BookEntity> findByTitle(String username);
    Optional<BookEntity> findByTAuthor(String username);

}
