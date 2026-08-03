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
- generic:
  - generic [active]:
    - generic [ref=e3]:
      - generic [ref=e4]:
        - navigation [ref=e6]:
          - button [disabled] [ref=e7]:
            - img "previous" [ref=e8]
          - generic [ref=e10]:
            - generic [ref=e11]: 1/
            - text: "1"
          - button [disabled] [ref=e12]:
            - img "next" [ref=e13]
        - generic [ref=e16]:
          - generic "Latest available version is detected (16.2.12)." [ref=e19]: Next.js 16.2.12
          - generic [ref=e20]: Turbopack
      - dialog "Build Error" [ref=e22]:
        - generic [ref=e25]:
          - generic [ref=e26]:
            - generic [ref=e27]:
              - generic [ref=e28]: Build Error
              - generic [ref=e30]:
                - button "Copy Error Info" [ref=e31] [cursor=pointer]
                - link "Go to related documentation" [ref=e34] [cursor=pointer]:
                  - /url: https://nextjs.org/docs/messages/module-not-found
                - button "Attach Node.js inspector" [ref=e37] [cursor=pointer]
            - generic [ref=e46]: "Module not found: Can't resolve '../components/EmptyState'"
          - generic [ref=e49]:
            - generic [ref=e51]:
              - generic [ref=e56]: ./src/app/page.tsx (7:1)
              - button "Open in editor" [ref=e57] [cursor=pointer]
            - generic [ref=e62]:
              - generic [ref=e63]: "Module not found: Can't resolve '../components/EmptyState'"
              - generic [ref=e64]: 5 |
              - text: import
              - generic [ref=e65]: "{"
              - text: Order
              - generic [ref=e66]: "}"
              - text: from '@/types/order'
              - generic [ref=e67]: ;
              - generic [ref=e68]: 6 |
              - text: import OrderCard from '../components/OrderCard'
              - generic [ref=e69]: ;
              - text: ">"
              - generic [ref=e70]: 7 |
              - text: import EmptyState from '../components/EmptyState'
              - generic [ref=e71]: ;
              - generic [ref=e72]: "|"
              - text: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
              - generic [ref=e73]: 8 |
              - text: import RevenueStatCard from '../components/RevenueStatCard'
              - generic [ref=e74]: ;
              - generic [ref=e75]: 9 |
              - generic [ref=e76]: 10 |
              - text: interface RevenueData
              - generic [ref=e77]:
                - text: "{ Import traces: Client Component Browser: ./src/app/page.tsx [Client Component Browser] ./src/app/page.tsx [Server Component] Client Component SSR: ./src/app/page.tsx [Client Component SSR] ./src/app/page.tsx [Server Component]"
                - link "https://nextjs.org/docs/messages/module-not-found" [ref=e78] [cursor=pointer]:
                  - /url: https://nextjs.org/docs/messages/module-not-found
        - generic [ref=e79]: "1"
        - generic [ref=e80]: "2"
    - generic [ref=e85] [cursor=pointer]:
      - button "Open Next.js Dev Tools" [ref=e86]
      - button "Open issues overlay" [ref=e91]:
        - generic [ref=e92]:
          - generic [ref=e93]: "0"
          - generic [ref=e94]: "1"
        - generic [ref=e95]: Issue
  - alert [ref=e96]
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