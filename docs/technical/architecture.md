# BudgetTracker Architecture

## 🏗️ System Architecture Overview

BudgetTracker follows a modern full-stack architecture with clear separation between frontend, backend, and data layers.

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Layer                       │
│  ┌─────────────────────────────────────────────────┐   │
│  │        React 19 + TypeScript SPA               │   │
│  │  - Modern UI with React 19 concurrent features │   │
│  │  - TailwindCSS + CSS custom properties        │   │
│  │  - TanStack Query for server state            │   │
│  │  - Vite build system with HMR                 │   │
│  │  - OAuth2 client integration                  │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                           │ REST API (JSON)
                           │ JWT Bearer token auth
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   Backend Layer                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │           Spring Boot 3.5.3 API                │   │
│  │  - REST Controllers with OpenAPI docs          │   │
│  │  - Spring Security with OAuth2 + JWT           │   │
│  │  - Service layer with business logic           │   │
│  │  - JPA repositories with custom queries        │   │
│  │  - JWT authentication with auto-refresh        │   │
│  │  - Static resource serving for SPA             │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                           │ JPA/Hibernate
                           │ Connection pooling
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   Data Layer                            │
│  ┌─────────────────────────────────────────────────┐   │
│  │            PostgreSQL Database                  │   │
│  │  - Relational data model                       │   │
│  │  - Flyway database migrations                  │   │
│  │  - Optimized indexes and constraints           │   │
│  │  - ACID compliance for financial data          │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## 🔐 Security Architecture

### Authentication Flow
```
User Browser ──➤ Google OAuth2 ──➤ Spring Security ──➤ JWT Token
     │                                      │
     └──────── Bearer Token ─────────────────┘
     │
     └──────── API Requests (Authenticated) ──➤ Controllers
```

### Security Layers
1. **OAuth2 Authentication** - Google SSO integration
2. **JWT Authentication** - Stateless token-based auth
3. **Token Management** - Auto-refresh, persistent keys
4. **Input Validation** - Server-side data validation
5. **SQL Injection Prevention** - JPA parameterized queries

## 📊 Data Architecture

### Entity Relationship Model
```
┌─────────────┐    ┌──────────────────┐    ┌─────────────────┐
│    User     │    │   Subscription   │    │    Category     │
├─────────────┤    ├──────────────────┤    ├─────────────────┤
│ id (PK)     │◄──┤│ id (PK)          │    │ id (PK)         │
│ email       │   ││ user_id (FK)     │──┤►│ name            │
│ name        │   ││ category_id (FK) │   │ │ color           │
│ currency    │   ││ name             │   │ │ icon            │
│ created_at  │   ││ price            │   │ └─────────────────┘
└─────────────┘   ││ billing_period   │   │
                  ││ status           │   │
                  ││ next_bill_date   │   │
                  ││ created_at       │   │
                  └──────────────────┘   │
                                         │
┌─────────────────┐    ┌─────────────────┐
│     Income      │    │      Bills      │
├─────────────────┤    ├─────────────────┤
│ id (PK)         │    │ id (PK)         │
│ user_id (FK)    │──┤ │ user_id (FK)    │
│ category_id (FK)│   │ │ name            │
│ amount          │   │ │ amount          │
│ description     │   │ │ due_date        │
│ date            │   │ │ is_recurring    │
│ created_at      │   │ │ status          │
└─────────────────┘   │ │ created_at      │
                      │ └─────────────────┘
                      └──┤
                         │
                    ┌─────────────────┐
                    │  Payment_Log    │
                    ├─────────────────┤
                    │ id (PK)         │
                    │ bill_id (FK)    │
                    │ amount          │
                    │ payment_date    │
                    │ status          │
                    │ created_at      │
                    └─────────────────┘
```

## 🚀 Deployment Architecture

