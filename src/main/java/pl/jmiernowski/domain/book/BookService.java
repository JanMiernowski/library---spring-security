package pl.jmiernowski.domain.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jmiernowski.external.book.BookEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookEntity create(BookDto dto){
        BookEntity entity = bookMapper.toEntity(dto);
        return bookRepository.create(entity);
    }
    public BookEntity update(BookDto dto){
        if(getById(dto.getId()).isEmpty()){
            throw new IllegalStateException("Updated object not exists");
        }
        BookEntity entity = bookMapper.toEntity(dto);
        return bookRepository.update(entity);
    }
    public void delete(Long id){
        bookRepository.delete(id);
    }
   public Optional<BookDto> getById(Long id){
        return bookRepository.getById(id)
                .map(bookMapper::toDto);
    }
    public List<BookDto> getAll(){
        List<BookEntity> all = bookRepository.getAll();
        if(all.isEmpty()) return Arrays.asList();
        return bookRepository.getAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
    public Optional<BookDto> findByTitle(String title){
        return bookRepository.findByTitle(title)
                .map(bookMapper::toDto);
    }
    public Optional<BookDto> findByAuthor(String author){
        return bookRepository.findByTAuthor(author)
                .map(bookMapper::toDto);
    }
}