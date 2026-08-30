package com.andabazaar.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MobileNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMobileNumber {
    String message() default "Invalid mobile number";
    String region() default "IN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
