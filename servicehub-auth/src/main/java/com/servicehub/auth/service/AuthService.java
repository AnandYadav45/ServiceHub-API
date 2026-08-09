package com.servicehub.auth.service;

import com.servicehub.auth.dto.*;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    AuthResponse verifyOtp(OtpVerifyRequest request);
}
