package com.example.javamailsender.controller;

import com.example.javamailsender.Service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    @Autowired
    private MailService otpEmailService;

    private final SecureRandom random = new SecureRandom();

    @PostMapping("/register")
    public String registerUser(@RequestParam String email) {
        // generate a secure random 6-digit OTP
        int otp = 100000 + random.nextInt(900000);
        String generatedOtp = String.valueOf(otp);
        try {
            otpEmailService.sendOtpEmail(email, generatedOtp);
            return "OTP Sent Successfully";
        } catch (MessagingException e) {
            return "Error sending email: " + e.getMessage();
        }
    }
}
