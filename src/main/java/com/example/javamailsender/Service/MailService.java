package com.example.javamailsender.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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

        // 2. Process HTML template (otp-template.html in templates/)
        String body = templateEngine.process("otp-template", context);

        // 3. Prepare Mail message (HTML)
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setSubject("Verify Your Habit Tracker Account");
        helper.setText(body, true); // true = HTML
        helper.setTo(toEmail);

        mailSender.send(mimeMessage);
    }
}
