package com.demo.otp.dto;

public class OtpRequest {
    private String phone;
    private String otp;

    // Manual Getters
    public String getPhone() {
        return phone;
    }

    public String getOtp() {
        return otp;
    }

    // Manual Setters
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
