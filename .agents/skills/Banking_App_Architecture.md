# Banking Application - System Architecture Documentation

## 1. System Architecture Diagram

+-----------------------------------------------------------------------------------+
|                                  CLIENT LAYER                                     |
|                                                                                   |
|   +---------------------------------------------------------------------------+   |
|   |                            React Single Page App                          |   |
|   |  [ Dashboard.jsx ] <----> [ TransactionHistory.jsx ]                      |   |
|   |         |                          |                                      |   |
|   |         +--------------------------+                                      |   |
|   |                      |                                                    |   |
|   |               [ api.js (Axios) ]                                          |   |
|   +----------------------|----------------------------------------------------+   |
+--------------------------|--------------------------------------------------------+
|
HTTP / JSON
Header: Authorization: Bearer
|
v
+-----------------------------------------------------------------------------------+
|                                  BACKEND LAYER                                    |
|                                 (Spring Boot)                                     |
|                                                                                   |
|  +-----------------------------------------------------------------------------+  |
|  |                             SECURITY LAYER                                  |  |
|  |  [ JwtAuthenticationFilter ] ---> [ CustomerUserDetails ]                   |  |
|  +-----------------------------------|-----------------------------------------+  |
|                                      |                                            |
|                                Extract Claims & User                              |
|                                      v                                            |
|  +-----------------------------------------------------------------------------+  |
|  |                            CONTROLLER LAYER                                 |  |
|  |  [ TransactionController ]  (@RequestMapping("/api/transaction"))          |  |
|  |    ├── POST /api/transaction/{Type}  (DEBIT / CREDIT)                        |  |
|  |    ├── POST /api/transaction/transfer                                       |  |
|  |    └── GET  /api/transaction/history                                       |  |
|  +-----------------------------------|-----------------------------------------+  |
|                                      |                                            |
|                                Call Business Logic                                |
|                                      v                                            |
|  +-----------------------------------------------------------------------------+  |
|  |                              SERVICE LAYER                                  |  |
|  |  [ TransactionServiceImpl ] (@Transactional)                                |  |
|  |    ├── addMoney(jwt, amount)                                                |  |
|  |    ├── debitMoney(jwt, amount)                                              |  |
|  |    ├── transfer(jwt, amount, receiverAccountNo)                             |  |
|  |    └── getTransactionHistory(jwt)                                           |  |
|  +-----------------------------------|-----------------------------------------+  |
|                                      |                                            |
|                              Data Access via Repos                             |
|                                      v                                            |
|  +-----------------------------------------------------------------------------+  |
|  |                             REPOSITORY LAYER                                |  |
|  |  [ AccountRepository ]   <-->  [ TransactionRepository ]                   |  |
|  |    findByUser()                  findByAccountOrderByTimeDesc()                |  |
|  |    findByAccountNumber()                                                       |  |
|  +-----------------------------------|-----------------------------------------+  |
+--------------------------------------|--------------------------------------------+
|
Spring Data JPA / ORM
|
v
+-----------------------------------------------------------------------------------+
|                                DATABASE LAYER                                     |
|                                                                                   |
|    +---------------------+                   +---------------------+              |
|    |        User         |                   |       Account       |              |
|    +---------------------+                   +---------------------+              |
|    | id (PK)             | <--- 1 to 1 ----> | id (PK)             |              |
|    | username / email    |                   | account_number      |              |
|    | password            |                   | balance             |              |
|    +---------------------+                   +----------|----------+              |
|                                                         |                         |
|                                                      1 to Many                    |
|                                                         |                         |
|                                                         v                         |
|                                              +---------------------+              |
|                                              |     Transaction     |              |
|                                              +---------------------+              |
|                                              | id (PK)             |              |
|                                              | account_id (FK)     |              |
|                                              | type (ENUM)         |              |
|                                              | amount              |              |
|                                              | counter_party       |              |
|                                              | time / created_at   |              |
|                                              +---------------------+              |
+-----------------------------------------------------------------------------------+


---

## 2. Layer Breakdown Summary

* **React Frontend (`Dashboard.jsx`, `TransactionHistory.jsx`):** Maintains UI state and handles transfers/history views using Axios with Bearer JWT headers.
* **Security Layer (`CustomerUserDetails` / JWT Filters):** Intercepts requests, validates authorization claims, and resolves current authenticated user context.
* **REST Controllers (`TransactionController`):** Exposes HTTP endpoints for self-account operations, transfers, and historical data retrieval.
* **Service Layer (`TransactionServiceImpl`):** Handles core transaction logic inside `@Transactional` boundaries (balance updates, debit/credit ledger generation, validation).
* **Data Access Layer (`AccountRepository`, `TransactionRepository`):** Performs relational queries using Spring Data JPA.
* **Database (MySQL):** Stores entity state for `User`, `Account`, and `Transaction` wi


3**Request Data Flow & Lifecycle**
   Example: Executing a Transfer (POST /api/transaction/transfer)
   React App               Spring Boot Security      Controller               Service Layer             Database
   │                         │                        │                        │                         │
   ├─ Send POST + JWT ──────>│                        │                        │                         │
   │  (TransferRequest)      ├─ Validate Token & ────>│                        │                         │
   │                         │  Authenticate User     │                        │                         │
   │                         │                        ├─ Call transfer() ─────>│                         │
   │                         │                        │                        ├─ Begin Transaction      │
   │                         │                        │                        ├─ Verify Balance >= Amt  │
   │                         │                        │                        ├─ Update Sender Bal ────>│
   │                         │                        │                        ├─ Update Recipient Bal ─>│
   │                         │                        │                        ├─ Create Sender Tx ─────>│
   │                         │                        │                        ├─ Create Recipient Tx ──>│
   │                         │                        │                        ├─ Commit Transaction ────>│
   │                         │                        │<─ Return Status ───────┤                         │
   │<─ 200 OK Response ──────┴────────────────────────┴────────────────────────┘                         │
   │                                                                                                     │
   ├─ Auto-refresh Account & History Data ──────────────────────────────────────────────────────────────>│
****4**Layer Breakdown Summary****
   React Frontend (Dashboard.jsx, TransactionHistory.jsx): Maintains application state and handles UI actions. Calls API endpoints via Axios using a JWT bearer header stored upon user login.

Security Layer (CustomerUserDetails / JWT Filters): Intercepts incoming requests, decodes the Authorization JWT header, and extracts user metadata without requiring repeated database logins.

REST Controllers (TransactionController): Exposes HTTP endpoints for self-service deposits, withdrawals, transfers, and historical lookups.

Service Layer (TransactionServiceImpl): Enforces transaction boundary integrity using @Transactional. Manages balance updates, creates debit/credit ledger records, and validates fund availability.

Data Access Layer (AccountRepository, TransactionRepository): Uses Spring Data JPA to automatically construct optimized SQL queries (e.g., fetching transactions sorted by time in descending order).

Database (MySQL / Relational DB): Maintains relational data across User, Account, and Transaction entities with primary key and foreign key constraints.