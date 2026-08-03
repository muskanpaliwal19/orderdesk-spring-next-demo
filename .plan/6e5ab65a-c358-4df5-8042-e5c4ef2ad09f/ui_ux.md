# UI/UX

## UI/UX: Dedicated Customers Page

### Layout

**App shell**: Sidebar navigation (fixed left, ~224px) + scrollable main content area. This establishes the navigation pattern for all stories.

**Sidebar nav items** (sets the pattern for the whole app):
- Dashboard (future)
- **Customers** (this story — active/highlighted)
- Products (future)
- Orders (future)
- Revenue (future)

**Customers page** (`/customers`) has two stacked cards:

1. **Registration form card** — "Register New Customer" header, 3-column grid on desktop (name, email, tier select), "Add Customer" button aligned right
2. **Customer list card** — "All Customers" header with count badge, full-width table with columns: Name, Email, Tier (badge), Registered (formatted date)

### Component Breakdown

| Component | Description |
|-----------|-------------|
| `AppLayout` | Sidebar + main content shell — reused by every page |
| `Sidebar` / `NavLink` | Navigation with active state highlighting |
| `CustomerForm` | Controlled form with name, email, tier inputs + submit |
| `CustomerTable` | Table displaying customer list with tier badges |
| `TierBadge` | Small colored badge — gray for Standard, amber for Premium, purple for Enterprise |
| `ErrorBanner` | Dismissible red banner above the form for backend validation errors |

### Interaction Flow

1. Page loads → `useEffect` fetches `GET /api/customers` → populates table
2. User fills form → clicks "Add Customer" → `POST /api/customers`
3. **Success (201)**: form resets, customer list re-fetches, new customer appears at top
4. **Error (400/409)**: red error banner appears above the form with the backend's error message; clears on next input change
5. Tier defaults to "Standard" (pre-selected in dropdown) — user can change it

### Visual Design

- **Colors**: Blue brand palette (600 primary, 50 for active nav backgrounds), slate/gray surface tones
- **Cards**: White background, rounded-xl, subtle border + shadow-sm
- **Table rows**: Hover highlight, comfortable padding (py-3.5)
- **Tier badges**: Distinct colors per tier — gray-100/gray-700 (Standard), amber-50/amber-700 (Premium), purple-50/purple-700 (Enterprise)
- **Typography**: Inter/system font stack, 2xl bold page title, xs uppercase table headers
- **Transitions**: 150ms on all interactive elements

### Responsive Behavior
- Form grid collapses from 3 columns to 1 on small screens (`grid-cols-1 sm:grid-cols-3`)
- Sidebar remains fixed (acceptable for internal desktop tool)
- Table scrolls horizontally if needed on very narrow viewports
