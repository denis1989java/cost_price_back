package com.monich.cost_price.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullConstraintValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private String[] fields;

    /**
     * Standard initializer
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    /***
     * Processes validation check
     * @param object object to check
     * @param context  context in which the constraint is evaluated
     * @return <code>true</code> if at least one of the fields in the object is not null and <code>false</code> otherwise
     */
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        for (String field : fields) {
            if (beanWrapper.getPropertyValue(field) != null) {
                return true;
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.join(", ", fields))
                .addConstraintViolation();
        return false;
    }
}
