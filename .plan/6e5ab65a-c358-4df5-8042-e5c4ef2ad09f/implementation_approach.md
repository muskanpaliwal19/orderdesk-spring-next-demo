# Implementation Approach

## Implementation Approach: Standard Spring Boot CRUD + Next.js Page

### Backend (Spring Boot)

Follow the locked layered architecture exactly:

| Layer | Class | Responsibility |
|-------|-------|----------------|
| Controller | `CustomerController` | `GET /api/customers`, `POST /api/customers` — validation, HTTP status codes, DTO mapping |
| Service | `CustomerService` | Business logic — duplicate email check, default tier assignment |
| Repository | `CustomerRepository` | `JpaRepository<Customer, Long>` — `findAllByOrderByCreatedAtDesc()`, `existsByEmail(String)` |
| Entity | `Customer` | JPA entity with Lombok annotations (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) |
| DTOs | `CreateCustomerRequest` (record), `CustomerResponse` (record) | Decouple API contract from entity |

### API Endpoints

**`GET /api/customers`**
- Returns `{ "customers": [...] }` — array of all customers sorted by `created_at` DESC
- Each customer includes: `id`, `name`, `email`, `tier`, `createdAt`
- Matches legacy response shape exactly

**`POST /api/customers`**
- Request body: `{ "name": "...", "email": "...", "tier": "..." }` — tier is optional (defaults to `"standard"`)
- Success: `201 Created` with `{ "customer": { id, name, email, tier, createdAt } }`
- Validation failure: `400 Bad Request` with `{ "error": "..." }`
- Duplicate email: `409 Conflict` with `{ "error": "A customer with this email already exists" }`

### Frontend (Next.js)

A dedicated `/customers` route with a single `'use client'` page component containing:
- **Registration form** — name (text input), email (email input), tier (select dropdown), submit button
- **Customer table** — displays all customers sorted by most recently created, showing name, email, and tier badge
- Data fetching via `fetch('/api/customers')` with `useState`/`useEffect`
- Form submission via `fetch('/api/customers', { method: 'POST' })` — on success, refresh the list; on error, display the backend error message

### Flyway Migration

A single `V1__create_customers_table.sql` migration to create the `customers` table in PostgreSQL. This is the foundational migration — other stories will add subsequent versioned migrations.

### Seed Data

Flyway `V1.1__seed_customers.sql` (or `afterMigrate` callback) inserts the 3 legacy demo customers (Ava Chen, Noah Singh, Maya Patel) so the app is immediately usable after startup.

### Key Decisions
- **Duplicate email handling**: Check at the service layer via `existsByEmail()` before insert, and also enforce via DB unique constraint. Return `409 Conflict` (the legacy app relies on the DB constraint silently; the target should return a clear error).
- **Tier enum**: Model as a Java `enum` (`STANDARD`, `PREMIUM`, `ENTERPRISE`) with `@Enumerated(EnumType.STRING)` on the entity. Stored as lowercase string in PostgreSQL to match legacy data.
- **ID generation**: Use `@GeneratedValue(strategy = GenerationType.IDENTITY)` with PostgreSQL `BIGSERIAL` — auto-incrementing IDs matching legacy behavior.
- **Timestamps**: Use `@CreationTimestamp` with `LocalDateTime` / PostgreSQL `TIMESTAMP`.
