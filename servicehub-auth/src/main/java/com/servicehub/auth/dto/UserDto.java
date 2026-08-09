package com.servicehub.auth.dto;

import com.servicehub.common.enums.UserRole;

public record UserDto(
        Long id, String fullName, String contact, String email, UserRole role
) {}
