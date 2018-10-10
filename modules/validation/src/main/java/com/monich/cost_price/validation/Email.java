package com.monich.cost_price.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {
    String message() default "{com.monich.cost_price.validation.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
