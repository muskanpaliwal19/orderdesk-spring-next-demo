# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order.spec.ts >> Order Creation >> should allow a user to create a new order
- Location: tests/order.spec.ts:5:7

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: locator.click: Test timeout of 30000ms exceeded.
Call log:
  - waiting for getByRole('button', { name: 'New Order' })

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
              - option "Standard" [selected]
              - option "Premium"
              - option "Enterprise"
          - button "Add Customer" [ref=e24] [cursor=pointer]
        - table [ref=e26]:
          - rowgroup [ref=e27]:
            - row [ref=e28]:
              - columnheader "Name" [ref=e29]
              - columnheader "Email" [ref=e30]
              - columnheader "Tier" [ref=e31]
          - rowgroup [ref=e32]:
            - row [ref=e33]:
              - cell "Test Customer" [ref=e34]
              - cell "test@example.com" [ref=e35]
              - cell "STANDARD" [ref=e36]
  - region "Notifications alt+T"
  - button "Open Next.js Dev Tools" [ref=e42] [cursor=pointer]
  - alert [ref=e46]
```

# Test source

```ts
  1  | 
  2  | import { test, expect } from '@playwright/test';
  3  | 
  4  | test.describe('Order Creation', () => {
  5  |   test('should allow a user to create a new order', async ({ page }) => {
  6  |     await page.goto('/');
  7  | 
  8  |     // Click the 'New Order' button to open the slide panel
> 9  |     await page.getByRole('button', { name: 'New Order' }).click();
     |                                                           ^ Error: locator.click: Test timeout of 30000ms exceeded.
  10 | 
  11 |     // Wait for the panel to be visible
  12 |     await expect(page.getByRole('heading', { name: 'New Order' })).toBeVisible();
  13 | 
  14 |     // Select a customer
  15 |     await page.locator('#customerSelect').selectOption({ index: 1 });
  16 | 
  17 |     // Wait for products to load - check for product selector
  18 |     const productSelector = page.getByRole('combobox').nth(1);
  19 |     await expect(productSelector).toBeVisible();
  20 | 
  21 |     // Select a product in the first line item
  22 |     await productSelector.selectOption({ index: 1 });
  23 | 
  24 |     // Enter quantity
  25 |     await page.getByRole('spinbutton').fill('2');
  26 | 
  27 |     // Click the 'Create Order' button
  28 |     await page.getByRole('button', { name: 'Create Order' }).click();
  29 | 
  30 |     // Check for the success toast message
  31 |     await expect(page.getByText('Order created successfully!')).toBeVisible();
  32 | 
  33 |     // The panel should close upon successful order creation
  34 |     await expect(page.getByRole('heading', { name: 'New Order' })).not.toBeVisible();
  35 |   });
  36 | });
  37 | 
```