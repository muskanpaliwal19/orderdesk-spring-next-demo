# Locked Decisions for Story 5b9b6fa8-52f9-421b-9593-6aa14f50ab02

## Implementation Approach
## Implementation Approach: PATCH Endpoint + Inline Status Control

### Backend — Spring Boot

**Endpoint:** `PATCH /api/orders/{id}/status`

Matches the legacy contract exactly. Request body: `{ "status": "paid" }`. Response: `{ "ok": true }` on success.

**Layered flow:**

1. **`OrderController.updateOrderStatus(Long id, UpdateOrderStatusRequest request)`**
   - Receives the path variable and validated request body
   - Delegates to `OrderService`
   - Returns `200 OK` with `{ "ok": true }` on success
   - Returns `404` if order not found (via exception handler)
   - Returns `400` if status value is invalid (via Bean Validation)

2. **`OrderService.updateOrderStatus(Long id, String status)`**
   - Finds the order by ID — throws `OrderNotFoundException` if absent
   - Updates the `status` field on the `Order` entity
   - Creates an `AuditLog` entry with `entityType: "order"`, `eventType: "status_changed"`, and `message: "Order moved to {status}"`
   - Both operations in the same `@Transactional` method — audit log is never orphaned from the status change

3. **`OrderRepository`** — Spring Data JPA, `findById()` is sufficient
4. **`AuditLogRepository`** — Spring Data JPA, `save()` for new entries

**DTOs (Java records):**
- `UpdateOrderStatusRequest` — single `status` field, validated with a custom `@ValidOrderStatus` annotation
- Response is a simple `Map.of("ok", true)` or a shared `ApiResponse` record

**Order status enum:**
- Define `OrderStatus` as a Java enum (`NEW`, `PAID`, `SHIPPED`, `CANCELLED`) with a `@JsonValue` lowercase string representation
- Store as a VARCHAR in PostgreSQL (not a DB-level enum) for easier future extension
- The `Order` JPA entity maps the field with `@Enumerated(EnumType.STRING)`

**Exception handling:**
- `OrderNotFoundException` extends `ResponseStatusException(HttpStatus.NOT_FOUND)` or is caught by a `@RestControllerAdvice` that returns `{ "error": "order not found" }` with 404
- Validation failures return `{ "error": "status must be new, paid, shipped, or cancelled" }` with 400

### Frontend — Next.js

**Inline status dropdown** on each order row in the orders list page:

1. Each order row renders a `<select>` styled as a status badge (color-coded by current status)
2. On change, fires `PATCH /api/orders/{id}/status` with the selected value
3. On success, updates local state to reflect the new status immediately
4. On error, reverts the dropdown and shows an inline error message

**Integration with "View and Filter Orders" story:**
This story adds the status-change behavior to order rows. The order list itself (fetching, filtering, layout) is owned by the "View and Filter Orders" story. This story contributes:
- The `StatusSelect` component (dropdown with color-coded styling)
- The `updateOrderStatus(id, status)` API call function
- Error handling and feedback within each row

### Audit Logging Pattern

The audit log write happens inside `OrderService.updateOrderStatus()`, not in the controller or a separate listener. This keeps the pattern simple and transactional:

```
@Transactional
public void updateOrderStatus(Long id, String status) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    order.setStatus(OrderStatus.fromString(status));
    orderRepository.save(order);
    
    auditLogRepository.save(AuditLog.builder()
        .entityType("order")
        .entityId(id)
        .eventType("status_changed")
        .message("Order moved to " + status)
        .build());
}
```

This matches the legacy `db.js` behavior where `updateOrderStatus` writes both the status update and the audit log in a single operation.

## UI/UX
## UI/UX: Inline Status Dropdown on Order Rows

### Component: `StatusSelect`

Each order row in the orders list includes an inline `<select>` dropdown styled as a color-coded pill badge. Staff can change an order's status directly from the list without navigating away.

### Color Scheme (per status)

| Status | Background | Text | Border |
|--------|-----------|------|--------|
| New | `blue-50` | `blue-700` | `blue-200` |
| Paid | `emerald-50` | `emerald-700` | `emerald-200` |
| Shipped | `violet-50` | `violet-700` | `violet-200` |
| Cancelled | `red-50` | `red-700` | `red-200` |

Colors are chosen to be immediately distinguishable at a glance while staying harmonious with the legacy warm palette (cream surfaces, dark green brand, orange accent).

### Interaction Flow

