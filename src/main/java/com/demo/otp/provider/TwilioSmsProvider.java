package com.demo.otp.provider;

import com.demo.otp.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "otp.service.provider", havingValue = "twilio", matchIfMissing = true)
public class TwilioSmsProvider implements SmsProvider {

    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void init() {
        if (twilioConfig.getAccountSid() != null && !twilioConfig.getAccountSid().isEmpty() && !"YOUR_SID_HERE".equals(twilioConfig.getAccountSid())) {
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        }
    }

    @Override
    public void sendSms(String phone, String otp, String customMessage) {
        String sender = twilioConfig.getPhoneNumber();
        String fullMessage = customMessage.replace("[OTP]", otp);
        
        if (sender.startsWith("MG")) {
            Message.creator(new PhoneNumber(phone), sender, fullMessage).create();
        } else {
            Message.creator(new PhoneNumber(phone), new PhoneNumber(sender), fullMessage).create();
        }
    }
}
