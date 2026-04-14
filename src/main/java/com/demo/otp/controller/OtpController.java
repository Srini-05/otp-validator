package com.demo.otp.controller;

import com.demo.otp.dto.OtpRequest;
import com.demo.otp.dto.OtpResponse;
import com.demo.otp.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<OtpResponse> generateOtp(@RequestBody OtpRequest request) {
        OtpResponse response = otpService.generateOtp(request);
        if (!response.isSuccess() && response.getMessage().contains("required")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<OtpResponse> validateOtp(@RequestBody OtpRequest request) {
        OtpResponse response = otpService.validateOtp(request);
        if (!response.isSuccess() && response.getMessage().contains("required")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
