# API Design

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
