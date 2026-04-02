package com.example.javamailsender.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MailController {
    @PostMapping("/register")
    public String registerUser(@RequestParam String email) {
        String generatedOtp = "327006"; // Logic to generate random 6-digit code
        try {
            otpEmailService.sendOtpEmail(email, generatedOtp);
            return "OTP Sent Successfully";
        } catch (MessagingException e) {
            return "Error sending email: " + e.getMessage();
        }
    }
}
