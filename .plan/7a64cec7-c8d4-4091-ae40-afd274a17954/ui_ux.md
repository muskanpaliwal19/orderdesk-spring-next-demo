# UI/UX

## UI/UX: Export Button on Orders Page

### Placement
- Add an **"Export CSV"** button in the orders page header area, next to the existing status filter dropdown
- Button uses a download/export icon (↓ or document icon) alongside the text label
- Positioned to the right of the filter controls so it doesn't disrupt the existing filter workflow

### Behavior
- Clicking the button navigates the browser to `/api/orders/export` (or `/api/orders/export?status=X` if a filter is active), triggering a native file download
- The button is always enabled — if no orders match, the user receives a valid CSV with only the header row
- No loading spinner needed since the browser handles the download natively in the background
- The current page state is preserved — the user stays on the orders page after clicking

### Button Design
- Secondary/outlined style to distinguish it from primary actions (like creating an order)
- Small/compact size consistent with the filter controls
- Label: **"Export CSV"** — concise and unambiguous

### No Confirmation Dialog
- Export is a read-only, non-destructive action — no confirmation needed
- Keeps the interaction fast (single click → download starts)
