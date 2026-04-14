package com.demo.otp.provider;

import com.demo.otp.config.Msg91Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@ConditionalOnProperty(name = "otp.service.provider", havingValue = "msg91")
public class Msg91SmsProvider implements SmsProvider {

    @Autowired
    private Msg91Config msg91Config;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendSms(String phone, String otp, String customMessage) {
        String url = "https://api.msg91.com/api/v5/flow/";

        // Standard Indian numbers from my system come as +91... 
        // MSG91 prefers numbers without the + for international.
        String cleanPhone = phone.replace("+", "");

        // Prepare JSON payload for MSG91 Flow API
        Map<String, Object> payload = new HashMap<>();
        payload.put("template_id", msg91Config.getTemplateId());
        payload.put("sender", msg91Config.getSenderId());
        payload.put("short_url", "0");

        List<Map<String, String>> recipients = new ArrayList<>();
        Map<String, String> recipient = new HashMap<>();
        recipient.put("mobiles", cleanPhone);
        
        // Dynamic variables in your MSG91 template (e.g., ##otp##)
        recipient.put("otp", otp); 
        recipients.add(recipient);
        
        payload.put("recipients", recipients);

        HttpHeaders headers = new HttpHeaders();
        headers.set("authkey", msg91Config.getAuthKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("MSG91 Error: " + e.getMessage());
        }
    }
}
