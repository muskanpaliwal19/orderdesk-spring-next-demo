# Locked Decisions for Story 2001be5f-1682-4db8-aa11-1c821a9f0686

## Implementation Approach
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

## UI/UX
## UI/UX: Product Catalog Table

### Page Layout
The product catalog lives at the `/products` route in the sidebar navigation. It follows the app-wide layout: left sidebar nav + main content area.

### Design
- **Simple data table** inside a rounded card container, matching the legacy app's warm surface palette (`#fffaf0` surface, `#e1d7c7` borders, `#245f4f` brand green).
- **Three columns:** Product Name (left-aligned, bold), SKU (left-aligned, monospace code badge), Unit Price (right-aligned, brand green, dollar-formatted).
- **Row hover:** Subtle green tint on hover for scannability.
- **No pagination** — the product catalog is small (4 seed products); a simple list is sufficient.

### Column Details
| Column | Alignment | Formatting |
|--------|-----------|------------|
| Product Name | Left | Medium weight text |
| SKU | Left | Monospace `<code>` badge with light background |
| Unit Price | Right | `$XX.XX` via `Intl.NumberFormat` (cents ÷ 100), bold brand green |

### States
1. **Loading:** Skeleton rows or a centered spinner while `GET /api/products` is in flight.
2. **Populated:** Table rows sorted A→Z by name with a count label below ("Showing N active products").
3. **Empty:** A centered empty state with a package icon, "No active products" heading, and a short explanation. Handles the edge case where all products have been deactivated.
4. **Error:** Simple error message with retry option if the API call fails.

### Navigation
- Sidebar nav with pages: Dashboard, Customers, **Products** (active), Orders, Audit Log.
- "Products" is highlighted as the active page when viewing this route.

### Visual Consistency
- Colors, border radius, and shadows match the legacy app's warm design language (cream backgrounds, rounded corners, subtle amber shadows).
- Typography uses Inter with the same hierarchy as other pages.

### Responsive
- On narrow screens, the sidebar collapses or the table scrolls horizontally.
- All three columns are compact enough to remain readable at small widths.
Artifacts: `artifacts/product_catalog_page.html`
