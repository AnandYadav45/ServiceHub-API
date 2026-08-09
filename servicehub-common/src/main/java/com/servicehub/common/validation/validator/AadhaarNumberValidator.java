package com.servicehub.common.validation.validator;

import com.servicehub.common.util.RegexPatternsUtil;
import com.servicehub.common.validation.annotation.ValidAadhaarNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AadhaarNumberValidator implements ConstraintValidator<ValidAadhaarNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && RegexPatternsUtil.AADHAAR.matcher(value.trim()).matches();
    }
}
