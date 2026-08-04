# Implementation Approach

## Implementation Approach: Layered Read Endpoint + Service-Level Audit Writing

### Backend Layers

**JPA Entity** — `AuditLog` in `model/` with Lombok annotations (`@Entity`, `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`). Maps directly to the `audit_logs` table.

**Repository** — `AuditLogRepository` extends `JpaRepository<AuditLog, Long>`. Provides:
- `List<AuditLog> findTop50ByOrderByCreatedAtDesc()` — single derived query method satisfying the "most recent 50" requirement from AC #4

**Service** — `AuditLogService` with two responsibilities:
- `getRecentAuditLogs()` — delegates to the repository's top-50 query
- `logEvent(entityType, entityId, eventType, message)` — creates and persists a new audit entry. Called by `OrderService` during order creation (AC #2) and status updates (AC #3)

**Controller** — `AuditLogController` with a single endpoint:
- `GET /api/audit-logs` → returns `{ "auditLogs": [...] }`
- Matches the legacy response shape exactly

**DTO** — `AuditLogDto` as a Java record:
```java
public record AuditLogDto(
    Long id,
    String entityType,
    Long entityId,
    String eventType,
    String message,
    LocalDateTime createdAt
) {}
```

### Audit Entry Creation Pattern

Audit entries are written synchronously within the same transaction as the triggering operation — matching the legacy behavior where `createOrder` and `updateOrderStatus` append to the audit log inline. No event bus or async mechanism needed for this scale.

- **Order created** → `AuditLogService.logEvent("order", orderId, "created", "Order created from legacy UI")`
- **Status changed** → `AuditLogService.logEvent("order", orderId, "status_changed", "Order moved to {status}")`

### API Contract

| Method | Path | Response | Notes |
|--------|------|----------|-------|
| GET | `/api/audit-logs` | `{ "auditLogs": [...] }` | 50 most recent, descending by `created_at` |

No query parameters, no pagination cursor — the legacy endpoint has none, and the AC specifies a hard cap of 50. This keeps the contract identical to the legacy app.

### JSON Serialization

`createdAt` serialized as ISO-8601 string via Jackson's `JavaTimeModule` (e.g., `"2026-02-01T12:00:01"`). Field names use camelCase in the JSON response (`entityType`, `entityId`, `eventType`, `createdAt`) matching the legacy contract.
