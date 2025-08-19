# BudgetTracker - Personal Finance Management Platform

BudgetTracker is a comprehensive personal finance management platform that helps you track subscriptions, manage bills, monitor income, and gain insights into your spending habits across all financial categories.

## 🎯 Project Overview

A full-stack web application built with Spring Boot and React that provides complete financial visibility through subscription management, expense tracking, and analytics dashboard. The platform offers Google OAuth2 authentication and a modern, responsive UI for managing your entire financial ecosystem.

## ✨ Key Features

### 🔐 Authentication & Security
- **Google OAuth2 Integration** - Secure login with Google accounts
- **User Profiles** - Personalized currency settings and preferences
- **CSRF Protection** - Enterprise-grade security measures

### 💳 Subscription Management
- **Complete CRUD Operations** - Add, edit, delete, and view subscriptions
- **Smart Categorization** - Organize subscriptions by type (Entertainment, Utilities, etc.)
- **Status Tracking** - Active/inactive subscription monitoring
- **Filtering & Search** - Find subscriptions quickly
- **Billing Period Support** - Monthly, yearly, and custom billing cycles

### 📊 Dashboard & Analytics (Planned)
- **Financial Overview** - Total expenses, income, and savings at-a-glance
- **Visual Charts** - Spending trends, category breakdowns, income vs expenses
- **Smart Insights** - AI-powered recommendations and spending alerts
- **Goal Tracking** - Budget goals and progress monitoring
- **Real-time Updates** - Live financial health scoring

### 💰 Additional Features (In Development)
- **Income Tracking** - Manual entry with categorization
- **Bills Management** - Non-subscription recurring bills
- **Payment History** - Track bill payment records
- **Monthly Analytics** - Comprehensive spending analysis

## 🏗️ Technology Stack

### Backend
- **Spring Boot 3.5.3** - Modern Java framework
- **Spring Security** - OAuth2 authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **PostgreSQL** - Production database
- **Flyway** - Database migration management
- **OpenAPI/Swagger** - API documentation
- **Maven** - Build automation

### Frontend
- **React 19** - Modern UI framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Fast build tool and dev server
- **Tailwind CSS** - Utility-first styling
- **Modern UI Components** - Custom component library

### Development & Testing
- **H2 Database** - In-memory testing database
- **JUnit 5** - Comprehensive testing framework
- **Spring Boot Test** - Integration testing
- **Lombok** - Reduced boilerplate code

## 🎨 Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React)                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Components: Auth, Subscriptions, Dashboard, etc │   │
│  │ State Management: React Hooks + Context        │   │
│  │ Styling: Tailwind CSS + Custom Components      │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                           │ HTTP/REST API
                           ▼
┌─────────────────────────────────────────────────────────┐
│                Backend (Spring Boot)                    │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Controllers: REST endpoints + SPA routing       │   │
│  │ Services: Business logic + validation           │   │
│  │ Security: OAuth2 + CSRF protection             │   │
│  │ JPA Repositories: Database abstraction         │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                           │ JPA/Hibernate
                           ▼
┌─────────────────────────────────────────────────────────┐
│                PostgreSQL Database                      │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Tables: Users, Subscriptions, Categories, etc  │   │
│  │ Migrations: Flyway version control             │   │
│  │ Indexing: Optimized query performance         │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 20+
- PostgreSQL 12+
- Maven 3.6+

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/marcel/budget-tracker.git
   cd budget-tracker
   ```

2. **Database Setup**
   ```bash
   # Create PostgreSQL database
   createdb budget_tracker
   
   # Configure environment variables
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. **Backend Setup**
   ```bash
   # Install dependencies and build
   mvn clean install
   
   # Run database migrations
   mvn flyway:migrate
   
   # Start Spring Boot application
   mvn spring-boot:run
   ```

4. **Frontend Setup**
   ```bash
   cd ui
   npm install
   npm run dev
   ```

5. **Access the Application**
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

## 📱 Screenshots & Demo

### Subscription Management
- Clean, intuitive interface for managing all subscriptions
- Category-based organization with visual icons
- Quick status toggling and bulk operations

### Dashboard Analytics (Coming Soon)
- Real-time financial health scoring
- Interactive charts showing spending trends
- Smart insights and recommendations
- Goal tracking and progress monitoring

## 🧪 Testing

```bash
# Run all backend tests
mvn test

# Run frontend tests
cd ui && npm test

# Integration tests
mvn test -Dspring.profiles.active=test
```

## 📚 API Documentation

Interactive API documentation is available at `/swagger-ui.html` when running the application. Key endpoints include:

- `GET /api/subscriptions` - List user subscriptions
- `POST /api/subscriptions` - Create new subscription  
- `PUT /api/subscriptions/{id}` - Update subscription
- `DELETE /api/subscriptions/{id}` - Delete subscription
- `GET /api/categories` - List available categories

## 🛠️ Development Status

**Current Version:** 0.0.1-SNAPSHOT  
**Status:** Beta - Core subscription management features complete

### ✅ Completed Features
- Google OAuth2 authentication
- Full subscription CRUD operations
- Category management system
- Responsive UI with dark theme
- Comprehensive test coverage

### 🚧 In Development
- Income tracking system
- Bills management
- Payment history tracking

### 🎯 Planned Features
- Advanced analytics dashboard
- Budget goal setting
- Export capabilities
- Mobile applications
- Multi-currency support

## 🤝 Contributing

This project follows standard Git workflow practices:
- Feature branches for new development
- Comprehensive testing requirements
- Code review process

## 📄 License

MIT License - see LICENSE file for details

---

**Built by Marcel** | [Project Repository](https://github.com/marcel/budget-tracker)
