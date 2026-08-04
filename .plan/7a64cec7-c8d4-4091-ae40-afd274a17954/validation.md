# Validation

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
