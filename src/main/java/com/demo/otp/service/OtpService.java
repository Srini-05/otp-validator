package com.demo.otp.service;

import com.demo.otp.config.TwilioConfig;
import com.demo.otp.dto.OtpRequest;
import com.demo.otp.dto.OtpResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void init() {
        if (twilioConfig.getAccountSid() != null && !twilioConfig.getAccountSid().isEmpty() && !"YOUR_TWILIO_SID".equals(twilioConfig.getAccountSid())) {
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        }
    }

    // 5 minutes expiration in milliseconds
    private static final long EXPIRE_MS = 5 * 60 * 1000;

    // Store mapped Phone -> OtpDetail
    private final ConcurrentHashMap<String, OtpDetail> otpStorage = new ConcurrentHashMap<>();

    private static class OtpDetail {
        private final String otp;
        private final long expiryTime;

        public OtpDetail(String otp) {
            this.otp = otp;
            this.expiryTime = System.currentTimeMillis() + EXPIRE_MS;
        }

        public String getOtp() { return otp; }
        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return phone;
        String trimmed = phone.trim();
        if (trimmed.startsWith("+")) {
            return trimmed;
        }
        // Automatically prepend +91 for 10-digit Indian numbers
        if (trimmed.length() == 10 && trimmed.matches("\\d+")) {
            return "+91" + trimmed;
        }
        // Fallback: just prepend +
        return "+" + trimmed.replaceFirst("^0+", "");
    }

    // Generate a 6-digit OTP
    public OtpResponse generateOtp(OtpRequest request) {
        String rawPhone = request.getPhone();
        if (rawPhone == null || rawPhone.isEmpty()) {
            return new OtpResponse(false, "Phone number is required");
        }
        
        String formattedPhone = formatPhoneNumber(rawPhone);

        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);
        
        System.out.println("Generated OTP for " + formattedPhone + " is " + otp);
        otpStorage.put(formattedPhone, new OtpDetail(otp));
        
        // Output through Twilio Live SMS
        try {
            if (twilioConfig.getAccountSid() != null && !"YOUR_TWILIO_SID".equals(twilioConfig.getAccountSid())) {
                String sender = twilioConfig.getPhoneNumber();
                if (sender.startsWith("MG")) {
                    Message.creator(
                            new PhoneNumber(formattedPhone),
                            sender,
                            "Ahoy! Your verification code is " + otp + ". For security, do not share this code with anyone-Srinivasan.")
                        .create();
                } else {
                    Message.creator(
                            new PhoneNumber(formattedPhone),
                            new PhoneNumber(sender),
                            "Ahoy! Your verification code is " + otp + ". For security, do not share this code with anyone-Srinivasan")
                        .create();
                }
                System.out.println("Live SMS sent successfully via Twilio to " + formattedPhone);
            } else {
                System.out.println("Twilio not configured. Operating in test mode.");
            }
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            return new OtpResponse(false, "OTP generated but failed to send SMS: " + e.getMessage());
        }
        
        return new OtpResponse(true, "OTP generated and sent successfully!");
    }

    // Validate the OTP
    public OtpResponse validateOtp(OtpRequest request) {
        String rawPhone = request.getPhone();
        if (rawPhone == null || request.getOtp() == null) {
            return new OtpResponse(false, "Phone and OTP are required");
        }
        
        String formattedPhone = formatPhoneNumber(rawPhone);

        OtpDetail detail = otpStorage.get(formattedPhone);
        
        if (detail == null) {
            return new OtpResponse(false, "No OTP found for this number \u274C");
        }

        if (detail.isExpired()) {
            otpStorage.remove(formattedPhone);
            return new OtpResponse(false, "OTP has expired. Please request a new one \u274C");
        }

        if (detail.getOtp().equals(request.getOtp())) {
            // OTP matches. Remove it so it cannot be used again.
            otpStorage.remove(formattedPhone);
            return new OtpResponse(true, "OTP Validated Successfully! \u2705");
        }
        
        return new OtpResponse(false, "Invalid OTP code \u274C");
    }
}
