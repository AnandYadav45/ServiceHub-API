package com.servicehub.common.validation.validator;

import com.servicehub.common.util.RegexPatternsUtil;
import com.servicehub.common.validation.annotation.ValidIndianPinCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PinCodeValidator implements ConstraintValidator<ValidIndianPinCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && RegexPatternsUtil.PIN_CODE.matcher(value.trim()).matches();
    }
}
