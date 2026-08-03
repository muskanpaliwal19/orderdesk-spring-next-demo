# Implementation Approach

## Implementation Approach: View & Filter Orders

### Backend

**Endpoint:** `GET /api/orders?status={status}` — matches the legacy contract exactly.

- **Controller** (`OrderController`): Accepts an optional `status` query parameter. If provided, validates it against the allowed enum values (`new`, `paid`, `shipped`, `cancelled`); returns 400 for invalid values. Delegates to `OrderService`.
- **Service** (`OrderService`): Thin layer — calls the repository method and returns the DTO list. No business logic transformation needed for this read-only operation.
- **Repository** (`OrderRepository` extends `JpaRepository<Order, Long>`): Custom JPQL query that JOINs `orders` → `customers` (for name/email) and `order_items` (for total computation). Uses `SUM(oi.quantity * oi.unitPriceCents)` with `GROUP BY` to compute totals inline. Two methods:
  - `findAllOrdersWithTotals()` — no filter, all orders
  - `findOrdersByStatusWithTotals(String status)` — filtered by status
  - Both sorted by `orderDate DESC` (most recent first)
- **DTO** (`OrderListItemDto` — Java record):
  ```
  record OrderListItemDto(Long id, String customerName, String customerEmail,
                          String status, Long totalCents, String orderDate, String notes)
  ```
  The `totalCents` field is returned as a `Long` (integer cents). The frontend converts to USD display format.

### Frontend

- **Page:** `/orders` route — `'use client'` component
- **Data fetching:** `fetch('/api/orders')` and `fetch('/api/orders?status=...')` via `useEffect`, triggered on filter change
- **State:** `useState` for `orders` (array) and `selectedStatus` (string)
- **Filter:** `<select>` dropdown with options: All statuses, New, Paid, Shipped, Cancelled. On change, re-fetches from the API with the selected status query param (server-side filtering, matching legacy behavior).
- **Rendering:** Map over orders array, render a card per order with status badge
- **Currency:** Format `totalCents` as USD using `Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })` with `value / 100` — same approach as the legacy `cents()` helper.

### Validation
- Backend rejects unknown `status` values with HTTP 400 and a clear error message.
- If no orders match the filter, return an empty array (not an error).
- The `totalCents` field defaults to `0` if an order has no line items (using `COALESCE` in the query).
