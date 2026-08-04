# DESIGN.md

## Core Technology
- Framework: Next.js with TypeScript.
- Styling: Use Tailwind CSS for a utility-first, responsive approach.
- Data Fetching: Create a dedicated API client layer to interact with the Spring Boot REST API. Do NOT fetch data directly within components.

## Visual System
- Font: Use the default system sans-serif font stack (`font-sans`). It's lightweight and provides a native feel.
- Colors: Use a limited, professional color palette from the default Tailwind CSS configuration.
    - Primary/Action: `blue-600` (#2563eb)
    - Surface: `white` (#ffffff), `gray-50` (#f9fafb) for page backgrounds.
    - Text: `gray-900` (#11182c) for headings, `gray-700` (#374151) for body copy.
    - Borders: `gray-200` (#e5e7eb)
- Do NOT introduce custom color names or hex values. Stick to the default Tailwind palette.

## Spacing & Layout
- Use the default Tailwind spacing scale (multiples of `0.25rem`).
- Common patterns: `p-4`, `px-6`, `gap-4`, `mb-4`.
- Layouts MUST be responsive and functional on laptops and narrow mobile screens. Use Flexbox and Grid (`flex`, `grid`, `gap-*`).
- Do NOT use arbitrary values (e.g., `top: 13px`) for spacing or layout.

## Components
- Build simple, reusable components for common UI patterns using React and Tailwind CSS.
- Do NOT use a third-party component library like shadcn/ui or Material UI.

### Badges
- Use badges with rounded corners (`rounded-full`) and padding (`px-2.5 py-0.5`) to indicate status.
- Success/Completed: `bg-green-100 text-green-800`
- Pending/In Progress: `bg-yellow-100 text-yellow-800`
- Error/Cancelled: `bg-red-100 text-red-800`
- Default/Info: `bg-gray-100 text-gray-800`

### Forms
- Clearly display backend validation errors below the corresponding input field.
- Use `text-red-600` and `text-sm` for validation messages.
- Use `ring-1 ring-red-300` on inputs with errors.

### Tables
- Style tables for readability with `w-full` and `text-left`.
- Use `border-b` on `<tr>` elements for row separation.
- Use `p-4` for cell padding (`<th>` and `<td>`).

### Summary Cards
- Use cards to display summary information (e.g., revenue, customer count).
- Style cards with `bg-white`, `border`, `rounded-lg`, and `shadow-sm`.
- Use `p-6` for internal padding.