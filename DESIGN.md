# DESIGN.md

## Framework & Architecture
-   Build the application using Next.js and TypeScript.
-   Create a dedicated API client layer for communicating with the Spring Boot REST endpoints.
-   Ensure the UI remains business-facing and focused on operational workflows.

## Visual System
-   Font: Use a system sans-serif font stack (e.g., `system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif`).
-   Colors: Use CSS variables for the entire palette. Do NOT use hex codes directly in components.
    -   `--color-primary`: `#2563eb` (Primary Blue)
    -   `--color-surface`: `#ffffff`
    -   `--color-surface-muted`: `#f8fafc`
    -   `--color-border`: `#e2e8f0`
    -   `--color-text-primary`: `#1e293b`
    -   `--color-text-secondary`: `#64748b`
    -   `--status-success-bg`: `#dcfce7` (Green)
    -   `--status-success-text`: `#166534`
    -   `--status-warning-bg`: `#fef9c3` (Amber)
    -   `--status-warning-text`: `#854d0e`
    -   `--status-info-bg`: `#e0f2fe` (Blue)
    -   `--status-info-text`: `#075985`

## Spacing & Layout
-   Use a 4px grid (0.25rem) for all spacing, padding, margins, and layout.
-   Use a consistent spacing scale (e.g., `4px`, `8px`, `12px`, `16px`, `24px`, `32px`).
-   Do NOT use arbitrary values for spacing.
-   Ensure layouts are responsive and function correctly on both laptop and narrow mobile screens.

## Components
-   Build simple, reusable components for common patterns. Do NOT install a large component library.
-   **Tables:** Use clean, readable table styles with clear headers (`background-color: var(--color-surface-muted)`) and row separation (`border-bottom: 1px solid var(--color-border)`).
-   **Forms:**
    -   Inputs must have a consistent style.
    -   Clearly display backend validation errors near the corresponding form field.
-   **Status Badges:**
    -   Create a `Badge` component that accepts a status prop (e.g., 'success', 'warning').
    -   Style badges as small pills with `border-radius: 9999px;`, `padding: 2px 8px;`, `font-size: 12px;`, and `font-weight: 500;`.
    -   Use the `--status-*` CSS variables for background and text colors to make statuses visually distinct.
-   **Summary Cards:**
    -   Use for dashboard metrics.
    -   Apply `padding: 16px;`, `border: 1px solid var(--color-border);`, and `border-radius: 8px;`.
-   **Filters:** Create reusable filter controls (e.g., dropdowns, text inputs) for data tables.