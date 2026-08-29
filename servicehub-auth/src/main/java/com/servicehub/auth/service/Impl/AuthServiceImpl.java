package com.servicehub.auth.service.Impl;

import com.servicehub.auth.dto.*;
import com.servicehub.auth.entity.RefreshToken;
import com.servicehub.auth.entity.User;
import com.servicehub.auth.mapper.UserMapper;
import com.servicehub.auth.repository.RefreshTokenRepository;
import com.servicehub.auth.repository.UserRepository;
import com.servicehub.auth.service.AuthService;
import com.servicehub.auth.service.OtpService;
import com.servicehub.common.enums.UserRole;
import com.servicehub.common.exceptions.BusinessValidationException;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.common.exceptions.UnauthorizedException;
import com.servicehub.common.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final OtpService otpService;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final long refreshExpirationMs;

    public AuthServiceImpl(
            UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            UserMapper userMapper, @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs)
    {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.userMapper = userMapper;
        this.refreshExpirationMs = refreshExpirationMs;
    }



    @Override
    @Transactional
    public void register(RegisterRequest request) {
        User user = new User();
        user.setName(request.fullName());
        user.setActive(false);
        user.setContact(request.contact());
        user.setEmail(request.email());
        user.setRole(UserRole.CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.contact())
                .orElseThrow(() -> new UnauthorizedException(HttpStatus.UNAUTHORIZED, "AUTH-401-001", "Invalid phone number or password"));
        if(!user.isActive()){
            throw new BusinessValidationException(HttpStatus.UNAUTHORIZED, "AUTH-403-001", "Account not verified — please verify your phone number first");
        }
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new UnauthorizedException ( HttpStatus.UNAUTHORIZED, "AUTH-401-001", "Invalid phone number or password");
        }
        return buildAuthResponse(user);

    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByPhoneNumber(request.phoneNumber())
                .ifPresent(user -> {
                    String otp = otpService.generateAndStore(request.phoneNumber());
                    // Here Call the Otp Send service
                });

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if(!otpService.verify(request.phoneNumber(), request.otp())){
            throw new BusinessValidationException(HttpStatus.UNAUTHORIZED, "AUTH-400-001", "Invalid or expired OTP");
        }
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AUTH-404-001", "User not found"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        if (!otpService.verify(request.phoneNumber(), request.otp())) {
            throw new BusinessValidationException(HttpStatus.UNAUTHORIZED, "AUTH-400-001", "Invalid or expired OTP");
        }

        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AUTH-404-001", "User not found"));
        user.setActive(true);
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException(HttpStatus.UNAUTHORIZED, "AUTH-401-002", "Invalid refresh token"));

        if (storedToken.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, "AUTH-401-003", "Refresh token expired");
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AUTH-404-001", "User not found"));

        // Rotation: the old refresh token is revoked and a completely new pair issued. If a refresh
        // token is ever stolen, this limits how long it stays useful — and if the legitimate user's
        // next refresh call finds their token already revoked, that's a signal something's wrong,
        // worth alerting on later even though that detection isn't built here yet.
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        return buildAuthResponse(user);
    }

    @Override
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByTokenAndRevokedFalse(request.refreshToken())
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AUTH-404-001", "User not found"));
        return userMapper.toUser(user);
    }


    @Override
    @Transactional
    public String issue(Long userId) {
        byte[] bytes = new byte[48];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(LocalDateTime.now().plus(refreshExpirationMs, ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public Long validateAndGetUserId(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new UnauthorizedException(HttpStatus.UNAUTHORIZED, "AUTH-401-002", "Invalid refresh token"));
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, "AUTH-401-003", "Refresh token expired");
        }
        return refreshToken.getUserId();
    }

    @Override
    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .ifPresent(rt -> { rt.setRevoked(true); refreshTokenRepository.save(rt); });
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
        String refreshTokenValue = generateAndStoreRefreshToken(user.getId());
        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresInMs(86400000)
                .user(userMapper.toUser(user))
                .build();
    }

    private String generateAndStoreRefreshToken(Long userId) {
        byte[] randomBytes = new byte[64];
        new java.security.SecureRandom().nextBytes(randomBytes);
        String tokenValue = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(java.time.LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }
}
