package shop.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import shop.example.validator.impl.BirthConstraintImpl;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BirthConstraintImpl.class})
public @interface BirthConstraint {
    String message() default "{Invalid date of birth}";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}