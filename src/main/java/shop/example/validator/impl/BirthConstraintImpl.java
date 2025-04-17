package shop.example.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import shop.example.validator.BirthConstraint;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthConstraintImpl implements ConstraintValidator<BirthConstraint, LocalDate> {
    private int min;

    @Override
    public void initialize(BirthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        long years = ChronoUnit.YEARS.between(value, LocalDate.now());
        return years >= min;
    }
}
