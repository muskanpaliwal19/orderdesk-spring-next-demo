# DESIGN.md

## Core Framework
- **Framework**: Next.js with TypeScript.
- **Styling**: Tailwind CSS for utility-first styling.
- **API**: Create a dedicated API client layer to handle all communication with the Spring Boot REST endpoints.

## Visual System
- **Font**: Use a system sans-serif font stack for performance and a native feel. `font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;`
- **Responsiveness**: All layouts and components must be responsive, targeting laptop and narrow screen widths.

### Colors
- Use CSS variables for all colors. Do NOT use hex codes directly in components.
- **Primary**: `#2563eb` (`--color-primary`)
- **Surface**: `#ffffff` (`--color-surface`)
- **Border**: `#e5e7eb` (`--color-border`)
- **Text Primary**: `#111827` (`--color-text-primary`)
- **Text Secondary**: `#6b7280` (`--color-text-secondary`)
- **Success**: `#16a34a` (`--color-success`) for successful statuses.
- **Warning**: `#f97316` (`--color-warning`) for pending statuses.
- **Error**: `#dc2626` (`--color-error`) for failed statuses or validation errors.

## Spacing & Layout
- **Scale**: Use the default Tailwind CSS spacing scale (multiples of `0.25rem`).
- **Layouts**: Use Flexbox and Grid for all layouts (`flex`, `grid`, `gap-*`).
- **Consistency**: Maintain consistent padding within containers. Use `p-4` or `p-6` for cards and page containers.

## Components
- Build all UI from simple, reusable components. Do NOT create monolithic page files.
- **Icons**: Use Heroicons, accessed via `@heroicons/react`.
- **Transitions**: Apply a `150ms` transition to all interactive elements (`transition-colors`, `duration-150`).

### Cards
- Use for summary information on the dashboard.
- **Class**: `.card`
- **Style**: White background (`bg-white`), rounded corners (`rounded-lg`), a subtle border (`border border-slate-200`), and light shadow (`shadow-sm`).
- **Padding**: `p-4` or `p-6`.

### Badges
- Use for order statuses. Badges must be visually distinct.
- **Base Style**: `px-2.5 py-0.5 rounded-full text-xs font-medium`.
- **Success**: `bg-green-100 text-green-800`.
- **Warning/Pending**: `bg-orange-100 text-orange-800`.
- **Error/Cancelled**: `bg-red-100 text-red-800`.

### Tables
- **Style**: Clean and simple. Use a `w-full` container.
- **Header**: `text-left text-sm font-semibold text-gray-900`.
- **Rows**: Use `border-b` for separation. Add a `hover:bg-gray-50` state for rows.
- **Cell Padding**: `px-3 py-4`.

### Forms
- **Layout**: Use labels stacked on top of inputs.
- **Inputs**: `block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm`.
- **Validation**: Display backend validation errors clearly. Show error messages below the corresponding input field in red (`text-red-600`).