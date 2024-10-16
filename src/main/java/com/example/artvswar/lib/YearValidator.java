package com.example.artvswar.lib;

import java.time.LocalDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<Year, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value <= LocalDateTime.now().getYear();
    }
}
