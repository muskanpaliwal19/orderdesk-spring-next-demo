# Validation

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
