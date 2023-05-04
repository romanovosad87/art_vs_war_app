package com.example.artvswar.lib;

import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<Year, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value <= LocalDateTime.now().getYear();
    }
}
