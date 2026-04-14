package com.demo.otp.dto;

import lombok.Data;

@Data
public class OtpRequest {
    private String phone;
    private String otp;
}
