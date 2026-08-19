package com.servicehub.auth.service.Impl;

import com.servicehub.auth.dto.*;
import com.servicehub.auth.entity.User;
import com.servicehub.auth.mapper.UserMapper;
import com.servicehub.auth.repository.UserRepository;
import com.servicehub.auth.service.AuthService;
import com.servicehub.auth.service.OtpService;
import com.servicehub.common.enums.UserRole;
import com.servicehub.common.exceptions.BusinessValidationException;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.common.exceptions.UnauthorizedException;
import com.servicehub.common.security.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final OtpService otpService;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            UserMapper userMapper)
    {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.userMapper = userMapper;
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

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInMs(86400000)
                .user(userMapper.toUser(user))
                .build();
    }
}
