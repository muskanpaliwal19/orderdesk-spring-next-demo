# DESIGN.md

## Core Technology
- **Framework**: Use Next.js with TypeScript.
- **Scope**: Build only the essential operational screens: dashboard/revenue, customers, products, orders, and audit visibility.
- **Architecture**: Implement a dedicated API client layer to communicate with the Spring Boot REST backend.

## Styling & Layout
- **Responsiveness**: Ensure the UI is responsive and functional on both laptop and narrow screen sizes.
- **System**: Implement a lightweight styling system. Prioritize clarity and function for a business-facing application.
- **Consistency**: Use a consistent system for colors, spacing, and typography. Do NOT use inline styles or introduce one-off styling values.

## Components
- Build the following as simple, reusable components:
    - Data Tables
    - Forms
    - Filter controls
    - Status Badges
    - Summary Cards

### Component-Specific Rules
- **Status Badges**: Use visually distinct colors and styles for different order statuses to ensure they are easily distinguishable at a glance.
- **Forms**: Design forms to clearly display backend validation errors to the user.
- **Focus**: Ensure components feel integrated into a cohesive, business-focused application, not like a generic CRUD scaffold.