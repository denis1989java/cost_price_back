package com.monich.cost_price.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyConditionalValidator.class)
public @interface NotEmptyConditional {
    String message() default "{com.monich.cost_price.validation.NotEmpty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String conditionalField();

    String value();
}
