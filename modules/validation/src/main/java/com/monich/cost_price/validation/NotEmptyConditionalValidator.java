package com.monich.cost_price.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyConditionalValidator implements ConstraintValidator<NotEmptyConditional, Object> {

    private String field;
    private String value;
    private String conditionalField;

    @Override
    public void initialize(NotEmptyConditional constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.value = constraintAnnotation.value();
        this.conditionalField = constraintAnnotation.conditionalField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        Object cv = beanWrapper.getPropertyValue(conditionalField);
        if (cv == null) {
            return true;
        }
        boolean success = true;
        if (cv.toString().equals(value)) {
            Object v = beanWrapper.getPropertyValue(field);
            success =  v != null && StringUtils.isNotBlank(v.toString());
        }
        if (success){
            return true;
        }else{
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("NotEmpty")
                    .addPropertyNode(field)
                    .addConstraintViolation();
            return false;
        }
    }
}
