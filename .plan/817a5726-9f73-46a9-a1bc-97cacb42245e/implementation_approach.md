# Implementation Approach

## Implementation Approach: Transactional Order Creation with Audit Logging

### Service Layer Pattern
The `OrderService.createOrder()` method handles the full workflow in a single `@Transactional` method:

1. **Validate inputs** — reject if `customerId` is null/missing or `items` is empty
2. **Verify customer** — load customer by ID; throw `ResponseStatusException(404)` if not found
3. **Create order** — persist `Order` entity with status `'new'`, current timestamp, and optional notes
4. **Process line items** — iterate submitted items:
   - Look up each product by ID where `active = true`
   - Skip silently if product not found or inactive
   - Snapshot `unitPriceCents` from the product into the `OrderItem`
5. **Post-validation** — if zero valid items survived filtering, roll back and return 400 error
6. **Audit log** — insert `AuditLog` with `entityType='order'`, `eventType='created'`, and a message
7. **Return full order** — respond with the created order including its accepted line items and a list of any skipped product IDs

### Key Design Choices
- **Single transaction** — order, items, and audit log are written atomically via `@Transactional`
- **Audit logging inline** — the audit log insert lives in `OrderService`, not in a separate event listener. This keeps it simple and transactional (no risk of the audit log missing if a listener fails)
- **Price snapshot at creation** — `unitPriceCents` is copied from `products.unit_price_cents` into `order_items.unit_price_cents` at insert time, never referenced live
- **No cascading deletes** — orders are never deleted, only status-transitioned

### Layer Responsibilities
| Layer | Class | Responsibility |
|-------|-------|---------------|
| Controller | `OrderController` | Parse request body, validate via `@Valid`, call service, shape response |
| Service | `OrderService` | Business logic: customer lookup, product filtering, price snapshot, audit |
| Repository | `OrderRepository`, `OrderItemRepository`, `AuditLogRepository` | Spring Data JPA interfaces |
| DTO | `CreateOrderRequest` (record), `OrderResponse` (record) | Request/response shapes |
| Entity | `Order`, `OrderItem`, `AuditLog` | JPA entities with Lombok |

