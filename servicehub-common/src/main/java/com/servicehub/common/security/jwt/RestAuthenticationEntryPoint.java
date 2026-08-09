package com.servicehub.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.common.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // RestAuthenticationEntryPoint.commence
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("SECURITY-401-003", "Authentication required")));
//        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error("Authentication required")));
    }
}
