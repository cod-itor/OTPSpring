# OTP Mail Sender - Complete Authentication API

## User Registration & Login Flow

```
1. REGISTER (name, email, password)
   ↓
2. OTP sent to email automatically
   ↓
3. User receives OTP in email
   ↓
4. VERIFY OTP (email, otpCode)
   ↓
5. User marked as VERIFIED
   ↓
6. LOGIN (email, password) - Only works if verified=true
   ↓
7. Login successful ✓
```

---

## API Endpoints

### Authentication Endpoints (`/api/v1/auth`)

#### 1. Register User

```
POST /api/v1/auth/register

Request Body:
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}

Response (200 OK):
{
  "success": true,
  "message": "Registration successful! OTP sent to your email. Please verify to complete registration.",
  "userId": 1,
  "email": "john@example.com",
  "name": "John Doe"
}

Response (409 CONFLICT - user already exists):
{
  "success": false,
  "error": "User Already Exists",
  "message": "User with email john@example.com already exists",
  "timestamp": "2026-04-03T12:00:00"
}
```

#### 2. Verify OTP

```
POST /api/v1/auth/verify-otp?email=john@example.com&otpCode=527643

Response (200 OK - success):
{
  "success": true,
  "message": "OTP verified successfully",
  "email": "john@example.com"
}

Response (200 OK - invalid code):
{
  "success": false,
  "message": "Invalid OTP code",
  "email": "john@example.com"
}

Response (200 OK - expired):
{
  "success": false,
  "message": "OTP has expired",
  "email": "john@example.com"
}
```

#### 3. Resend OTP

```
GET /api/v1/auth/resend-otp?email=john@example.com
or
POST /api/v1/auth/resend-otp?email=john@example.com

Response (200 OK):
{
  "success": true,
  "message": "OTP sent successfully to your email",
  "email": "john@example.com"
}
```

#### 4. Login User

```
POST /api/v1/auth/login

Request Body:
{
  "email": "john@example.com",
  "password": "password123"
}

Response (200 OK - success):
{
  "success": true,
  "message": "Login successful!",
  "userId": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "isVerified": true
}

Response (403 FORBIDDEN - not verified):
{
  "success": false,
  "error": "User Not Verified",
  "message": "User not verified. Please verify your email with OTP first.",
  "timestamp": "2026-04-03T12:00:00"
}

Response (401 UNAUTHORIZED - wrong password):
{
  "success": false,
  "error": "Invalid Credentials",
  "message": "Invalid email or password",
  "timestamp": "2026-04-03T12:00:00"
}
```

---

### OTP Endpoints (`/api/v1/otp`)

#### 1. Request OTP (manual)

```
GET /api/v1/otp/request?email=john@example.com
or
POST /api/v1/otp/request?email=john@example.com

Response (200 OK):
{
  "success": true,
  "message": "OTP sent successfully to your email",
  "email": "john@example.com"
}
```

#### 2. Verify OTP

```
POST /api/v1/otp/verify?email=john@example.com&otpCode=527643

Response (200 OK):
{
  "success": true,
  "message": "OTP verified successfully",
  "email": "john@example.com"
}
```

---

## Complete Registration Flow Example

### Step 1: Register User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Registration successful! OTP sent to your email...",
  "userId": 1,
  "email": "john@example.com",
  "name": "John Doe"
}
```

📧 **Email sent to user with OTP code** (check console logs for the code in dev)

---

### Step 2: Check Console Logs for OTP

Console output:

```
INFO ... Generated OTP: 527643 for email: john@example.com
INFO ... Email sent successfully to: john@example.com
```

---

### Step 3: Verify OTP

```bash
curl -X POST "http://localhost:8080/api/v1/auth/verify-otp?email=john@example.com&otpCode=527643"
```

**Response:**

```json
{
  "success": true,
  "message": "OTP verified successfully",
  "email": "john@example.com"
}
```

✅ **User is now verified and can login**

---

### Step 4: Login (only works if verified)

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Response:**

```json
{
  "success": true,
  "message": "Login successful!",
  "userId": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "isVerified": true
}
```

✅ **User logged in successfully**

---

## What Happens If User Tries to Login Before Verification?

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Response (403 FORBIDDEN):**

```json
{
  "success": false,
  "error": "User Not Verified",
  "message": "User not verified. Please verify your email with OTP first.",
  "timestamp": "2026-04-03T12:00:00"
}
```

❌ **Login blocked until user verifies email with OTP**

---

## User Entity Structure

```java
UserEntity {
  id: Long,              // Unique user ID
  name: String,          // User's name
  email: String,         // User's email (unique)
  password: String,      // User's password (TODO: hash in production)
  isVerified: boolean,   // TRUE after OTP verification, FALSE initially
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

**Key Field: `isVerified`**

- **FALSE** → User can register but cannot login
- **TRUE** → OTP verified, user can login

---

## Testing with Swagger UI

1. Start app: `mvn spring-boot:run`
2. Open: `http://localhost:8080/swagger-ui.html`
3. Test all endpoints in the UI

---

## Database/Storage

Currently using **in-memory storage** (HashMap):

- User data stored in `UserRepositoryImpl`
- OTP data stored in `OtpRepositoryImpl`

**Data will be lost when app restarts!**

For production, replace with:

- PostgreSQL / MySQL with JPA `@Entity`
- Spring Data `@Repository`
- Database migrations with Flyway/Liquibase

---

## TODO for Production

- [ ] Hash passwords with BCrypt
- [ ] Add JWT token generation after login
- [ ] Add database (JPA/Hibernate)
- [ ] Add email confirmation link (alternative to OTP)
- [ ] Add password reset flow
- [ ] Add rate limiting on /register and /login
- [ ] Add user profile endpoint
- [ ] Add logout endpoint
- [ ] Add refresh token mechanism
- [ ] Add 2FA (SMS + Email OTP)

---

## Error Codes

| Status | Error                 | Meaning                    |
| ------ | --------------------- | -------------------------- |
| 200    | Success               | Operation successful       |
| 400    | Invalid Email         | Email format is wrong      |
| 401    | Invalid Credentials   | Wrong email/password       |
| 403    | User Not Verified     | User not verified with OTP |
| 409    | User Already Exists   | Email already registered   |
| 500    | Internal Server Error | Unexpected server error    |
