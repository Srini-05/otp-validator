package com.demo.otp.provider;

public interface SmsProvider {
    void sendSms(String phone, String otp, String customMessage);
}
