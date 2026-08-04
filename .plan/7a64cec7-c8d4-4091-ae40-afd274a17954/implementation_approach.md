# Implementation Approach

## Implementation Approach

### Backend Layers

**Controller** — `OrderController` (extend existing controller or add the export endpoint alongside existing order endpoints):
- `GET /api/orders/export` mapped method that writes CSV directly to `HttpServletResponse`
- Accepts optional `@RequestParam String status`
- Sets response content type to `text/csv` and `Content-Disposition` header for download
- Delegates to the service layer for data retrieval

**Service** — `OrderService` (add an `exportOrders` method):
- Accepts an optional status filter
- Calls the repository to fetch orders joined with customer email and computed total
- Returns a `List<OrderExportRow>` (a simple DTO/record with the 5 CSV fields)

**Repository** — `OrderRepository` (add a custom JPQL or native query):
- Single query that joins `orders` → `customers` (for email) and aggregates `order_items` (for total_cents)
- Sorted by `orders.id ASC`
- Optionally filtered by status via a query parameter

### CSV Generation
- **Manual string building** — no external CSV library needed for 5 simple columns with no commas/quotes in values
- Write header row `id,customer_email,status,order_date,total_cents` then one row per order
- Use `PrintWriter` from `HttpServletResponse.getWriter()` to write directly to the response stream
- No intermediate file — write straight to the HTTP response

### DTO
- `OrderExportRow` Java record: `record OrderExportRow(Long id, String customerEmail, String status, String orderDate, Long totalCents)`
- Used as a JPQL constructor expression or mapped from query results

### Key Design Choices
- **No external CSV library** (e.g., OpenCSV) — the data is simple, columns are known, and values don't contain delimiters. Plain string concatenation keeps dependencies minimal.
- **No streaming/pagination** — the order count for this app is small (demo scale). Loading all matching orders in memory is appropriate.
- **Single query** — one JOIN+GROUP BY query is more efficient than N+1 lookups per order for total calculation.
- **Frontend download** — uses `window.location.href` assignment to the export URL, which triggers the browser's native file download dialog. No fetch+blob complexity needed since the response is already a file download.
