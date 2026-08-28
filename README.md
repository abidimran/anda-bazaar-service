# Anda-Bazaar

A Spring Boot REST API backend for an egg market price tracking and management application targeting the Indian market.

---

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- MySQL
- Spring Security + JWT (stateless)
- Razorpay (payment gateway)
- Firebase (push notifications)
- Lombok
- Spring Actuator
- Maven

---

## Project Structure

```
src/main/java/com/andabazaar/
├── config/          # Security, CORS, JWT, Razorpay, Jackson configs
├── constants/       # API, Security, Subscription constants
├── controller/      # REST controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── enums/           # Enums (RoleType, PaymentStatus, etc.)
├── exception/       # Global exception handler + custom exceptions
├── mapper/          # Entity ↔ DTO mappers
├── notification/    # Firebase push notification services
├── payment/         # Razorpay payment & webhook services
├── repository/      # Spring Data JPA repositories
├── scheduler/       # Scheduled background jobs
├── security/        # JWT filter, UserDetails, JwtService
├── service/         # Service interfaces
├── serviceimpl/     # Service implementations
└── util/            # DateTimeUtil, PriceCalculationUtil, ValidationUtil
```

---

## Features

### Authentication
- Register, Login (JWT-based)
- Forgot Password / Reset Password
- Role-based access control (ADMIN / USER)

### Egg Prices
- CRUD operations (Admin only for write)
- Bulk price upload
- Price history tracking
- Price analytics & trends
- Price alerts for users
- Expected price management
- User price reports

### Markets & Location
- Market management
- State & City hierarchy
- Favorite markets per user
- Market comparison

### Subscriptions & Payments
- Subscription plans management
- Razorpay payment integration with webhook support
- Coupon system with usage tracking

### Notifications
- Firebase push notifications
- Notification preferences per user
- Scheduled notification dispatch

### Admin
- Admin dashboard with stats
- User management
- Audit logs
- Reports

### Support
- Support ticket system with replies

### News
- Market news feed

---

## Scheduled Jobs

| Scheduler | Purpose |
|---|---|
| `PriceUpdateScheduler` | Automated price updates |
| `NotificationScheduler` | Push notification dispatch |
| `CouponExpiryScheduler` | Coupon expiry management |
| `SubscriptionExpiryScheduler` | Subscription expiry handling |
| `SubscriptionReminderScheduler` | Subscription renewal reminders |

---

## API Endpoints (Summary)

| Module | Base Path |
|---|---|
| Auth | `/api/auth` |
| Users | `/api/users` |
| Egg Prices | `/api/egg-prices` |
| Markets | `/api/markets` |
| States | `/api/states` |
| Cities | `/api/cities` |
| Subscriptions | `/api/subscriptions` |
| Payments | `/api/payments` |
| Notifications | `/api/notifications` |
| Price Alerts | `/api/price-alerts` |
| Price Analytics | `/api/price-analytics` |
| Price History | `/api/price-history` |
| Coupons | `/api/coupons` |
| News | `/api/news` |
| Support | `/api/support` |
| Admin | `/api/admin` |
| Dashboard | `/api/dashboard` |
| Reports | `/api/reports` |
| App Settings | `/api/app-settings` |
| Audit Logs | `/api/audit-logs` |

Public endpoints (no auth required): `/api/auth/register`, `/api/auth/login`, `/actuator/health`

---

## Getting Started

### Prerequisites
- Java 21
- MySQL
- Maven

### Setup

1. Clone the repository
2. Create a MySQL database:
   ```sql
   CREATE DATABASE anda_bazaar;
   ```
3. Configure `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/anda_bazaar
   spring.datasource.username=<your_db_username>
   spring.datasource.password=<your_db_password>

   jwt.secret=<your_jwt_secret>

   razorpay.key.id=<your_razorpay_key_id>
   razorpay.key.secret=<your_razorpay_key_secret>
   razorpay.webhook.secret=<your_razorpay_webhook_secret>
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

Server starts on `http://localhost:8080`

---

## Security

- Stateless JWT authentication
- BCrypt password encoding
- Role-based endpoint authorization (ADMIN / USER)
- CSRF disabled (stateless API)

---

## Health Check

```
GET /actuator/health
```
