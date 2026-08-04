# Locked Decisions for Story 7a64cec7-c8d4-4091-ae40-afd274a17954

## Implementation Approach
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

## UI/UX
## UI/UX: Export Button on Orders Page

### Placement
- Add an **"Export CSV"** button in the orders page header area, next to the existing status filter dropdown
- Button uses a download/export icon (↓ or document icon) alongside the text label
- Positioned to the right of the filter controls so it doesn't disrupt the existing filter workflow

### Behavior
- Clicking the button navigates the browser to `/api/orders/export` (or `/api/orders/export?status=X` if a filter is active), triggering a native file download
- The button is always enabled — if no orders match, the user receives a valid CSV with only the header row
- No loading spinner needed since the browser handles the download natively in the background
- The current page state is preserved — the user stays on the orders page after clicking

### Button Design
- Secondary/outlined style to distinguish it from primary actions (like creating an order)
- Small/compact size consistent with the filter controls
- Label: **"Export CSV"** — concise and unambiguous

### No Confirmation Dialog
- Export is a read-only, non-destructive action — no confirmation needed
- Keeps the interaction fast (single click → download starts)

## Validation
## Validation & Edge Cases

### Input Validation
- **Status parameter:** If `?status=` is provided, validate it's one of `new`, `paid`, `shipped`, `cancelled`. Return `400 Bad Request` with a JSON error body for invalid values. This matches the validation pattern on the existing `PATCH /api/orders/:id/status` endpoint.
- **No status parameter:** Valid — returns all orders unfiltered.

### Edge Cases
| Scenario | Behavior |
|----------|----------|
| No orders exist (or none match filter) | Return 200 with header-only CSV: `id,customer_email,status,order_date,total_cents\n` |
| Order has no order items | `total_cents` = 0 (use `COALESCE` / `LEFT JOIN` in query) |
| Customer deleted but order exists | Should not happen due to FK constraints, but if it does, use empty string for `customer_email` |
| Special characters in email | Email addresses don't contain commas, so no CSV escaping needed. If any field ever contained a comma or quote, wrap in double quotes per RFC 4180. |
| Very large export | Not a concern at demo scale. No pagination or streaming needed. |

### CSV Integrity
- Header row is always present, even for empty results
- Row count (excluding header) must equal the number of matching orders — enforced by using a single query with no post-filtering
- No duplicate rows — `GROUP BY orders.id` ensures one row per order
- Sort order is deterministic: `ORDER BY orders.id ASC`

### Error Responses
- `200 OK` — CSV file (even if empty, header-only)
- `400 Bad Request` — invalid status parameter (JSON error body, not CSV)

## API Design
## API Endpoint: `GET /api/orders/export`

### Contract
- **Method:** `GET`
- **Path:** `/api/orders/export`
- **Query Parameters:**
  - `status` (optional) — one of `new`, `paid`, `shipped`, `cancelled`. When provided, only orders matching that status are exported. When omitted, all orders are exported.
- **Response Headers:**
  - `Content-Type: text/csv; charset=UTF-8`
  - `Content-Disposition: attachment; filename="orders.csv"`
- **Response Body:** CSV text with a header row followed by one row per order, sorted by order ID ascending.

### CSV Format
```
id,customer_email,status,order_date,total_cents
1,ava@example.com,paid,2026-02-01T12:00:00Z,8895
2,noah@example.com,new,2026-02-02T14:20:00Z,4599
3,maya@example.com,shipped,2026-02-03T16:40:00Z,85992
```

**Columns** (exact match of legacy `export-orders.js`):
| Column | Source | Notes |
|--------|--------|-------|
| `id` | `orders.id` | Integer order ID |
| `customer_email` | `customers.email` | Joined via `orders.customer_id` → `customers.id` |
| `status` | `orders.status` | One of: new, paid, shipped, cancelled |
| `order_date` | `orders.order_date` | ISO 8601 timestamp |
| `total_cents` | `SUM(order_items.quantity * order_items.unit_price_cents)` | Computed per order |

### Error Responses
- `200 OK` with header-only CSV if no orders match the filter (valid empty result)
- `400 Bad Request` if `status` parameter is provided but not one of the four valid values

### Rationale
- Matches legacy column names and computation exactly
- Optional `status` filter reuses the same parameter pattern as the existing `GET /api/orders?status=` endpoint
- Returns CSV directly (not JSON-wrapped) for straightforward browser download

## API Design
## API Endpoint: `GET /api/orders/export`

### Contract
- **Method:** `GET`
- **Path:** `/api/orders/export`
- **Query Parameters:**
  - `status` (optional) — one of `new`, `paid`, `shipped`, `cancelled`. When provided, only orders matching that status are exported. When omitted, all orders are exported.
- **Response Headers:**
  - `Content-Type: text/csv; charset=UTF-8`
  - `Content-Disposition: attachment; filename="orders.csv"`
- **Response Body:** CSV text with a header row followed by one row per order, sorted by order ID ascending.

### CSV Format
```
id,customer_email,status,order_date,total_cents
1,ava@example.com,paid,2026-02-01T12:00:00Z,8895
2,noah@example.com,new,2026-02-02T14:20:00Z,4599
3,maya@example.com,shipped,2026-02-03T16:40:00Z,85992
```

**Columns** (exact match of legacy `export-orders.js`):
| Column | Source | Notes |
|--------|--------|-------|
| `id` | `orders.id` | Integer order ID |
| `customer_email` | `customers.email` | Joined via `orders.customer_id` → `customers.id` |
| `status` | `orders.status` | One of: new, paid, shipped, cancelled |
| `order_date` | `orders.order_date` | ISO 8601 timestamp |
| `total_cents` | `SUM(order_items.quantity * order_items.unit_price_cents)` | Computed per order |

### Error Responses
- `200 OK` with header-only CSV if no orders match the filter (valid empty result)
- `400 Bad Request` if `status` parameter is provided but not one of the four valid values

### Rationale
- Matches legacy column names and computation exactly
- Optional `status` filter reuses the same parameter pattern as the existing `GET /api/orders?status=` endpoint
- Returns CSV directly (not JSON-wrapped) for straightforward browser download
