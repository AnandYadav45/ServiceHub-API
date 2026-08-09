package com.servicehub.common.validation.annotation;

import com.servicehub.common.validation.validator.EmailAddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailAddressValidator.class)
public @interface ValidEmailAddress {
    String message() default "must be a valid Email Address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
