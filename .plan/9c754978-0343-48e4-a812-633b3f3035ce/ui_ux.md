# UI/UX

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
