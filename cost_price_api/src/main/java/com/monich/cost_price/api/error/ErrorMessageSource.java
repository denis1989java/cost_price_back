package com.monich.cost_price.api.error;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ErrorMessageSource {
    private final ResourceBundleMessageSource errors;

    public ErrorMessageSource() {
        errors = new ResourceBundleMessageSource();
        errors.setBasename("errors");
    }

    public String getMessage(ACBMException exception, Locale locale) {
        return errors.getMessage(exception.getErrorCode().name(), exception.getArgs(), locale);
    }
}
