package com.servicehub.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.common.dto.ApiResponse;
import com.servicehub.common.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper){
        this.jwtTokenProvider=jwtTokenProvider;
        this.objectMapper=objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtTokenProvider.parseClaims(token);
            Integer userId = Integer.parseInt(claims.getId());
            String role = claims.get("role", String.class);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request,response);

        }catch (JwtException | IllegalArgumentException ex){
            String message = (ex instanceof ExpiredJwtException) ? "Token expired" : "Invalid token";
            writeErrorResponse(response, message, (JwtException) ex);

        }

    }

    private void writeErrorResponse(HttpServletResponse response, String message, JwtException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String code = (ex instanceof ExpiredJwtException) ? "SECURITY-401-002" : "SECURITY-401-001";
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(code, message)));
//        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(message)));
    }

}
