package com.monich.cost_price.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final org.apache.commons.validator.routines.EmailValidator validator
            = org.apache.commons.validator.routines.EmailValidator.getInstance();

    @Override
    public void initialize(Email constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.isValid(value);
    }
}
