package com.servicehub.common.filter;

import com.servicehub.common.security.encryption.AesGcmEncryptor;
import com.servicehub.common.security.encryption.DecryptedRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EncryptionFilter extends OncePerRequestFilter {
    private final AesGcmEncryptor encryptor;

    public EncryptionFilter(AesGcmEncryptor encryptor) {
        this.encryptor = encryptor;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest requestToUse = request;
        if(request.getContentLength() > 0){
            String rowBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            String decryptedJson = encryptor.decrypt(rowBody);
            requestToUse = new DecryptedRequestWrapper(request, decryptedJson);
        }

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestToUse,responseWrapper);

        byte[] responseBody = responseWrapper.getContentAsByteArray();
        if(responseBody.length > 0){
            String plainJson = new String(responseBody, StandardCharsets.UTF_8);
            String encytpedJson = encryptor.encrypt(plainJson);
            responseWrapper.resetBuffer();
            responseWrapper.getWriter().write(encytpedJson);
        }
        responseWrapper.copyBodyToResponse();

    }
}
