package com.example.javamailsender.Service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.naming.Context;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendOtpEmail(String toEmail, String otpCode) throws MessagingException {
        // 1. Prepare Thymeleaf Context
        Context context = new Context();
        context.setVariable("otpCode", otpCode);

        // 2. Process HTML template
        String process = templateEngine.process("otp-template", context);

        // 3. Prepare Mail
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setSubject("Verify Your Habit Tracker Account");
        helper.setText(process, true); // 'true' enables HTML content
        helper.setTo(toEmail);

        mailSender.send(mimeMessage);
    }
}
