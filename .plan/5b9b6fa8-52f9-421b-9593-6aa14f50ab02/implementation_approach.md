# Implementation Approach

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
