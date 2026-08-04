# UI/UX

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
