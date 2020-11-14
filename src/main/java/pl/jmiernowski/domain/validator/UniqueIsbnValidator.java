package pl.jmiernowski.domain.validator;

import lombok.AllArgsConstructor;
import pl.jmiernowski.domain.book.BookService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
@AllArgsConstructor
public class UniqueIsbnValidator implements ConstraintValidator<UniqueIsbn, String> {

    private final BookService bookService;

    public void initialize(UniqueIsbn constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return bookService.getAll().stream()
                .noneMatch(bookDto -> bookDto.getIsbn().equalsIgnoreCase(value) );
    }
}
