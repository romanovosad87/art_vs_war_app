package com.example.artvswar.lib;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = YearValidator.class)
public @interface Year {
    String message() default "year of creation can be in past or in present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

