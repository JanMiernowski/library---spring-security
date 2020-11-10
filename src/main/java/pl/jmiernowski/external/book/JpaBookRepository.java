package pl.jmiernowski.external.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<BookEntity, Long> {


    Optional<BookEntity> findByTitle(String title);

    Optional<BookEntity> findByAuthor(String author);

}
