# UI/UX

## UI/UX: Slide-Out Panel Order Creation on Orders Page

### Design System
Matches the legacy warm earthy palette:
- Brand green `#245f4f`, orange accent `#d36b37`, beige background `#f6f3ed`
- Font: Inter, border-radius: 28px panels / 18px items / 14px inputs
- Status badges: blue (new), green (paid), amber (shipped), red (cancelled)

### Order Creation Flow
1. **Trigger** — "New Order" button in the orders page toolbar (next to the status filter) opens a **slide-out panel from the right** with a blurred backdrop
2. **Customer selection** — dropdown populated from `GET /api/customers`, showing name, email, and tier
3. **Dynamic line items** — each row is a 3-column grid: product dropdown (name + formatted price), quantity input (`min=1`), and a remove button. An "Add item" dashed button appends new rows. At least one row is always visible.
4. **Notes** — optional textarea below the line items
5. **Running total** — a summary bar below the form shows the estimated total (computed client-side as `sum of quantity × unitPriceCents / 100` for selected products) with an item count. Labeled "Estimated total" since the server confirms the final price.
6. **Submit** — "Create Order" button POSTs to `/api/orders`, "Cancel" closes the panel

### Post-Submission Feedback
- **Success** — panel closes, a green banner slides down at the top of the orders list: "Order #N created successfully". The orders list refreshes to show the new order at the top. Banner auto-dismisses after 5 seconds.
- **Skipped items warning** — if `skippedProductIds` is non-empty, an orange warning banner appears alongside the success banner: "N item(s) skipped — [product names] are no longer active"
- **Validation error** — inline red error box within the panel (e.g., "Please select a customer"). Panel stays open so the user can fix the issue.
- **Server error** — inline error in panel showing the server's error message

### Components to Build
| Component | Purpose |
|-----------|---------|
| `OrderSlidePanel` | The slide-out panel container with backdrop, open/close state |
| `CreateOrderForm` | Form content: customer select, line items, notes, total, submit |
| `LineItemRow` | Single product-quantity row with remove button |
| `StatusBadge` | Reusable colored pill for order status (used across orders list too) |
| `AlertBanner` | Reusable success/warning/error banner with auto-dismiss |

### Responsive Behavior
- Panel is `min(520px, 100%)` — fills the screen on mobile
- Orders page grid collapses to single column below 800px (matches legacy breakpoint)

