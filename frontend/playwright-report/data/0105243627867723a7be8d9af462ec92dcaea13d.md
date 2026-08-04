# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-registration.spec.ts >> Customer Registration >> should show a client-side error if email is left empty
- Location: tests/customer-registration.spec.ts:60:7

# Error details

```
Error: expect(locator).toContainText(expected) failed

Locator: locator('div[role="alert"]')
Expected substring: "Name and email are required."
Received string:    ""
Timeout: 5000ms

Call log:
  - Expect "toContainText" with timeout 5000ms
  - waiting for locator('div[role="alert"]')
    14 × locator resolved to <div role="alert" aria-live="assertive" id="__next-route-announcer__"></div>
       - unexpected value ""

```

```yaml
- alert
```

# Test source

```ts
  1   | 
  2   | import { test, expect } from '@playwright/test';
  3   | 
  4   | test.describe('Customer Registration', () => {
  5   |   // Mock initial customer list for all tests in this describe block
  6   |   test.beforeEach(async ({ page }) => {
  7   |     await page.route('**/api/customers', route => {
  8   |       // For POST requests, let them pass through to be handled by specific tests
  9   |       if (route.request().method() === 'POST') {
  10  |         return route.continue();
  11  |       }
  12  |       // For GET requests, return an empty list
  13  |       route.fulfill({
  14  |         status: 200,
  15  |         contentType: 'application/json',
  16  |         body: JSON.stringify([]),
  17  |       });
  18  |     });
  19  |   });
  20  | 
  21  |   test('should allow a user to register a new customer', async ({ page }) => {
  22  |     await page.route('**/api/customers', async route => {
  23  |       if (route.request().method() === 'POST') {
  24  |         const json = route.request().postDataJSON();
  25  |         await route.fulfill({
  26  |           status: 200,
  27  |           contentType: 'application/json',
  28  |           body: JSON.stringify({ id: 123, ...json })
  29  |         });
  30  |       } else {
  31  |         await route.fulfill({ status: 200, contentType: 'application/json', body: '[]' });
  32  |       }
  33  |     });
  34  | 
  35  |     await page.goto('/customers');
  36  | 
  37  |     const uniqueName = `Test User ${Date.now()}`;
  38  |     const uniqueEmail = `testuser_${Date.now()}@example.com`;
  39  |     await page.getByTestId('name-input').fill(uniqueName);
  40  |     await page.getByTestId('email-input').fill(uniqueEmail);
  41  |     await page.locator('select').selectOption({ label: 'Premium' });
  42  |     await page.getByTestId('submit-button').click();
  43  | 
  44  |     await expect(page.getByTestId('name-input')).toBeEmpty();
  45  |     await expect(page.getByTestId('email-input')).toBeEmpty();
  46  |   });
  47  | 
  48  |   test('should show a client-side error if name is left empty', async ({ page }) => {
  49  |     await page.goto('/customers');
  50  | 
  51  |     // Leave name empty
  52  |     await page.getByTestId('email-input').fill('test@example.com');
  53  |     await page.getByTestId('submit-button').click();
  54  | 
  55  |     const errorBanner = page.locator('div[role="alert"]');
  56  |     await expect(errorBanner).toBeVisible();
  57  |     await expect(errorBanner).toContainText('Name and email are required.');
  58  |   });
  59  | 
  60  |   test('should show a client-side error if email is left empty', async ({ page }) => {
  61  |     await page.goto('/customers');
  62  | 
  63  |     // Leave email empty
  64  |     await page.getByTestId('name-input').fill('Test User');
  65  |     await page.getByTestId('submit-button').click();
  66  | 
  67  |     const errorBanner = page.locator('div[role="alert"]');
  68  |     await expect(errorBanner).toBeVisible();
> 69  |     await expect(errorBanner).toContainText('Name and email are required.');
      |                               ^ Error: expect(locator).toContainText(expected) failed
  70  |   });
  71  | 
  72  |   test('should show a server-side error for invalid email format', async ({ page }) => {
  73  |     // This test will check if the backend validation is also working
  74  |     // To bypass client-side validation, we remove the "if" block temporarily
  75  |     // For the purpose of this test, we assume we want to test the backend.
  76  |     // In a real scenario, we might have separate API tests for this.
  77  |     // Here, we will mock the POST response to simulate a backend validation error.
  78  | 
  79  |     await page.route('**/api/customers', async (route) => {
  80  |       if (route.request().method() === 'POST') {
  81  |         route.fulfill({
  82  |           status: 400,
  83  |           contentType: 'application/json',
  84  |           body: JSON.stringify({ message: 'Email should be valid' }),
  85  |         });
  86  |       } else {
  87  |          route.fulfill({
  88  |             status: 200,
  89  |             contentType: 'application/json',
  90  |             body: JSON.stringify([]),
  91  |           });
  92  |       }
  93  |     });
  94  | 
  95  |     await page.goto('/customers');
  96  | 
  97  |     await page.getByTestId('name-input').fill('Test User');
  98  |     await page.getByTestId('email-input').fill('not-an-email');
  99  |     await page.getByTestId('submit-button').click();
  100 | 
  101 |     const errorBanner = page.locator('div[role="alert"]');
  102 |     await expect(errorBanner).toBeVisible();
  103 |     await expect(errorBanner).toContainText('Email should be valid');
  104 |   });
  105 | });
  106 | 
```