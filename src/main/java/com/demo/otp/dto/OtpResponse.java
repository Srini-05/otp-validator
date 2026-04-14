package com.demo.otp.dto;

public class OtpResponse {
    private boolean success;
    private String message;

    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Manual Getter for success
    public boolean isSuccess() {
        return success;
    }

    // Manual Getter for message
    public String getMessage() {
        return message;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
