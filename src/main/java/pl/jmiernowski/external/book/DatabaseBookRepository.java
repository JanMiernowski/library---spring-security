package pl.jmiernowski.external.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jmiernowski.domain.book.BookRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class DatabaseBookRepository implements BookRepository {

    private final JpaBookRepository jpaBookRepository;

    @Override
    public BookEntity create(BookEntity entity) {
        return jpaBookRepository.save(entity);
    }

    @Override
    public BookEntity update(BookEntity entity) {
        return jpaBookRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        jpaBookRepository.deleteById(id);
    }

    @Override
    public Optional<BookEntity> getById(Long id) {
        return jpaBookRepository.findById(id);
    }

    @Override
    public List<BookEntity> getAll() {
        return jpaBookRepository.findAll();
    }

    @Override
    public Optional<BookEntity> findByTitle(String title) {
        return jpaBookRepository.findByTitle(title);
    }

    @Override
    public Optional<BookEntity> findByTAuthor(String author) {
        return jpaBookRepository.findByAuthor(author);
    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return jpaBookRepository.findByIsbn(isbn);
    }

}
