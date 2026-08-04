# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customers.spec.ts >> Customers Page >> should display a list of customers
- Location: tests/customers.spec.ts:10:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: getByText('VIP')
Expected: visible
Error: strict mode violation: getByText('VIP') resolved to 2 elements:
    1) <option value="VIP">VIP</option> aka getByTestId('tier-select')
    2) <span class="px-2 py-1 text-xs font-semibold rounded-full bg-purple-200 text-purple-800">VIP</span> aka getByRole('cell', { name: 'VIP' }).locator('span')

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for getByText('VIP')

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - generic [ref=e2]:
    - generic [ref=e3]:
      - heading "CRM" [level=2] [ref=e4]
      - navigation [ref=e5]:
        - list [ref=e6]:
          - listitem [ref=e7]:
            - link "Customers" [ref=e8] [cursor=pointer]:
              - /url: /customers
    - main [ref=e9]:
      - generic [ref=e10]:
        - heading "Customers" [level=1] [ref=e11]
        - generic [ref=e12]:
          - heading "Add New Customer" [level=2] [ref=e13]
          - generic [ref=e14]:
            - generic [ref=e15]: Name
            - textbox "Name" [ref=e16]
          - generic [ref=e17]:
            - generic [ref=e18]: Email
            - textbox "Email" [ref=e19]
          - generic [ref=e20]:
            - generic [ref=e21]: Tier
            - combobox "Tier" [ref=e22]:
              - option "Basic"
              - option "Standard" [selected]
              - option "Premium"
              - option "VIP"
          - button "Add Customer" [ref=e24] [cursor=pointer]
        - table [ref=e26]:
          - rowgroup [ref=e27]:
            - row [ref=e28]:
              - columnheader "Name" [ref=e29]
              - columnheader "Email" [ref=e30]
              - columnheader "Tier" [ref=e31]
          - rowgroup [ref=e32]:
            - row [ref=e33]:
              - cell "Alice Wonder" [ref=e34]
              - cell "alice@example.com" [ref=e35]
              - cell "VIP" [ref=e36]
            - row [ref=e37]:
              - cell "Bob Builder" [ref=e38]
              - cell "bob@example.com" [ref=e39]
              - cell "BASIC" [ref=e40]
  - region "Notifications alt+T"
  - button "Open Next.js Dev Tools" [ref=e46] [cursor=pointer]
  - alert [ref=e50]
```

# Test source

```ts
  1  | 
  2  | import { test, expect } from '@playwright/test';
  3  | 
  4  | const mockCustomers = [
  5  |   { id: 1, name: 'Alice Wonder', email: 'alice@example.com', tier: 'VIP' },
  6  |   { id: 2, name: 'Bob Builder', email: 'bob@example.com', tier: 'BASIC' },
  7  | ];
  8  | 
  9  | test.describe('Customers Page', () => {
  10 |   test('should display a list of customers', async ({ page }) => {
  11 |     // Mock the API response before navigating
  12 |     await page.route('**/api/customers', route => {
  13 |       route.fulfill({
  14 |         status: 200,
  15 |         contentType: 'application/json',
  16 |         body: JSON.stringify(mockCustomers),
  17 |       });
  18 |     });
  19 | 
  20 |     await page.goto('/customers');
  21 | 
  22 |     // Check for the heading
  23 |     await expect(page.getByRole('heading', { name: 'Customers' })).toBeVisible();
  24 | 
  25 |     // Check for table content from mock data
  26 |     await expect(page.getByText('Alice Wonder')).toBeVisible();
  27 |     await expect(page.getByText('alice@example.com')).toBeVisible();
> 28 |     await expect(page.getByText('VIP')).toBeVisible();
     |                                         ^ Error: expect(locator).toBeVisible() failed
  29 | 
  30 |     await expect(page.getByText('Bob Builder')).toBeVisible();
  31 |     await expect(page.getByText('bob@example.com')).toBeVisible();
  32 |     await expect(page.getByText('BASIC')).toBeVisible();
  33 |   });
  34 | });
  35 | 
```