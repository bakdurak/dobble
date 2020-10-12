package com.example.dobble.release.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidatorAdapter implements IEmailValidator {
    private EmailValidator validator;

    public EmailValidatorAdapter(EmailValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(String email) {
        return validator.isValid(email);
    }
}
