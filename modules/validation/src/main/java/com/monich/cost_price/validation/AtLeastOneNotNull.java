package com.monich.cost_price.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated object must have at least one of fields not null
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AtLeastOneNotNullConstraintValidator.class)
public @interface AtLeastOneNotNull {
    String message() default "{com.monich.cost_price.validation.OneNotNull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines list of field names which are validated
     */
    String[] fields();

}
