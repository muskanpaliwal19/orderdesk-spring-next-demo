# Validation

## Validation: Input Rules & Edge Cases

### Request-Level Validation (Jakarta Bean Validation on DTOs)
| Field | Rule | Annotation |
|-------|------|-----------|
| `customerId` | Required, non-null | `@NotNull` |
| `items` | Required, at least 1 element | `@NotEmpty` |
| `items[].productId` | Required, non-null | `@NotNull` |
| `items[].quantity` | Required, ≥ 1 | `@NotNull @Min(1)` |
| `notes` | Optional, nullable | No annotation needed |

Jakarta validation failures return `400` with a structured error body before the service layer is reached.

### Business Rule Validation (in OrderService)
| Rule | Behavior |
|------|----------|
| Customer ID doesn't match any row | Return `404` — "Customer not found" |
| Product ID doesn't exist | Skip that line item silently |
| Product exists but `active = false` | Skip that line item silently |
| All submitted items were skipped (zero valid) | Return `400` — "No valid active products found in the submitted items" |
| Duplicate product IDs in items list | Each entry is processed independently (two line items for same product = two rows in `order_items`). This matches legacy behavior. |

### Edge Cases
| Scenario | Handling |
|----------|---------|
| `quantity = 0` or negative | Rejected by `@Min(1)` at the DTO level |
| `items` is `null` | Rejected by `@NotEmpty` |
| `items` is `[]` (empty array) | Rejected by `@NotEmpty` |
| Request body missing entirely | Spring returns `400` (HttpMessageNotReadableException) |
| `notes` is `null` | Stored as null — order created without notes |
| Very large quantity (e.g., 999999) | Allowed — no business cap specified. The `INTEGER` column handles up to ~2.1B |
| `customerId` is valid but customer was soft-deleted | Not applicable — customers table has no soft-delete column in the schema |

### Error Response Format
All validation errors use a consistent shape:
```json
{ "error": "Human-readable message describing what went wrong" }
```
This matches the legacy pattern (`res.status(400).json({ error: '...' })`).

