# Anda Bazaar Service

Egg Market Price Rate Application — A Spring Boot REST API for tracking and managing egg prices across Indian markets.

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.1.0 |
| Language | Java 21 |
| Database | MySQL 8+ |
| ORM | Hibernate 7 / Spring Data JPA |
| Connection Pool | HikariCP |
| Security | Spring Security + JWT (jjwt 0.13.0) |
| API Client | Spring Cloud OpenFeign |
| Mapping | MapStruct 1.6.3 |
| API Docs | Springdoc OpenAPI 3.1.0 (Swagger UI) |
| Payments | Razorpay Java SDK |
| Build | Maven |
| Tests | JUnit 5 + Mockito (80%+ coverage) |

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      Client (Browser / Mobile)          │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP
                           ▼
┌─────────────────────────────────────────────────────────┐
│                    Spring Security                      │
│              JWT Authentication Filter                  │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                     Controllers                         │
│  Auth │ User │ Admin │ EggPrice │ Location │ Payment    │
│  Dashboard │ Notification │ ExpectedPrice │ EggRateApi  │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   Service Layer                         │
│            Business Logic + Validation                  │
└──────────┬────────────────────────────┬─────────────────┘
           │                            │
           ▼                            ▼
┌──────────────────────┐  ┌───────────────────────────────┐
│   Repository Layer   │  │     External Integrations     │
│   Spring Data JPA    │  │  ┌─────────────────────────┐  │
│                      │  │  │ RapidAPI (Egg Rates)    │  │
│   ┌──────────────┐   │  │  │ Razorpay (Payments)    │  │
│   │   MySQL DB   │   │  │  │ Feign HTTP Clients     │  │
│   │  (HikariCP)  │   │  │  └─────────────────────────┘  │
│   └──────────────┘   │  └───────────────────────────────┘
└──────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────┐
│                     Schedulers                          │
│  EggRateFetchScheduler (hourly) │ PriceUpdateScheduler  │
│  NotificationScheduler                                  │
└─────────────────────────────────────────────────────────┘
```

## Entity Relationship Diagram

```
┌──────────────┐
│  BaseEntity  │ (MappedSuperclass)
│──────────────│
│ id: Long     │
│ name: String │
└──────┬───────┘
       │ extends
       ├──────────────┬──────────────┐
       ▼              ▼              ▼
┌────────────┐ ┌────────────┐ ┌────────────┐
│  Country   │ │   State    │ │    City    │
│────────────│ │────────────│ │────────────│
│ (id, name) │ │ (id, name) │ │ (id, name) │
└──────┬─────┘ └──────┬─────┘ └──────┬─────┘
       │              │              │
       └──────┬───────┴──────────────┘
              ▼
       ┌─────────────────┐
       │    Location      │
       │─────────────────│
       │ id               │
       │ city ──► City    │
       │ state ──► State  │
       │ country ──► Cntry│
       │ latitude         │
       │ longitude        │
       │ rapidEnabled     │
       │ createdDate      │
       │ updatedDate      │
       └─────────────────┘

