# DESIGN.md

## Framework & Architecture
- Use Next.js with TypeScript for the frontend application.
- Create a dedicated API client layer for all communication with the Spring Boot REST backend.
- Do NOT make direct API calls from UI components; all requests must go through the API client.
- Focus implementation on the following screens: Dashboard/Revenue, Customers, Products, Orders, and Audit.

## Styling & Layout
- Implement a lightweight, responsive design that works on laptop and narrow screen widths.
- Keep the UI business-facing and polished. Avoid unstyled, default browser elements.
- Use a consistent styling approach (e.g., CSS Modules, Tailwind CSS). Define a limited set of reusable styles.
- Do NOT use inline styles.
- Do NOT introduce custom one-off colors or spacing values. Establish a simple scale and adhere to it.

## Components
- Build simple, reusable components for common UI patterns.

### Badges
- Use status badges to visually distinguish order statuses.
- Each status (e.g., "Shipped", "Pending", "Cancelled") must have a distinct, consistent color and style.

### Forms
- Build reusable form components for inputs, selects, and buttons.
- Forms must clearly display backend validation errors directly associated with the invalid fields.

### Tables
- Use clean, readable tables for displaying lists of data (e.g., orders, customers).
- Ensure tables are responsive and readable on smaller screens.

### Cards
- Use summary cards on the dashboard to display key metrics and revenue figures.

### Filters
- Implement reusable filter components for tables and data lists.