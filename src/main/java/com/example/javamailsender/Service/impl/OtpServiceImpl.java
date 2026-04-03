package com.example.javamailsender.service.impl;

import com.example.javamailsender.exception.EmailSendingException;
import com.example.javamailsender.exception.InvalidEmailException;
import com.example.javamailsender.model.dto.OtpRequest;
import com.example.javamailsender.model.dto.OtpResponse;
import com.example.javamailsender.model.entity.OtpEntity;
import com.example.javamailsender.model.entity.UserEntity;
import com.example.javamailsender.repository.OtpRepository;
import com.example.javamailsender.repository.UserRepository;
import com.example.javamailsender.service.OtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * OTP Service Implementation
 */
@Service
public class OtpServiceImpl implements OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public OtpResponse requestOtp(OtpRequest request) {
        logger.info("OTP request for email: {}", request.getEmail());

        // Validate email
        if (!isValidEmail(request.getEmail())) {
            throw new InvalidEmailException("Invalid email format: " + request.getEmail());
        }

        try {
            // Generate OTP
            String otpCode = generateOtp();
            logger.info("Generated OTP: {} for email: {}", otpCode, request.getEmail());

            // Create OTP entity
            OtpEntity otp = new OtpEntity();
            otp.setEmail(request.getEmail());
            otp.setOtpCode(otpCode);
            otp.setCreatedAt(LocalDateTime.now());
            otp.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
            otp.setVerified(false);

            // Save to repository
            otpRepository.save(otp);

            // Send email
            sendOtpEmail(request.getEmail(), otpCode);

            logger.info("OTP sent successfully to: {}", request.getEmail());
            return new OtpResponse(true, "OTP sent successfully to your email", request.getEmail());

        } catch (Exception e) {
            logger.error("Failed to request OTP for {}: {}", request.getEmail(), e.getMessage(), e);
            throw new EmailSendingException("Failed to send OTP email", e);
        }
    }

    @Override
    public OtpResponse verifyOtp(String email, String otpCode) {
        logger.info("Verifying OTP for email: {}", email);

        Optional<OtpEntity> otpOptional = otpRepository.findByEmail(email);

        if (otpOptional.isEmpty()) {
            return new OtpResponse(false, "No OTP found for this email");
        }

        OtpEntity otp = otpOptional.get();

        // Check expiry
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            otpRepository.deleteByEmail(email);
            return new OtpResponse(false, "OTP has expired");
        }

        // Verify code
        if (otp.getOtpCode().equals(otpCode)) {
            otp.setVerified(true);
            otpRepository.save(otp);
            
            // Mark user as verified
            Optional<UserEntity> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                user.setVerified(true);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
                logger.info("User marked as verified: {}", email);
            }
            
            logger.info("OTP verified successfully for: {}", email);
            return new OtpResponse(true, "OTP verified successfully");
        }

        return new OtpResponse(false, "Invalid OTP code");
    }

    /**
     * Generate a random 6-digit OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Send OTP email with HTML template
     */
    private void sendOtpEmail(String toEmail, String otpCode) throws MessagingException {
        logger.debug("Sending OTP email to: {}", toEmail);

        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("otpCode", otpCode);

        // Process HTML template
        String body = templateEngine.process("otp-template", context);

        // Create MIME message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromAddress);
        helper.setSubject("Verify Your Account - OTP");
        helper.setText(body, true);
        helper.setTo(toEmail);

        // Send
        mailSender.send(mimeMessage);
        logger.debug("Email sent successfully to: {}", toEmail);
    }
}
