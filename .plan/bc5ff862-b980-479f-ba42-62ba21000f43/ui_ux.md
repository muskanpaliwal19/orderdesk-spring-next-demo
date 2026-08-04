# UI/UX

## UI/UX: Dedicated Audit Trail Page

### Page Location
- Route: `/audit` in the Next.js app router
- Navigation: "Audit Trail" link in the top navigation bar alongside Dashboard, Orders, Customers, Products

### Layout
Standard full-width table view inside the app's shared layout (top nav + content area). No sidebar, no filters — the audit log is a simple read-only list.

### Table Columns (matching AC #1)

| Column | Source Field | Display |
|--------|-------------|---------|
| Timestamp | `createdAt` | Two-line: date on top (e.g., "Feb 4, 2026"), time below (e.g., "08:30:00") |
| Entity Type | `entityType` | Gray pill badge (e.g., "order") |
| Entity ID | `entityId` | Monospace with # prefix (e.g., "#3") |
| Event | `eventType` | Color-coded badge — green for `created`, blue for `status_changed` |
| Message | `message` | Plain text |

### Visual Design
- White card with subtle border and shadow containing the table
- Light gray header row with uppercase column labels
- Hover highlight on rows for scanability
- Entry count shown below the table ("Showing 3 of 3 entries")
- Responsive — table scrolls horizontally on narrow screens

### Data Fetching
- `'use client'` page component
- `useEffect` calls `fetch('/api/audit-logs')` on mount
- Loading state shows a simple "Loading..." text or skeleton
- Error state shows an inline error message

### No Interactivity Beyond Display
- No filtering, searching, or pagination controls — the API returns a fixed cap of 50 entries sorted by most recent, matching the legacy behavior exactly
- No click-through to order detail (can be added in a future story)

### Components
- **Page component**: `app/audit/page.tsx` — fetches data, renders table
- **Event badge**: Inline styled span with event-type-to-color mapping (small enough to be inline, not a separate component)
- Reuses whatever shared layout/nav component exists from other stories
