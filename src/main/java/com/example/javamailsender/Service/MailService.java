package com.example.javamailsender.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendOtpEmail(String toEmail, String otpCode) throws MessagingException {
        logger.info("Starting OTP email send to: {}", toEmail);
        logger.info("Generated OTP: {}", otpCode);
        logger.info("From address: {}", fromAddress);

        try {
            // 1. Prepare Thymeleaf Context
            Context context = new Context();
            context.setVariable("otpCode", otpCode);
            logger.debug("Thymeleaf context prepared");

            // 2. Process HTML template (otp-template.html in templates/)
            String body = templateEngine.process("otp-template", context);
            logger.debug("HTML template processed successfully");

            // 3. Prepare Mail message (HTML)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setSubject("Verify Your Habit Tracker Account");
            helper.setText(body, true); // true = HTML
            helper.setTo(toEmail);
            logger.debug("MIME message prepared with from={}, to={}", fromAddress, toEmail);

            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to: {}", toEmail, e);
            throw e;
        }
    }
}
