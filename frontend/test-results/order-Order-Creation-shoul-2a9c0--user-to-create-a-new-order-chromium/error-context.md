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
  - banner [ref=e2]:
    - navigation [ref=e3]:
      - link "Home" [ref=e4] [cursor=pointer]:
        - /url: /
      - link "Quotes" [ref=e5] [cursor=pointer]:
        - /url: /quotes
  - main [ref=e6]:
    - heading "Quotes" [level=1] [ref=e7]
    - generic [ref=e8]:
      - link "New Quote" [ref=e10] [cursor=pointer]:
        - /url: /quotes/new
      - table [ref=e11]:
        - rowgroup [ref=e12]:
          - row [ref=e13]:
            - columnheader "Customer" [ref=e14]
            - columnheader "Amount" [ref=e15]
            - columnheader "Status" [ref=e16]
            - columnheader "Actions" [ref=e17]
        - rowgroup [ref=e18]:
          - row [ref=e19]:
            - cell "Acme Corp" [ref=e20]
            - cell "1200.50" [ref=e21]
            - cell "Draft" [ref=e22]
            - cell [ref=e23]:
              - link "View" [ref=e24] [cursor=pointer]:
                - /url: /quotes/1
              - link "Edit" [ref=e25] [cursor=pointer]:
                - /url: /quotes/1/edit
          - row [ref=e26]:
            - cell "Stark Industries" [ref=e27]
            - cell "5500.00" [ref=e28]
            - cell "Sent" [ref=e29]
            - cell [ref=e30]:
              - link "View" [ref=e31] [cursor=pointer]:
                - /url: /quotes/2
              - link "Edit" [ref=e32] [cursor=pointer]:
                - /url: /quotes/2/edit
          - row [ref=e33]:
            - cell "Wayne Enterprises" [ref=e34]
            - cell "2300.75" [ref=e35]
            - cell "Accepted" [ref=e36]
            - cell [ref=e37]:
              - link "View" [ref=e38] [cursor=pointer]:
                - /url: /quotes/3
              - link "Edit" [ref=e39] [cursor=pointer]:
                - /url: /quotes/3/edit
  - region "Notifications alt+T"
  - button "Open Next.js Dev Tools" [ref=e45] [cursor=pointer]
  - alert [ref=e49]
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