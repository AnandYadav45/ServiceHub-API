package com.servicehub.auth.service;

import com.servicehub.auth.dto.*;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    AuthResponse verifyOtp(OtpVerifyRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(RefreshTokenRequest request);
    UserDto getCurrentUser(Long userId);
    String issue(Long userId);
    Long validateAndGetUserId(String token);
    void revoke(String token);
}