1. **Default state:** The dropdown shows the current status, styled as a rounded pill badge with the matching color
2. **On click:** Native `<select>` dropdown opens showing all four status options
3. **On selection:** 
   - Optimistic UI update — badge color changes immediately
   - `PATCH /api/orders/{id}/status` fires in the background
   - On success: a toast appears at bottom-right confirming "Order #N moved to {status}"
   - On failure: dropdown reverts to previous value, inline error message appears below the order row, and an error toast is shown
4. **Loading state:** Dropdown is briefly disabled (pointer-events-none) while the API call is in flight to prevent double-clicks

### Layout within Order Row

```
┌──────────────────────────────────────────────────────────────┐
│  #1  Ava Chen  ava@example.com                  [ PAID ▼ ]  │
│  $88.95 · Feb 1, 2026, 12:00 PM                             │
│  Priority customer                                           │
└──────────────────────────────────────────────────────────────┘
```

- Order info (ID, customer name, email) on the left
- Status dropdown right-aligned as a pill-shaped select
- Secondary info (total, date, notes) below in muted text
- Error message area hidden by default, shown inline below the row on failure

### Component Structure

- **`StatusSelect`** — Reusable component accepting `orderId`, `currentStatus`, and `onStatusChange` callback
  - Renders a styled `<select>` with a chevron icon overlay
  - Manages optimistic state internally, reverts on error
  - Applies color classes dynamically based on selected value
- **`updateOrderStatus(id: number, status: string)`** — API helper in a shared `api.ts` module
  - Calls `fetch('/api/orders/{id}/status', { method: 'PATCH', body: { status } })`
  - Returns parsed JSON or throws on non-OK responses
- **Toast feedback** — A simple fixed-position toast component, auto-dismissing after 2.5 seconds

### Responsive Behavior

On narrow screens (< 640px), the order row stacks vertically: order info on top, status dropdown below aligned to the left. The dropdown maintains the same pill badge styling at all breakpoints.
Artifacts: `artifacts/order_status_inline_control_prototype.html`

## Validation
## Validation: Status Enum + Error Responses

### Server-Side Validation

**Status value validation:**
- Define an `OrderStatus` Java enum with values `NEW`, `PAID`, `SHIPPED`, `CANCELLED`
- Each enum constant has a `@JsonValue` lowercase string (`"new"`, `"paid"`, `"shipped"`, `"cancelled"`)
- The `UpdateOrderStatusRequest` DTO uses a custom `@ValidOrderStatus` constraint annotation, or simply accepts a `String` field and validates it in the service layer via `OrderStatus.fromString(status)` which throws on invalid input
- Invalid/missing status → **400** with `{ "error": "status must be new, paid, shipped, or cancelled" }` (matches legacy error message exactly)

**No transition enforcement:**
- Free-form status changes — any valid status can transition to any other valid status
- No state machine, no guards — matches legacy behavior exactly

**Order existence:**
- `OrderService` calls `orderRepository.findById(id)` which returns `Optional<Order>`
- If empty → throw `OrderNotFoundException` → **404** with `{ "error": "order not found" }` (matches legacy)

**Request body validation:**
- Missing or empty request body → 400 (Spring's built-in handling)
- `status` field null or absent → 400 via Bean Validation `@NotNull`
- `status` field present but not one of the four valid values → 400 via custom validation

### Error Response Format

All error responses follow a consistent shape:

```json
{ "error": "descriptive message" }
```

This matches the legacy contract. A `@RestControllerAdvice` or `@ExceptionHandler` on the controller centralizes error formatting:

| Scenario | HTTP Status | Response Body |
|----------|-------------|---------------|
| Invalid status value | 400 | `{ "error": "status must be new, paid, shipped, or cancelled" }` |
| Missing status field | 400 | `{ "error": "status is required" }` |
| Order not found | 404 | `{ "error": "order not found" }` |
| Malformed JSON body | 400 | `{ "error": "invalid request body" }` |

### Frontend Validation

- The `StatusSelect` dropdown only offers the four valid options, so invalid values cannot be submitted from the UI
- Error handling is purely for API failures (network errors, race conditions, server errors)
- On API error, the dropdown reverts to its previous value and an inline error message is shown

### Edge Cases

- **Concurrent updates:** No optimistic locking for this story. If two users update the same order simultaneously, last write wins. This matches legacy behavior.
- **Same-status update:** Allowed — if the user selects the current status again, the API still processes it and creates an audit log entry. This matches legacy behavior (the legacy code doesn't guard against no-op updates).
- **Order ID type:** The path parameter `id` is a `Long`. Non-numeric IDs will be rejected by Spring's type conversion with a 400 error.
