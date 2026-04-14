# Real-Time OTP Validation System (Spring Boot + Twilio)

A professional, secure monolithic OTP (One-Time Password) system built with Spring Boot. This application generates 6-digit verification codes, sends them to physical phones via the Twilio SMS API, and validates them in real-time.

## 🚀 Features

- **Live SMS Integration**: Uses Twilio SDK to deliver real codes to mobile devices.
- **Secure Architecture**: OTP codes are never returned in API responses; they are only sent via SMS.
- **Smart Formatting**: Automatically handles phone number formatting (e.g., prepending `+91` for Indian mobile numbers).
- **Clean Code**: Implements Lombok to eliminate boilerplate and uses a dedicated Configuration class for security keys.
- **Real-Time Validation**: In-memory storage using `ConcurrentHashMap` for high-performance validation.

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.3
- **Tools**: Maven, Project Lombok (Annotation Processing)
- **Integrations**: Twilio Java SDK v9.14.0
- **Frontend**: HTML5, Vanilla JavaScript, CSS3 (Modern Glassmorphism UI)
- **Architecture**: Monolithic REST API

## 📁 Project Structure

```text
src/main/java/com/demo/otp/
├── config/
│   └── TwilioConfig.java     # Configuration bean for Twilio credentials
├── controller/
│   └── OtpController.java   # REST Endpoints for Generate/Validate
├── dto/
│   ├── OtpRequest.java      # Data object for incoming phone/otp
│   └── OtpResponse.java     # Data object for API responses
└── service/
    └── OtpService.java      # Core logic (Twilio init, SMS sending, mapping)
```

## ⚙️ Configuration & Security Keys

Before running, you must configure your Twilio credentials in `src/main/resources/application.properties`:

```properties
# Twilio Configuration
twilio.account-sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth-token=YOUR_TWILIO_AUTH_TOKEN
twilio.phone-number=YOUR_TWILIO_PHONE_OR_SERVICE_SID
```

> [!IMPORTANT]
> **Safety Tip**: Never commit your real Auth Token to a public repository. For Trial accounts, ensure your "To" number is verified in the Twilio Console.

## 🏃 How to Run

1. **Verify Java**: Ensure you have Java 17 installed.
2. **Setup Maven**: If Maven isn't in your PATH, use the local bundle included in this project.
3. **Start the Application**:
   ```bash
   mvn spring-boot:run
   ```
4. **Access the UI**: Open [http://localhost:8080](http://localhost:8080) in your browser.

## 📡 API Endpoints

### 1. Generate OTP
- **URL**: `/api/otp/generate`
- **Method**: `POST`
- **Body**: `{ "phone": "9360161838" }`
- **Note**: If standard 10-digit number is provided, it auto-prefixes `+91`.

### 2. Validate OTP
- **URL**: `/api/otp/validate`
- **Method**: `POST`
- **Body**: `{ "phone": "9360161838", "otp": "123456" }`

## 🛡️ Security Implementation

- **Exclusion of OTP in Response**: The `OtpResponse` object intentionally excludes the generated OTP value. This prevents attackers from "sniffing" the code via the browser's Network tab.
- **E.164 Formatting**: The system cleans and formats phone numbers to meet international telecommunication standards required by SMS providers.
- **Stateless Storage**: Uses thread-safe concurrent maps to prevent data corruption during simultaneous user requests.

---

## 🌐 Deployment Logic

### GitHub Hosting
1. Create a new repository on GitHub.
2. Initialize git in your project:
   ```bash
   git init
   git add .
   git commit -m "Initialize OTP Project"
   ```
3. Push to GitHub. (The `.gitignore` will skip the heavy `target` folder).

### Backend Hosting (Render / Railway)
Since this is a Java application, GitHub Pages cannot run the backend.
1. Sign up for **[Railway.app](https://railway.app)** or **Render.com**.
2. Connect your GitHub repository.
3. **Environment Variables**: In the dashboard of your host, add these "Variables":
   - `TWILIO_ACCOUNT_SID`: (Your SID)
   - `TWILIO_AUTH_TOKEN`: (Your Auth Token)
   - `TWILIO_PHONE_NUMBER`: (Your Service SID or Twilio Number)

The application will automatically pick these up from the cloud!

---
*Created for Demo purposes - Secure, Modular, and Scalable.*
