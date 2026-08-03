# Validation

## Validation: Customer Registration Rules

### Backend Validation (Spring Boot)

**Request DTO — `CreateCustomerRequest`** (Java record with Jakarta Bean Validation):

| Field | Annotation | Rule | Error Message |
|-------|-----------|------|---------------|
| `name` | `@NotBlank` | Required, non-empty after trim | `"name is required"` |
| `email` | `@NotBlank`, `@Email` | Required, valid email format | `"email is required"` / `"must be a valid email address"` |
| `tier` | (nullable) | Optional — defaults to `"standard"` if null/absent | — |

**Business Rules (Service Layer)**:

| Rule | Check | HTTP Response |
|------|-------|---------------|
| Duplicate email | `customerRepository.existsByEmail(email)` | `409 Conflict` — `{ "error": "A customer with this email already exists" }` |
| Invalid tier value | Java enum deserialization fails OR explicit check | `400 Bad Request` — `{ "error": "tier must be one of: standard, premium, enterprise" }` |

**Database Constraints (Defense in Depth)**:
- `UNIQUE(email)` — catches any race condition the service-layer check misses
- `CHECK(tier IN ('standard', 'premium', 'enterprise'))` — final guard on tier values
- `NOT NULL` on name, email, tier, created_at

### Frontend Validation (Next.js)

**Client-side (immediate feedback)**:
- Name field: `required` attribute — browser prevents empty submission
- Email field: `required` + `type="email"` — browser checks basic format
- Tier field: pre-selected to "Standard" — cannot be blank

**Server error display**:
- On `400`/`409` response, parse the JSON `error` field and display it as an inline error message above the form
- Clear the error on the next form input change
- Do NOT duplicate complex business validation (like email uniqueness) on the client — let the backend be the source of truth

### Error Response Format

All validation errors return a consistent JSON shape:
```json
{
  "error": "human-readable message"
}
```

This matches the legacy API's error format (`res.status(400).json({ error: '...' })`), keeping the frontend integration simple.

### Edge Cases
- **Whitespace-only name**: `@NotBlank` rejects it (unlike `@NotNull` which would allow it)
- **Case sensitivity on email**: Store emails as-is (no lowercasing) — matches legacy behavior. The unique constraint is case-sensitive by default in PostgreSQL; this is acceptable for an internal tool
- **Leading/trailing whitespace on email**: Trim in the service layer before persistence to avoid accidental duplicates like `"ava@example.com"` vs `"ava@example.com "`
