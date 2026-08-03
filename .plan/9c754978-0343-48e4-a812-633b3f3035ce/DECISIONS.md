# Locked Decisions for Story 9c754978-0343-48e4-a812-633b3f3035ce

## Implementation Approach
## Implementation Approach: Read-Only Revenue Aggregation

### API Endpoint
`GET /api/reports/revenue` — matches the legacy contract exactly.

**Response shape** (unchanged from legacy):
```json
{
  "totalCents": 85992,
  "byStatus": [
    { "status": "new", "orderCount": 1, "totalCents": 4599 },
    { "status": "paid", "orderCount": 1, "totalCents": 8895 },
    { "status": "shipped", "orderCount": 1, "totalCents": 85992 }
  ]
}
```

### Backend Layers

**Repository** — Custom JPQL query on `OrderItem` entity, grouped by order status:
```java
@Query("SELECT o.status, COUNT(DISTINCT o.id), SUM(oi.quantity * oi.unitPriceCents) " +
       "FROM OrderItem oi JOIN oi.order o GROUP BY o.status ORDER BY o.status")
```
This replaces the legacy `order_totals` SQL view with an in-application query. The aggregation is simple enough that a database view is unnecessary — a single JPQL query keeps all logic in the Spring layer and avoids an extra Flyway migration artifact.

**Service** — `ReportService.getRevenueReport()`:
- Calls the repository query
- Maps raw results into `RevenueByStatusDto` records
- Computes `totalCents` by summing the per-status subtotals
- Returns a `RevenueReportDto`

**Controller** — `ReportController`:
- `@GetMapping("/api/reports/revenue")` returns the DTO directly
- No request parameters, no pagination — single aggregate response

**DTOs** (Java records):
- `RevenueReportDto(long totalCents, List<RevenueByStatusDto> byStatus)`
- `RevenueByStatusDto(String status, int orderCount, long totalCents)`

### Frontend
- On the dashboard page, a `useEffect` fetches `GET /api/reports/revenue` on mount
- Divides `totalCents` by 100 and formats with `Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })` — matching the legacy `cents()` helper exactly
- Displays the formatted total in a summary card component

### Key Design Choices
- **JPQL over database view**: Keeps aggregation logic co-located with the service layer. The query is a single join + group-by — no performance concern at this data scale.
- **Integer cents throughout**: `totalCents` stays as a `long` (integer cents) in the API response. Cents-to-dollars conversion happens exclusively on the frontend, preserving precision.
- **No caching**: The revenue report is a simple aggregate over a small dataset. No caching layer needed.

## UI/UX
## UI/UX: Revenue Stat Card on Dashboard

### Component
A single **stat card** placed at the top of the main dashboard page (above the Orders and Customers panels), matching the legacy layout.

### Card Design
- White card with rounded corners, subtle border, and light shadow (`rounded-xl shadow-sm border`)
- Small dollar icon + "Total Revenue" label in muted text above the value
- Large bold number displaying the formatted USD amount (e.g., **$859.92**)
- Max-width constrained (`max-w-xs`) so it doesn't stretch across the full page

### Currency Formatting
Uses `Intl.NumberFormat` on the frontend, matching the legacy `cents()` helper exactly:
```typescript
const formatCurrency = (cents: number): string =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })
    .format(cents / 100);
```

### Data Flow
1. Dashboard page mounts → `useEffect` calls `fetch('/api/reports/revenue')`
2. Response provides `totalCents` as an integer
3. Frontend divides by 100 and formats with `Intl.NumberFormat`
4. Card displays the result; shows "Loading..." placeholder until the fetch resolves

### Loading & Error States
- **Loading**: Card shows a subtle shimmer/pulse placeholder while fetching
- **Error**: Card shows "$—" with a small "Failed to load" message in muted text beneath

### Layout Context
The revenue card sits at the top of the dashboard, above the two-column grid of Orders and Customers panels. It's a standalone summary element — no interaction, no drill-down, no click handlers.
Artifacts: `artifacts/revenue_dashboard_card.html`
