package com.servicehub.auth.dto;

import com.servicehub.common.validation.annotation.ValidPhoneNumber;

public record ForgotPasswordRequest(
        @ValidPhoneNumber String phoneNumber
) {}
