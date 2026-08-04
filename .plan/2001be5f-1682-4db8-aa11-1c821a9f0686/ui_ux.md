# UI/UX

## UI/UX: Product Catalog Table

### Page Layout
The product catalog lives at the `/products` route in the sidebar navigation. It follows the app-wide layout: left sidebar nav + main content area.

### Design
- **Simple data table** inside a rounded card container, matching the legacy app's warm surface palette (`#fffaf0` surface, `#e1d7c7` borders, `#245f4f` brand green).
- **Three columns:** Product Name (left-aligned, bold), SKU (left-aligned, monospace code badge), Unit Price (right-aligned, brand green, dollar-formatted).
- **Row hover:** Subtle green tint on hover for scannability.
- **No pagination** — the product catalog is small (4 seed products); a simple list is sufficient.

### Column Details
| Column | Alignment | Formatting |
|--------|-----------|------------|
| Product Name | Left | Medium weight text |
| SKU | Left | Monospace `<code>` badge with light background |
| Unit Price | Right | `$XX.XX` via `Intl.NumberFormat` (cents ÷ 100), bold brand green |

### States
1. **Loading:** Skeleton rows or a centered spinner while `GET /api/products` is in flight.
2. **Populated:** Table rows sorted A→Z by name with a count label below ("Showing N active products").
3. **Empty:** A centered empty state with a package icon, "No active products" heading, and a short explanation. Handles the edge case where all products have been deactivated.
4. **Error:** Simple error message with retry option if the API call fails.

### Navigation
- Sidebar nav with pages: Dashboard, Customers, **Products** (active), Orders, Audit Log.
- "Products" is highlighted as the active page when viewing this route.

### Visual Consistency
- Colors, border radius, and shadows match the legacy app's warm design language (cream backgrounds, rounded corners, subtle amber shadows).
- Typography uses Inter with the same hierarchy as other pages.

### Responsive
- On narrow screens, the sidebar collapses or the table scrolls horizontally.
- All three columns are compact enough to remain readable at small widths.
