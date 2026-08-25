package com.example.sponsordonkey.services.EmailSender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private EmailService emailService;

    @GetMapping("/emails-to-send")
    public List<EmailDTO> getEmailsToSend(
            @RequestHeader("X-API-KEY") String key) {
                
        if (key == null || !key.equals(apiKey)) {
            throw new RuntimeException("Unauthorized");
        }
        return emailService.getEmailsToSend();
    }
}
