package com.servicehub.auth.service;

public interface OtpService {
    String generateAndStore(String phoneNumber);
    boolean verify(String phoneNumber, String otp);

}
