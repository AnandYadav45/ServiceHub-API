package com.servicehub.common.validation.validator;

import com.servicehub.common.util.RegexPatternsUtil;
import com.servicehub.common.validation.annotation.ValidEmailAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailAddressValidator implements ConstraintValidator<ValidEmailAddress, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && RegexPatternsUtil.EMAIL.matcher(value.trim()).matches();
    }
}
