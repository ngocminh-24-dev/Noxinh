# 🎀 Noxinh - Fashion & Accessories E-Commerce Backend

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![SQL Server](https://img.shields.io/badge/Microsoft_SQL_Server-2022-CC292B?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)](https://www.microsoft.com/sql-server)
[![Redis](https://img.shields.io/badge/Redis-Cache_%26_OTP-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Firebase](https://img.shields.io/badge/Firebase-Cloud_Storage-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Apache POI](https://img.shields.io/badge/Apache_POI-Excel_Reports-D22128?style=for-the-badge&logo=apache&logoColor=white)](https://poi.apache.org/)
[![OpenAPI/Swagger](https://img.shields.io/badge/OpenAPI-Swagger_3.0-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)

A modern, high-performance e-commerce backend service built for **Noxinh** (online boutique specializing in fashion accessories, hair bows, and lifestyle items). Built with **Java 17** and **Spring Boot 3.3.4**, this service features catalog and combo bundle management, shopping cart operations, Vietnamese administrative address mapping, Redis-backed OTP password resets, Firebase media storage, and Excel reporting with Apache POI.

---

## 📑 Table of Contents

- [Overview & Architecture](#-overview--architecture)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Environment Configuration](#-environment-configuration)
- [Getting Started](#-getting-started)
  - [Run Locally (Maven)](#run-locally-maven)
  - [Run with Docker Compose](#run-with-docker-compose)
- [API Documentation & Swagger](#-api-documentation--swagger)
- [Core API Reference](#-core-api-reference)
- [Contributing & License](#-contributing--license)

---

## 🌟 Overview & Architecture

Noxinh follows a layered service-oriented architecture with Redis caching, SQL Server transactional persistence, and cloud storage:

```
                      ┌──────────────────────────────────────┐
                      │    Client (Web / Mobile Storefront)  │
                      └──────────────────┬───────────────────┘
                                         │  HTTPS / REST / JSON
                                         ▼
                      ┌──────────────────────────────────────┐
                      │       Spring Security & JJWT         │
                      │  (Stateless Authentication & RBAC)   │
                      └──────────────────┬───────────────────┘
                                         │
                 ┌───────────────────────┴───────────────────────┐
                 ▼                                               ▼
    ┌─────────────────────────┐                     ┌─────────────────────────┐
    │ Storefront & Customers  │                     │ Administration & Ops    │
    │ - Catalog & Combos      │                     │ - Order Fulfillment     │
    │ - Shopping Cart         │                     │ - Banner & Sliders      │
    │ - Vietnamese Addresses  │                     │ - Excel Sales Reporting │
    │ - User Profile & Orders │                     │ - Account Moderation    │
    └────────────┬────────────┘                     └────────────┬────────────┘
                 │                                               │
                 └───────────────────────┬───────────────────────┘
                                         │
                                         ▼
                      ┌──────────────────────────────────────┐
                      │        Service & Business Layer      │
                      │ ┌──────────────────────────────────┐ │
                      │ │  Validation, OTP & Mail Services │ │
                      │ └──────────────────────────────────┘ │
                      └──────────────────┬───────────────────┘
                                         │
            ┌────────────────────────────┼────────────────────────────┐
            ▼                            ▼                            ▼
┌──────────────────────┐     ┌──────────────────────┐     ┌──────────────────────┐
│ Microsoft SQL Server │     │  Redis Cache & OTP   │     │ Firebase Storage     │
│ (Relational Data &   │     │  (Fast Session &     │     │ (Product Images,     │
│  Order Persistence)  │     │   Token Caching)     │     │  Banners & Assets)   │
└──────────────────────┘     └──────────────────────┘     └──────────────────────┘
```

---

## 🚀 Key Features

### 1. 🛍 Product & Combo Management
- **Catalog & Categories**: Multi-category support with product categorization, status transitions (`ProductStatus`, `CategoryStatus`), and dynamic search.
- **Product Combos / Bundles**: Group multiple complementary items into promotional combo packages with discounted pricing and stock monitoring.
- **Sliders & Promotional Banners**: Manage homepage carousel banners and featured product sliders dynamically.

### 2. 🛒 Shopping Cart & Order Processing
- **Cart Management**: Add, update quantity, inspect item variations, and clear carts linked to customer accounts.
- **Order Lifecycle**: Track orders through state transitions (`OrderStatus`) from checkout to delivery.
- **Excel Report Generation**: Export order manifests and product lists into Microsoft Excel spreadsheets via **Apache POI**.

### 3. 📍 Vietnamese Administrative Address System
- Built-in geographic hierarchy containing Vietnamese **Provinces**, **Districts**, and **Wards** (`/api/v1/address`).
- Enables precise shipping address selection and seamless courier integration.

### 4. 🔐 Security & Redis-Backed OTP Flow
- **Stateless JWT Security**: Secure access tokens and refresh tokens via JJWT.
- **OTP Verification & Password Reset**: Two-factor email verification codes generated and cached in **Redis** with automatic expiration, delivered via **Spring Mail (Gmail SMTP)**.
- **Role Isolation**: Enforces RBAC permissions between standard customers (`USER`) and administrators (`ADMIN`).

### 5. ☁️ Cloud Media Storage
- Integrated **Firebase Admin SDK** for fast, high-availability image and document uploads (`/api/v1/file/upload/multiple`).

---

## 🛠 Technology Stack

| Domain | Technology / Library | Version | Purpose |
|---|---|---|---|
| **Language** | Java OpenJDK | 17 | Modern LTS language runtime |
| **Framework** | Spring Boot | 3.3.4 | Core enterprise application framework |
| **Security** | Spring Security & JJWT | 0.12.5 | Stateless JWT authentication & authorization |
| **Database** | Microsoft SQL Server (mssql-jdbc) | 2019 / 2022 | Persistent transactional relational storage |
| **ORM** | Spring Data JPA (Hibernate 6) | 3.3.4 | Object-relational mapping & transactions |
| **Caching & OTP** | Spring Data Redis | 3.3.4 | Distributed cache, OTP token TTL management |
| **Email Service** | Spring Boot Starter Mail | 3.3.5 | Transactional emails and verification codes via SMTP |
| **Cloud Storage** | Firebase Admin SDK | Latest | Media and image cloud asset hosting |
| **Spreadsheets** | Apache POI | 5.3.0 | Excel file export for orders and inventory |
| **Object Mapping**| ModelMapper | 3.2.1 | Entity-to-DTO conversion mapping |
| **Documentation** | SpringDoc OpenAPI Starter WebMVC | 2.5.0 | Interactive API documentation (`/swagger-ui.html`) |

---

## 📁 Project Structure

```plaintext
Noxinh/
├── src/
│   ├── main/
│   │   ├── java/org/company/noxinh/
│   │   │   ├── NoxinhApplication.java     # Spring Boot application entry point
│   │   │   ├── config/                    # Security, Redis, ModelMapper, Swagger configs
│   │   │   ├── constants/                 # Enums (Role, OrderStatus, ProductStatus, etc.)
│   │   │   ├── controller/                # REST endpoints (Auth, Cart, Order, Product, etc.)
│   │   │   ├── dto/                       # Request and Response transfer objects
│   │   │   ├── entity/                    # JPA entity models (Product, Order, Combo, etc.)
│   │   │   ├── exception/                 # Global exception handler & error structures
│   │   │   ├── repository/                # Data JPA repositories & sub-repositories
│   │   │   ├── service/                   # Business logic interfaces
│   │   │   │   └── impl/                  # Concrete service implementations
│   │   │   └── utils/                     # Email, OTP, Firebase, and validation utilities
│   │   └── resources/
│   │       ├── application.properties     # Application settings & properties
│   │       └── META-INF/                  # Manifest specifications
│   └── test/                              # Automated integration and unit tests
├── Dockerfile                             # Container build file
├── docker-compose.yml                     # Docker Compose orchestration
├── key.env                                # Environment secrets template
├── pom.xml                                # Maven build and dependency configuration
└── mvnw / mvnw.cmd                        # Maven wrapper binaries
```

---

## 📋 Prerequisites

Before running the backend locally:

- **JDK 17 or higher**
- **Apache Maven 3.9+** (or use `./mvnw`)
- **Microsoft SQL Server 2019+**
- **Redis Server** (Local instance or Docker container on port `6379`)
- **Gmail SMTP Credentials** (or custom SMTP provider for emails)
- **Firebase Service Account JSON** (for media upload functionality)

---

## ⚙️ Environment Configuration

Set up environment variables in your environment or within a `key.env` file in the project root:

```properties
# ===============================
# Database Configuration
# ===============================
SPRING_DATASOURCE_URL=jdbc:sqlserver://localhost:1433;databaseName=Noxinh;encrypt=true;trustServerCertificate=true
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=YourStrongPassword!
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.SQLServerDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true

# ===============================
# JWT Token Settings
# ===============================
SECRET_KEY=94b83cf0b942b22cc7c98924f90eec3bc90b974d61392b4eb694b85f7c68265e
EXPIRATION=604800000
REFRESH_TOKEN_EXPIRATION=2592000000

# ===============================
# Redis Cache Configuration
# ===============================
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379

# ===============================
# Email & SMTP Configuration
# ===============================
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password

# ===============================
# Firebase Storage Credentials
# ===============================
FIREBASE_CREDENTIALS_PATH=src/main/resources/firebase-adminsdk.json
```

---

## 🏃 Getting Started

### Run Locally (Maven)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/hopelessabit/Noxinh.git
   cd Noxinh
   ```

2. **Ensure Redis and SQL Server are running:**
   ```bash
   # Launch Redis using Docker if not installed locally:
   docker run -d -p 6379:6379 --name redis redis:7-alpine
   ```

3. **Build the application:**
   - **Linux / macOS:**
     ```bash
     ./mvnw clean package -DskipTests
     ```
   - **Windows (PowerShell / CMD):**
     ```powershell
     .\mvnw.cmd clean package -DskipTests
     ```

4. **Run the service:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or launch the JAR:
   ```bash
   java -jar target/Noxinh-0.0.1-SNAPSHOT.jar
   ```

The application starts on port `8080` by default.

---

### Run with Docker Compose

1. **Build and start the container:**
   ```bash
   docker compose up --build -d
   ```

2. **View live logs:**
   ```bash
   docker compose logs -f admission-portal
   ```

3. **Stop services:**
   ```bash
   docker compose down
   ```

---

## 📖 API Documentation & Swagger

When the application is running, open the interactive Swagger UI:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI 3 Docs:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

To authenticate API requests:
1. Call `POST /api/v1/auth/login` (or `/api/v1/auth/login-admin`) to obtain a JWT.
2. Click the **Authorize 🔓** button in Swagger.
3. Enter `Bearer <token>` and click **Authorize**.

---

## 🌐 Core API Reference

### 🔐 Authentication (`/api/v1/auth` & `/api/v1/account`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Public | Register new customer account |
| `POST` | `/api/v1/auth/login` | Public | Customer authentication & JWT issuance |
| `POST` | `/api/v1/auth/login-admin` | Public | Administrator login |
| `POST` | `/api/v1/auth/reset-password` | Public | Request email OTP for password reset |
| `POST` | `/api/v1/auth/verify-password/{UUID}` | Public | Verify OTP code and reset password |
| `GET` | `/api/v1/account/profile` | Authenticated | Fetch current user account profile |
| `PUT` | `/api/v1/account/change-password` | Authenticated | Update account password |

### 🛍 Products & Categories (`/api/v1/product` & `/api/v1/category`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/product/search` | Public | Search products with pagination & filters |
| `GET` | `/api/v1/product/{productId}/include-categories/{flag}` | Public | Fetch product with category associations |
| `POST` | `/api/v1/product/` | `ADMIN` | Create a new product |
| `PUT` | `/api/v1/product/info/{productId}` | `ADMIN` | Update product details |
| `PUT` | `/api/v1/product/status/{productId}` | `ADMIN` | Activate or deactivate product |
| `DELETE`| `/api/v1/product/{productId}` | `ADMIN` | Remove a product |
| `GET` | `/api/v1/category` | Public | List all categories |
| `POST` | `/api/v1/category` | `ADMIN` | Create category |
| `POST` | `/api/v1/category/{categoryId}/attach-product/{productId}` | `ADMIN` | Associate product with category |

### 🎁 Combos (`/api/v1/combo`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/combo/` | Public | Retrieve all promotional combos |
| `POST` | `/api/v1/combo/` | `ADMIN` | Create a combo bundle |
| `PUT` | `/api/v1/combo/add/{comboId}/product/{productId}` | `ADMIN` | Add product to combo |
| `PUT` | `/api/v1/combo/remove/{comboId}/product/{productId}` | `ADMIN` | Remove product from combo |

### 🛒 Cart & Orders (`/api/v1/cart` & `/api/v1/order`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/cart/` | Authenticated | Retrieve customer shopping cart |
| `POST` | `/api/v1/cart/{productId}` | Authenticated | Add item to cart |
| `DELETE`| `/api/v1/cart/{productId}` | Authenticated | Remove item from cart |
| `POST` | `/api/v1/order/` | Authenticated | Place a new order |
| `GET` | `/api/v1/order/user` | Authenticated | View current customer orders |
| `GET` | `/api/v1/order/get-all` | `ADMIN` | View all customer orders |
| `PUT` | `/api/v1/order/admin-update/{orderId}` | `ADMIN` | Update order processing & shipping status |
| `GET` | `/api/v1/order/download-excel` | `ADMIN` | Export orders to Excel spreadsheet |

### 📍 Administrative Addresses (`/api/v1/address`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/address/province` | Public | List all Vietnamese provinces |
| `GET` | `/api/v1/address/district/{provinceId}` | Public | List districts by province |
| `GET` | `/api/v1/address/ward/{districtId}` | Public | List wards by district |

### 🎨 Banners & Sliders (`/api/v1/banner` & `/api/v1/slider`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/banner/display` | Public | Get active promotional banners |
| `POST` | `/api/v1/banner` | `ADMIN` | Upload and configure a banner |
| `GET` | `/api/v1/slider/` | Public | Fetch featured slider products |
| `PUT` | `/api/v1/slider/add-product/{productId}` | `ADMIN` | Add product to slider carousel |

---

## 🤝 Contributing & License

1. Fork the repository and create a feature branch (`git checkout -b feature/order-tracking`).
2. Make your improvements and commit changes (`git commit -m 'feat: add courier webhook integration'`).
3. Push to your branch (`git push origin feature/order-tracking`).
4. Submit a Pull Request.

---

## 📄 License

This repository is proprietary software developed for Noxinh. All rights reserved.
