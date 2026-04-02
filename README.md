# JavaMailSender (OTP Email via Gmail)

This small Spring Boot app sends an OTP email (HTML template) to a user's Gmail address.

What I changed

- Fixed `MailController` to autowire `MailService` and generate a secure 6-digit OTP.
- Fixed `MailService` to correctly build a MIME HTML message using Thymeleaf.

Gmail notes (important)

- For Gmail SMTP you must either use an App Password (recommended) or configure OAuth2. Regular account password usually won't work if 2FA is enabled (which is recommended).
- To use an app password:
  1. Enable 2-Step Verification on your Google account.

2.  Create an App Password (Mail) in your Google account security settings and copy the 16-character password.
3.  Set the values in `src/main/resources/application.properties` or via environment variables:

    spring.mail.username=your-email@gmail.com
    spring.mail.password=your-16-character-app-password

Alternatively you can set environment variables `SPRING_MAIL_USERNAME` and `SPRING_MAIL_PASSWORD`.

How to run

1. Update `src/main/resources/application.properties` with your Gmail address and app password.
2. Build and run with Maven:

```bash
mvn -DskipTests spring-boot:run
```

Test the endpoint

- POST /api/v1/register?email=recipient@example.com

Notes / next steps

- Consider moving credentials to environment variables or a secret manager for production.
- If you plan to use a custom 'from' address, set `helper.setFrom(...)` in the service.

If you want, I can attempt to run the build here and fix any compilation errors. Tell me to proceed.
