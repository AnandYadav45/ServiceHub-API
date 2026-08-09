package com.servicehub.common.validation.validator;

import com.servicehub.common.util.RegexPatternsUtil;
import com.servicehub.common.validation.annotation.ValidPanNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PanNumberValidator implements ConstraintValidator<ValidPanNumber,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && RegexPatternsUtil.PAN.matcher(value.trim()).matches();
    }
}
