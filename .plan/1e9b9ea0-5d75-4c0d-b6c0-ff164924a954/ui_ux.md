# UI/UX

## UI/UX: Orders Page

### Layout
Card list layout matching the legacy app's visual pattern, built with Tailwind CSS and the legacy color palette (`--brand: #245f4f`, `--accent: #d36b37`, `--bg: #f6f3ed`, `--surface: #fffaf0`).

### Page Structure
1. **Top nav bar** — sticky, with "OrderDesk" brand mark and navigation links (Dashboard, Orders, Customers, Products). "Orders" highlighted as active.
2. **Page header** — title "Orders" with subtitle, and the status filter dropdown aligned right.
3. **Card list** — vertical stack of order cards, one per order, 3px gap.
4. **Empty state** — centered message with icon when no orders match the filter.

### Order Card Anatomy
Each card is a rounded white container (`rounded-2xl`, `border-line`) showing:
- **Row 1:** Order ID (`#3`), customer name, color-coded status badge, and total amount in USD (right-aligned, brand color, bold)
- **Row 2:** Customer email and formatted date/time in muted text, dot-separated
- **Row 3 (conditional):** Notes in a muted inset block, only rendered if notes exist

### Status Badges
Color-coded pill badges per status — visually distinct at a glance:
- **New** — blue background, blue text
- **Paid** — emerald/green background, green text
- **Shipped** — violet/purple background, purple text
- **Cancelled** — red background, red text

### Filter Interaction
- `<select>` dropdown in the page header with 5 options: "All statuses", "New", "Paid", "Shipped", "Cancelled"
- Selecting a status triggers a new API fetch with `?status=` query param (server-side filtering)
- Selecting "All statuses" fetches without the query param
- Loading state: brief shimmer or "Loading..." text while fetching

### Currency Formatting
- `totalCents` from the API (integer) divided by 100 and formatted via `Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' })`
- Displayed with tabular-nums for aligned digits

### Responsive Behavior
- Single column layout below 640px
- Card content stacks gracefully — total moves below the name/status row on narrow screens
