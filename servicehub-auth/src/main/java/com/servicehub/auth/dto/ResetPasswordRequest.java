package com.servicehub.auth.dto;

import com.servicehub.common.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @ValidPhoneNumber String phoneNumber,
        @NotBlank @Size(min = 6, max = 6) String otp,
        @NotBlank @Size(min = 8, max = 100) String newPassword
) {}
