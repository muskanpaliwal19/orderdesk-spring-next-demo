# DESIGN.md

## Core Technology
- Build the frontend application using Next.js and TypeScript.
- Create a dedicated API client layer to handle all communication with the Spring Boot REST endpoints.

## Styling & Layout
- Implement a responsive layout that works on both laptop and narrow screen widths.
- Maintain a lightweight, clean, and business-facing aesthetic suitable for data-dense operational screens.
- Do NOT use a generic or unstyled CRUD scaffold look.

## Components
- Create simple, reusable components for common UI patterns. Do NOT create one-off styles for single-use cases.

### Core Component Patterns
- **Data Tables:** For displaying lists of customers, products, orders, etc.
- **Forms:** For creating and editing data.
- **Filter Controls:** For refining data displayed in tables.
- **Status Badges:** To visually indicate the status of an order.
- **Summary Cards:** For displaying key metrics on the dashboard.

### Component-Specific Rules
- **Forms:** Must clearly display validation errors returned from the backend API.
- **Status Badges:** Use distinct colors to make different order statuses immediately distinguishable.

## Application Scope
- Focus UI development on the following core screens:
    - Dashboard/Revenue
    - Customers
    - Products
    - Orders
    - Audit Visibility
- Do NOT build features or screens outside this defined scope.