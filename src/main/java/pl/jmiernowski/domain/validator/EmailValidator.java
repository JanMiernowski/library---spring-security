package pl.jmiernowski.domain.validator;

import lombok.AllArgsConstructor;
import pl.jmiernowski.external.user.DatabaseUserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class EmailValidator implements ConstraintValidator<Email, String> {


    public void initialize(Email constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(username);
        return m.matches();
    }
}