### Development Environment
```
Local Development:
- Frontend: Vite dev server (localhost:5173)
- Backend: Spring Boot (localhost:8080) 
- Database: PostgreSQL (localhost:5432)
- Proxy: Vite proxies API calls to Spring Boot
```

### Production Deployment
```
Single JAR Deployment:
┌─────────────────────────────────────┐
│          Spring Boot JAR            │
│  ┌─────────────────────────────┐   │
│  │     Static Resources        │   │
│  │  (React build artifacts)    │   │
│  └─────────────────────────────┘   │
│  ┌─────────────────────────────┐   │
│  │      REST API               │   │
│  │  (Spring Boot controllers)  │   │
│  └─────────────────────────────┘   │
└─────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      PostgreSQL Database            │
└─────────────────────────────────────┘
```

## 📦 Module Structure

### Backend Modules
```
src/main/java/com/example/budgettracker/
├── config/              # Spring configuration
│   ├── SecurityConfig   # OAuth2 + JWT setup
│   ├── JwtKeyConfig     # JWT key persistence
│   └── WebConfig        # CORS and web settings
├── controller/          # REST API endpoints
│   ├── AuthController   # Authentication
│   ├── SubscriptionController
│   └── ApiController    # Generic API utils
├── service/             # Business logic layer
│   ├── UserService      
│   ├── JwtService       # JWT token operations
│   ├── SubscriptionService
│   └── CategoryService
├── repository/          # Data access layer
│   ├── UserRepository
│   ├── SubscriptionRepository
│   └── CategoryRepository
├── model/               # JPA entities
│   ├── User
│   ├── Subscription
│   └── Category
└── dto/                 # Data transfer objects
    ├── SubscriptionDto
    └── CategoryDto
```

### Frontend Modules
```
ui/src/
├── components/          # React components
│   ├── auth/           # Authentication UI
│   ├── forms/          # Form components  
│   ├── ui/             # Base UI components
│   └── layout/         # Layout components
├── hooks/              # Custom React hooks
├── services/           # API integration
├── types/              # TypeScript definitions
├── utils/              # Utility functions
└── styles/             # CSS and styling
```

## 🔄 API Design

### RESTful Endpoints
```
Authentication:
POST /login/oauth2/google     # OAuth2 login
GET  /api/auth/status         # Auth status check
POST /api/auth/refresh        # JWT token refresh

Subscriptions:
GET    /api/subscriptions              # List user subscriptions
POST   /api/subscriptions              # Create subscription
GET    /api/subscriptions/{id}         # Get subscription
PUT    /api/subscriptions/{id}         # Update subscription  
DELETE /api/subscriptions/{id}         # Delete subscription

Categories:
GET /api/categories                    # List categories
```

### Response Format
```json
{
  "success": true,
  "data": { /* payload */ },
  "error": null,
  "timestamp": "2025-08-19T10:30:00Z"
}
```

## 🧪 Testing Architecture

### Backend Testing
- **Unit Tests** - JUnit 5 + Mockito for service layer
- **Integration Tests** - Spring Boot Test with H2 database
- **API Tests** - MockMvc for controller testing
- **Security Tests** - OAuth2 and CSRF validation

### Frontend Testing (Planned)
- **Unit Tests** - Jest + React Testing Library
- **Component Tests** - Storybook visual testing
- **E2E Tests** - Playwright for user flows
- **API Integration** - MSW for API mocking

## 📈 Scalability Considerations

### Current Architecture
- **Single JAR deployment** for simplicity
- **PostgreSQL** for ACID compliance
- **Connection pooling** for database efficiency
- **Stateless REST API** for horizontal scaling

### Future Enhancements
- **Microservices** - Split by domain (auth, subscriptions, analytics)
- **Caching** - Redis for session and data caching  
- **Message Queue** - For async processing
- **CDN** - Static asset optimization
- **Database scaling** - Read replicas for analytics

---

**Version:** 0.0.1-SNAPSHOT | **Last Updated:** August 2025