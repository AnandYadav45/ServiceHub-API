package com.servicehub.common.validation.validator;

import com.servicehub.common.util.RegexPatternsUtil;
import com.servicehub.common.validation.annotation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && RegexPatternsUtil.INDIAN_MOBILE.matcher(value.trim()).matches();
    }
}