┌─────────────────┐     ┌─────────────────┐
│      User       │     │     Market      │
│─────────────────│     │─────────────────│
│ id              │     │ id              │
│ firstName       │     │ name            │
│ lastName        │     │ city ──► City   │
│ email (unique)  │     │ address         │
│ phone (unique)  │     │ pincode         │
│ password (BCrypt│     │ contactPerson   │
│ role (ADMIN/USER│     │ contactNumber   │
│ status          │     │ active          │
│ profileImage    │     │ createdAt       │
│ preferredLang   │     │ updatedAt       │
│ preferredCity   │     └────────┬────────┘
│ notifEnabled    │              │
│ createdAt       │              │ market
│ updatedAt       │              ▼
└────────┬────────┘     ┌─────────────────┐
         │              │    EggPrice     │
         │ user         │─────────────────│
         ▼              │ id              │
┌─────────────────┐     │ market ──► Mkt  │
│   Notification  │     │ priceDate       │
│─────────────────│     │ pricePerEgg     │
│ id              │     │ pricePerTray    │
│ user ──► User   │     │ previousPrice   │
│ type            │     │ priceChangeType │
│ title           │     │ priceChangeAmt  │
│ message         │     │ remarks         │
│ isRead          │     │ active          │
│ sent            │     │ createdAt       │
│ createdAt       │     │ updatedAt       │
└─────────────────┘     └─────────────────┘

┌─────────────────┐     ┌─────────────────┐
│ ExpectedPrice   │     │  DailyEggRate   │
│─────────────────│     │─────────────────│
│ id              │     │ id              │
│ market ──► Mkt  │     │ city ──► City   │
│ expectedDate    │     │ state ──► State │
│ expectedPrice   │     │ rateDate        │
│ reason          │     │ rate            │
│ active          │     │ previousRate    │
│ createdAt       │     │ trend           │
│ updatedAt       │     │ change          │
└─────────────────┘     │ source          │
                        │ createdDate     │
┌─────────────────┐     │ updatedDate     │
│    Payment      │     └─────────────────┘
│─────────────────│
│ id              │
│ user ──► User   │
│ amount          │
│ currency        │
│ transactionId   │
│ razorpayOrderId │
│ razorpayPayId   │
│ razorpaySignature│
│ status          │
│ failureReason   │
│ paidAt          │
│ createdAt       │
│ updatedAt       │
└─────────────────┘
```

## API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |
| GET | `/api/auth/me` | Get current user profile |

### Users (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Create user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users` | Get all users (paginated) |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/{id}/profile` | Get user profile |
| PATCH | `/api/users/{id}/status` | Change user status |

### Admin (ADMIN role)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/dashboard` | Admin dashboard metrics |
| POST | `/api/admin` | Create admin user |
| GET | `/api/admin/users` | List all users (paginated) |
| GET | `/api/admin/users/{id}` | Get user details |
| PATCH | `/api/admin/users/{id}/status` | Change user status |
| DELETE | `/api/admin/users/{id}` | Delete user |

### Egg Prices (Authenticated, Write=ADMIN)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/egg-prices` | Create price (ADMIN) |
| PUT | `/api/egg-prices/{id}` | Update price (ADMIN) |
| DELETE | `/api/egg-prices/{id}` | Soft delete price (ADMIN) |
| GET | `/api/egg-prices/{id}` | Get price by ID |
| GET | `/api/egg-prices/market/{marketId}?date=` | Get market price by date |
| GET | `/api/egg-prices/today` | Today's prices (paginated) |
| GET | `/api/egg-prices/yesterday` | Yesterday's prices (paginated) |
| GET | `/api/egg-prices/history/{marketId}` | Price history (paginated) |
| GET | `/api/egg-prices/user/{userId}` | User prices (paginated) |
| GET | `/api/egg-prices/user/{userId}/history/{marketId}` | User price history (paginated) |

### External Egg Rates (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/egg-rates-external?city=&state=` | All rates from RapidAPI |
| GET | `/api/egg-rates-external/today?city=&state=` | Today's rate |
| GET | `/api/egg-rates-external/yesterday?city=&state=` | Yesterday's rate |

### Locations (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/locations` | Create location (auto-creates country/state/city) |
| GET | `/api/locations/{id}` | Get location by ID |
| GET | `/api/locations` | Get all locations (paginated) |
| PUT | `/api/locations/{id}` | Update location |
| DELETE | `/api/locations/{id}` | Delete location |
| GET | `/api/locations/rapid-enabled` | Get RapidAPI-enabled locations |

### Expected Prices (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/expected-prices` | Create expected price |
| PUT | `/api/expected-prices/{id}` | Update expected price |
| GET | `/api/expected-prices/{id}` | Get by ID |
| GET | `/api/expected-prices` | Get active (paginated) |
| DELETE | `/api/expected-prices/{id}` | Soft delete |

### Payments (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments/user/{userId}` | Create payment |
| POST | `/api/payments/user/{userId}/verification` | Verify payment |
| GET | `/api/payments/{id}` | Get payment by ID |
| GET | `/api/payments/user/{userId}` | Get user payments (paginated) |

### Notifications (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/notifications` | Create notification |
| GET | `/api/notifications/user/{userId}` | Get user notifications (paginated) |
| GET | `/api/notifications/user/{userId}/unread` | Get unread (paginated) |
| GET | `/api/notifications/user/{userId}/count` | Get unread count |
| PUT | `/api/notifications/{id}/read` | Mark as read |
| PUT | `/api/notifications/user/{userId}/read-status` | Mark all as read |
| DELETE | `/api/notifications/{id}` | Delete notification |

### Dashboard (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/dashboard/admin` | Admin dashboard |
| GET | `/api/dashboard/user/{userId}` | User dashboard |

## Request/Response Flow

```
Client Request
     │
     ▼
┌─────────────────┐     ┌──────────────────┐
│ JwtAuthFilter   │────▶│ CustomUserDetails │
│ Extract Bearer  │     │ Load from DB     │
│ Validate Token  │     └──────────────────┘
└────────┬────────┘
         │ Authenticated
         ▼
┌─────────────────┐     ┌──────────────────┐
│  SecurityConfig │────▶│ Role Check       │
│  URL Matching   │     │ ADMIN / USER     │
└────────┬────────┘     └──────────────────┘
         │ Authorized
         ▼
┌─────────────────┐
│   Controller    │
│ Validate Input  │
│ (@Valid)        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐     ┌──────────────────┐
│    Service      │────▶│  MapStruct       │
│ Business Logic  │     │  Entity ↔ DTO    │
└────────┬────────┘     └──────────────────┘
         │
         ▼
┌─────────────────┐     ┌──────────────────┐
│  Repository     │────▶│   MySQL (Hikari) │
│  Spring Data    │     │   JPA / Hibernate│
└─────────────────┘     └──────────────────┘
         │
         ▼
┌─────────────────┐
│ PagedResponse   │
│ {content, page, │
│  size, total,   │
│  totalPages,    │
│  last}          │
└─────────────────┘
```

## Scheduler Flow

```
┌────────────────────────────────────────────────────────┐
│              EggRateFetchScheduler (hourly)             │
│                                                        │
│  for each State:                                       │
│    for each City:                                      │
│      ┌──────────────────────────┐                      │
│      │ Try: city + state        │                      │
│      │ RapidAPI getTodayRate()  │──── Success ──► Save │
│      └────────────┬─────────────┘                      │
│                   │ Fail                               │
│                   ▼                                    │
│      ┌──────────────────────────┐                      │
│      │ Fallback: state + state  │                      │
│      │ (cached per state)       │──── Success ──► Save │
│      └────────────┬─────────────┘                      │
│                   │ Fail                               │
│                   ▼                                    │
│              Skip (logged)                             │
└────────────────────────────────────────────────────────┘
```

## Getting Started

### Prerequisites
- Java 21
- MySQL 8+
- Maven 3.9+

### Setup

```bash
# Clone
git clone https://github.com/abidimran/anda-bazaar-service.git
cd anda-bazaar-service

# Create database
mysql -u root -e "CREATE DATABASE andabazaar;"

# Update src/main/resources/application.yml with your DB credentials

# Build
./mvnw clean compile

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Swagger UI
```
http://localhost:8080/v1/swagger-ui/index.html
```

### Authentication
1. Register: `POST /v1/api/auth/register`
2. Login: `POST /v1/api/auth/login` → returns JWT token
3. Use header: `Authorization: Bearer <token>`

## Project Structure

```
src/main/java/com/andabazaar/
├── config/          # Security, CORS, JWT, OpenAPI, Feign configs
├── controller/      # REST controllers (9 controllers)
├── dto/             # Request/Response DTOs
├── enums/           # RoleType, UserStatus, PaymentStatus, NotificationType
├── exception/       # Custom exceptions + GlobalExceptionHandler
├── feign/           # Feign clients (RapidAPI egg rates)
├── mapper/          # MapStruct mappers (User, EggPrice, Location, Payment, ExpectedPrice)
├── payment/         # Razorpay integration
├── repository/      # Spring Data JPA repositories
│   └── entity/      # JPA entities (14 entities)
├── scheduler/       # Scheduled tasks (egg rate fetch, price update, notifications)
├── security/        # JWT service, auth filter, user details
├── service/         # Service interfaces
├── serviceimpl/     # Service implementations
└── util/            # DateTimeUtil, PriceCalculationUtil, ValidationUtil
```

## Test Coverage

| Package | Coverage |
|---|---|
| util/ | 100% |
| enums/ | 100% |
| config/ | 88% |
| serviceimpl/ | 98% |
| security/ | 90% |
| exception/ | 100% |
| scheduler/ | 100% |
| **Overall** | **81%** |
