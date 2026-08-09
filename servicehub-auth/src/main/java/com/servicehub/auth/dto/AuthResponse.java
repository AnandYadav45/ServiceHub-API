package com.servicehub.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private String tokenType;
    private long expiresInMs;
    private UserDto user;
}
