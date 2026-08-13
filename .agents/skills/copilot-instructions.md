# Banking Application Engineering Guidelines

## Tech Stack
- Backend: Java 17+, Spring Boot 3.x, Spring Security (JWT), Spring Data JPA, MySQL
- Database Connection Pool: HikariCP
- Testing: JUnit 5, Mockito

## Architecture Guidelines
1. **Transactions:** All service methods performing database modifications (e.g., debit, credit, transfer) MUST be annotated with `@Transactional`.
2. **Security:** Routes under `/api/auth/**` are public. All other endpoints require valid JWT bearer tokens in the `Authorization` header.
3. **Data Integrity:** Transfers must always update sender and receiver balances in a single transaction and construct matching `CREDIT_TRANSFER` and `DEBIT_TRANSFER` history logs.
4. **Exception Handling:** Use `CustomException` with meaningful error messages and return appropriate HTTP status codes.