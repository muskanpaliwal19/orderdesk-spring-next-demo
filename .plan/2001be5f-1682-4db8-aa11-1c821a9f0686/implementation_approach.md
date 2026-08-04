# Implementation Approach

## Implementation Approach: Read-Only Product Catalog

### Backend (Spring Boot)

**Endpoint:** `GET /api/products` — matches the legacy contract exactly.

**Response shape:**
```json
{
  "products": [
    { "id": 1, "sku": "SKU-BOARD-001", "name": "Planning Board", "unitPriceCents": 2499 }
  ]
}
```

**Layers:**

| Layer | Class | Responsibility |
|-------|-------|----------------|
| Controller | `ProductController` | `GET /api/products` → delegates to service, returns `{ "products": [...] }` wrapper |
| Service | `ProductService` | Calls repository, maps entities → DTOs |
| Repository | `ProductRepository` | `findByActiveTrueOrderByNameAsc()` — Spring Data derived query |
| Entity | `Product` | JPA `@Entity` with Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`), maps to `products` table |
| DTO | `ProductDto` (Java record) | Exposes `id`, `sku`, `name`, `unitPriceCents` — excludes the `active` flag |

**Key details:**
- The `active` column is **never exposed to the frontend** — the repository query filters it server-side via `findByActiveTrueOrderByNameAsc()`, and the DTO omits it entirely.
- Sorting is handled at the database level (`ORDER BY name ASC`) via the derived query method name, not in Java code.
- `unitPriceCents` is returned as an integer; dollar formatting (`$24.99`) is a frontend concern using `Intl.NumberFormat`.
- The response is wrapped in `{ "products": [...] }` to match the legacy `GET /api/products` contract exactly.
- The `active` column maps as a `boolean` in JPA (PostgreSQL `BOOLEAN`), converting from the legacy `TINYINT(1)` / `INTEGER` representation.

### Frontend (Next.js)

- **Route:** `/products` page, `'use client'` component
- **Data fetching:** `fetch('/api/products')` on mount via `useEffect` + `useState`
- **Rendering:** `<table>` with three columns: Product Name, SKU, Unit Price
- **Price formatting:** `Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })` converting cents → dollars
- **Empty state:** Friendly message when no active products exist
- **Loading state:** Skeleton/spinner while the API call is in flight

### Flyway Migration

The `products` table is created as part of the initial schema migration (shared across all stories). The Flyway seed script inserts the 4 demo products (all with `active = true`). No products-specific migration is needed beyond the project-level schema.

### Categories Removed
- **Data Mapping** — no schema changes; the `products` table structure is defined by the locked Data Migration decision.
- **Validation** — this is a pure read-only endpoint with no user input, no mutations, and no business rules beyond the `active` filter (which is a query concern handled by the repository).
