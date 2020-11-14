package pl.jmiernowski.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME) //wykonywanie podczas dzialania programu
@Target(value = ElementType.FIELD) // ElementType.TYPE adnotacja ma byc nad klasą, Field nad polem
@Constraint(validatedBy = UniqueIsbnValidator.class) // odwolanie sie do klasy walidatora

public @interface UniqueIsbn {

    String message() default "This isbn is already exists"; //odpowiedz w przypadku bledu

    Class<?>[] groups() default {}; // mozna adnotacje polaczyc w grupy

    Class<? extends Payload>[] payload() default {}; //jakie dane beda walidowane

}

