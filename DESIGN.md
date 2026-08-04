# DESIGN.md

## Framework & Architecture
- **Framework**: Use Next.js with TypeScript.
- **API Client**: Implement a dedicated client layer to call Spring Boot REST endpoints.
- **Scope**: Build only the operational screens: dashboard/revenue, customers, products, orders, and audit visibility.

## Styling & Layout
- **Responsiveness**: Ensure the UI is lightweight and responsive, functioning on both laptop and narrow screen sizes.
- **Aesthetic**: The UI must be clean and business-facing. Do NOT create a generic CRUD scaffold.

## Components
- **Strategy**: Build simple, reusable components. Do NOT add a complex external component library.
- **Tables**: Use for displaying lists of customers, products, and orders.
- **Forms**: Components must clearly display backend validation errors to the user.
- **Status Badges**: Use visually distinguishable badges to represent different order statuses.
- **Summary Cards**: Use for displaying key metrics on the dashboard (e.g., revenue).
- **Filters**: Implement filter components for data tables.