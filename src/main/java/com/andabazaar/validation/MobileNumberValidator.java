package com.andabazaar.validation;

import com.andabazaar.util.PhoneNumberUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileNumberValidator implements ConstraintValidator<ValidMobileNumber, String> {

    private String region;

    @Override
    public void initialize(ValidMobileNumber annotation) {
        this.region = annotation.region();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // let @NotBlank handle nulls
        return PhoneNumberUtil.isValid(value, region);
    }
}
