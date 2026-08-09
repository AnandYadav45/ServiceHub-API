package com.servicehub.common.validation.annotation;

import com.servicehub.common.validation.validator.AadhaarNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AadhaarNumberValidator.class)
public @interface ValidAadhaarNumber {
    String message() default "Must be a valid 12 digit Aadhaar number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
