# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: revenue.spec.ts >> Revenue Report >> should display the grand total revenue in USD format
- Location: tests/revenue.spec.ts:5:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('div').filter({ has: locator('span:has-text("Total Revenue")') })
Expected: visible
Timeout: 5000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('div').filter({ has: locator('span:has-text("Total Revenue")') })

```

```yaml
- banner:
  - navigation:
    - link "Home":
      - /url: /
    - link "Quotes":
      - /url: /quotes
- main:
  - heading "Quotes" [level=1]
  - link "New Quote":
    - /url: /quotes/new
  - table:
    - rowgroup:
      - row "Customer Amount Status Actions":
        - columnheader "Customer"
        - columnheader "Amount"
        - columnheader "Status"
        - columnheader "Actions"
    - rowgroup:
      - row "Acme Corp 1200.50 Draft ViewEdit":
        - cell "Acme Corp"
        - cell "1200.50"
        - cell "Draft"
        - cell "ViewEdit":
          - link "View":
            - /url: /quotes/1
          - link "Edit":
            - /url: /quotes/1/edit
      - row "Stark Industries 5500.00 Sent ViewEdit":
        - cell "Stark Industries"
        - cell "5500.00"
        - cell "Sent"
        - cell "ViewEdit":
          - link "View":
            - /url: /quotes/2
          - link "Edit":
            - /url: /quotes/2/edit
      - row "Wayne Enterprises 2300.75 Accepted ViewEdit":
        - cell "Wayne Enterprises"
        - cell "2300.75"
        - cell "Accepted"
        - cell "ViewEdit":
          - link "View":
            - /url: /quotes/3
          - link "Edit":
            - /url: /quotes/3/edit
- region "Notifications alt+T"
- alert
```

# Test source

```ts
  1  | 
  2  | import { test, expect } from '@playwright/test';
  3  | 
  4  | test.describe('Revenue Report', () => {
  5  |   test('should display the grand total revenue in USD format', async ({ page }) => {
  6  |     // Mock the API response
  7  |     await page.route('/api/reports/revenue', route => {
  8  |       route.fulfill({
  9  |         status: 200,
  10 |         contentType: 'application/json',
  11 |         body: JSON.stringify({
  12 |           totalRevenue: 123456, // 1234.56
  13 |           revenueByDate: [],
  14 |           revenueByStatus: [],
  15 |         }),
  16 |       });
  17 |     });
  18 | 
  19 |     await page.goto('/');
  20 | 
  21 |     // Check for the "Total Revenue" card
  22 |     const revenueCard = page.locator('div', { has: page.locator('span:has-text("Total Revenue")') });
> 23 |     await expect(revenueCard).toBeVisible();
     |                               ^ Error: expect(locator).toBeVisible() failed
  24 | 
  25 |     // Check for the correctly formatted currency
  26 |     const revenueValue = revenueCard.locator('p.text-3xl');
  27 |     await expect(revenueValue).toHaveText('$1,234.56');
  28 |   });
  29 | });
  30 | 
```