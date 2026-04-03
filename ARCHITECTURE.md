# JavaMailSender - Layered Architecture

## Project Structure

```
src/main/java/com/example/javamailsender/
├── controller/
│   └── MailController.java              (REST Endpoints)
├── service/
│   ├── OtpService.java                  (Service Interface)
│   └── impl/
│       └── OtpServiceImpl.java           (Service Implementation)
├── repository/
│   ├── OtpRepository.java               (Repository Interface)
│   └── OtpRepositoryImpl.java            (In-memory Implementation)
├── model/
│   ├── entity/
│   │   └── OtpEntity.java               (Data Model)
│   └── dto/
│       ├── OtpRequest.java              (Request DTO)
│       └── OtpResponse.java             (Response DTO)
├── exception/
│   ├── OtpSenderException.java          (Base Exception)
│   ├── EmailSendingException.java       (Email Error)
│   ├── InvalidEmailException.java       (Validation Error)
│   └── GlobalExceptionHandler.java      (Centralized Error Handling)
├── config/
│   ├── SwaggerConfig.java               (API Documentation)
│   └── MailConfig.java                  (Mail Configuration)
└── JavaMailSenderApplication.java       (Entry Point)

src/main/resources/
├── application.properties                (SMTP Config)
└── templates/
    └── otp-template.html                (Email Template)
```

## Layers Explained

### 1. **Controller Layer** (`controller/`)
- REST endpoints for client requests
- Handles HTTP method mapping (GET/POST)
- No business logic, only calls service
- Swagger/OpenAPI documentation
- Endpoint: `/api/v1/otp/request` (Generate & send OTP)
- Endpoint: `/api/v1/otp/verify` (Verify OTP)

### 2. **Service Layer** (`service/`)
- Business logic (OTP generation, validation, expiry)
- **Interface**: `OtpService` - contracts
- **Implementation**: `OtpServiceImpl` - logic
- Calls repository for data persistence
- Throws custom exceptions

### 3. **Repository Layer** (`repository/`)
- Data access abstraction
- **Interface**: `OtpRepository` - CRUD operations
- **Implementation**: `OtpRepositoryImpl` - in-memory storage (can be replaced with JPA/Database)
- Methods: `save()`, `findByEmail()`, `deleteByEmail()`

### 4. **Model/Entity Layer** (`model/`)
- **Entity**: `OtpEntity` - database/storage model
  - id, email, otpCode, createdAt, expiresAt, verified
- **DTOs** (Data Transfer Objects):
  - `OtpRequest` - input from client
  - `OtpResponse` - response to client

### 5. **Exception Layer** (`exception/`)
- Custom exceptions for specific errors
- `OtpSenderException` - base exception
- `EmailSendingException` - mail failures
- `InvalidEmailException` - validation errors
- `GlobalExceptionHandler` - centralized error handling (converts exceptions → HTTP responses)

### 6. **Config Layer** (`config/`)
- Application configuration
- `SwaggerConfig` - API documentation setup
- `MailConfig` - mail service configuration
- Properties read from `application.properties`

## Data Flow

1. **Client → Controller**
   - `GET /api/v1/otp/request?email=user@example.com`
   - Controller receives OtpRequest

2. **Controller → Service**
   - Service validates email
   - Generates random 6-digit OTP
   - Calls Repository to save OTP

3. **Service → Repository**
   - Repository stores OTP entity in memory
   - Returns saved entity

4. **Service → Email**
   - Processes Thymeleaf template
   - Sends HTML email via JavaMailSender

5. **Service → Controller → Client**
   - Returns OtpResponse (success/error)
   - HTTP 200 or 500 with JSON response

## Exception Handling Flow

```
Any Exception
    ↓
GlobalExceptionHandler catches it
    ↓
Converts to JSON error response with:
  - success: false
  - error: error type
  - message: human-readable message
  - timestamp: when it occurred
    ↓
Returns appropriate HTTP status (400, 500, etc.)
```

## Future Enhancements

- [ ] Replace `OtpRepositoryImpl` with JPA (`@Entity`, `@Repository` with Spring Data)
- [ ] Add database (PostgreSQL/MySQL) for OTP storage
- [ ] Add Lombok `@Entity` annotations to reduce boilerplate
- [ ] Add MapStruct for DTO ↔ Entity mapping
- [ ] Add input validation annotations (`@Email`, `@NotEmpty`, etc.)
- [ ] Add caching (Redis) for OTP lookups
- [ ] Add rate limiting on `/request` endpoint
- [ ] Add SMS sending as alternative to email
- [ ] Add JWT token generation after OTP verification

## API Endpoints

### 1. Request OTP
```
GET/POST /api/v1/otp/request?email=user@example.com

Response (200 OK):
{
  "success": true,
  "message": "OTP sent successfully to your email",
  "email": "user@example.com"
}

Response (400 Bad Request):
{
  "success": false,
  "error": "Invalid Email",
  "message": "Invalid email format: user@invalid",
  "timestamp": "2026-04-03T12:00:00"
}
```

### 2. Verify OTP
```
POST /api/v1/otp/verify?email=user@example.com&otpCode=123456

Response (200 OK):
{
  "success": true,
  "message": "OTP verified successfully"
}

Response (200 OK - but failed):
{
  "success": false,
  "message": "Invalid OTP code"
}
```

## Testing the API

With Swagger UI:
1. Start app: `mvn spring-boot:run`
2. Open: `http://localhost:8080/swagger-ui.html`
3. Test endpoints directly in UI

With curl:
```bash
# Request OTP
curl -X GET "http://localhost:8080/api/v1/otp/request?email=test@gmail.com"

# Verify OTP (get code from logs)
curl -X POST "http://localhost:8080/api/v1/otp/verify?email=test@gmail.com&otpCode=527643"
```

---

**Architecture ready for production!** ✨
