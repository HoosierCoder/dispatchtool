# DispatchTool

**DispatchTool** is a modern, multi-tenant SaaS application designed to streamline field service operations for Small-to-Medium Businesses (SMBs). It allows companies to manage customers, locations, and service tickets in a secure, isolated environment.

---

## 🚀 Key Features

*   **Multi-Tenancy**: Built from the ground up with a robust **discriminator-column architecture**. Data is logically isolated per tenant using Hibernate Filters and Aspect-Oriented Programming (AOP), ensuring "Sarah's Donuts" never sees data from "Joe's Plumbing".
*   **Role-Based Access Control (RBAC)**: Granular security permissions separating System Admins (platform owners) from Tenant Users (Admin, Manager, Lead, Associate).
*   **Ticket Lifecycle Management**: Full workflow support for creating, assigning, dispatching, and resolving service tickets.
*   **Smart Dashboard**: Role-aware dashboards that adapt to the user's responsibilities (e.g., Technicians see "My Work," Managers see "Team Stats").
*   **Modern UI**: Server-side rendered views using **Thymeleaf** styled with **Tailwind CSS** for a responsive, lightweight frontend.

---

## 🏗️ Technical Architecture

This project demonstrates a production-ready enterprise architecture using the latest Java ecosystem standards.

### Tech Stack
*   **Language**: Java 21 (LTS)
*   **Framework**: Spring Boot 3.x
*   **Security**: Spring Security 6 (Custom Authentication Provider, CSRF protection, Session Management)
*   **Database**: MySQL 8.0
*   **Migrations**: Liquibase (Version controlled database schema)
*   **Frontend**: Thymeleaf + Tailwind CSS (No heavy JS framework required)
*   **Build Tool**: Maven

### Architectural Highlights

#### 1. Multi-Tenant Data Isolation
Instead of relying on developers to remember `WHERE tenant_id = ?` in every query, this project uses a cross-cutting concern approach:
*   **`TenantFilter`**: A Servlet Filter that intercepts every request, extracts the Tenant ID (from the URL or Session), and sets it in a `ThreadLocal` **`TenantContext`**.
*   **`TenantAspect`**: An AspectJ Aspect that wraps all Repository calls. It reads the context and automatically enables a Hibernate Filter on the session.
*   **Result**: Complete, automatic data isolation that is difficult to bypass accidentally.

#### 2. Advanced Security Strategy
*   **Dual Authentication Flows**:
    *   **Form Login**: For browser users (`/default-tenant/login`), providing a persistent session.
    *   **Basic Auth**: For API clients (Postman/Mobile), supporting stateless interaction.
*   **Smart Identity Resolution**: `CustomUserDetailsService` intelligently handles "System Users" (global access) versus "Tenant Users" (scoped access) using a unique username resolution strategy (`username` vs `username@tenantId`).

---

## 🛠️ Getting Started

### Prerequisites
*   Java 21 SDK
*   MySQL 8.0 (running on localhost:3306)
*   Maven

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/yourusername/dispatchtool.git
    cd dispatchtool
    ```

2.  **Configure Database**
    Create a MySQL database named `hoosierCoder`. Update `src/main/resources/application.properties` if your credentials differ from the defaults (`hoosiercoder`/`hoosiercoder`).

3.  **Build and Run**
    The application uses Liquibase to automatically create the schema and seed initial data.
    ```bash
    mvn spring-boot:run
    ```

4.  **Access the App**
    *   **System Admin**: http://localhost:8080/login (User: `rdude`, Pwd: `password`)
    *   **Tenant Login**: http://localhost:8080/default-tenant/login (User: `lseattle`, Pwd: `password`)

---

## 🔮 Future Roadmap

*   **API-First Mobile App**: Building a Flutter/React Native companion app for technicians in the field.
*   **Stripe Integration**: Automating subscription billing for tenants.
*   **QuickBooks/Xero Export**: One-click CSV exports for tenant accounting.

---

### Author
**HoosierCoder**  
*Building scalable software solutions.*
