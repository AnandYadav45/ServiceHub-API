package com.servicehub.auth.dto;

import com.servicehub.common.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpVerifyRequest(
        @ValidPhoneNumber String phoneNumber,
        @NotBlank @Size(min = 6, max = 6) String otp
) {
}
