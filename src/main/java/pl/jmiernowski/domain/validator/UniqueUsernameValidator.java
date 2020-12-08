package pl.jmiernowski.domain.validator;

import lombok.AllArgsConstructor;
import pl.jmiernowski.external.user.DatabaseUserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final DatabaseUserRepository databaseUserRepository;

    public void initialize(UniqueUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        return databaseUserRepository.findByUsername(username).isEmpty();
    }
}
