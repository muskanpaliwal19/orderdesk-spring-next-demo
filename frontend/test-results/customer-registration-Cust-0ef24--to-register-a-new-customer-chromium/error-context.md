# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-registration.spec.ts >> Customer Registration >> should allow a user to register a new customer
- Location: tests/customer-registration.spec.ts:5:7

# Error details

```
Error: Channel closed
```

```
Error: page.goto: net::ERR_EMPTY_RESPONSE at http://localhost:3000/customers
Call log:
  - navigating to "http://localhost:3000/customers", waiting until "load"

```

# Test source

```ts
  1  | 
  2  | import { test, expect } from '@playwright/test';
  3  | 
  4  | test.describe('Customer Registration', () => {
  5  |   test('should allow a user to register a new customer', async ({ page }) => {
> 6  |     await page.goto('/customers');
     |                ^ Error: page.goto: net::ERR_EMPTY_RESPONSE at http://localhost:3000/customers
  7  | 
  8  |     // Fill out the form
  9  |     const uniqueEmail = `testuser_${Date.now()}@example.com`;
  10 |     await page.getByTestId('name-input').fill('Test User');
  11 |     await page.getByTestId('email-input').fill(uniqueEmail);
  12 | 
  13 |     // Submit the form
  14 |     await page.getByTestId('submit-button').click();
  15 | 
  16 |     // Wait for the form to clear and the customer to appear in the table
  17 |     await expect(page.getByTestId('name-input')).toBeEmpty();
  18 |     await expect(page.getByTestId('email-input')).toBeEmpty();
  19 | 
  20 |     // Check that the new customer is in the table
  21 |     const customerRow = page.locator(`tr:has-text("Test User") and :has-text("${uniqueEmail}")`);
  22 |     await expect(customerRow).toBeVisible();
  23 |     
  24 |     const tierBadge = customerRow.locator('span:has-text("STANDARD")');
  25 |     await expect(tierBadge).toBeVisible();
  26 |   });
  27 | 
  28 |   test('should show an error message when registration fails', async ({ page }) => {
  29 |     // Intercept the API request and force a failure
  30 |     await page.route('/api/customers', route => {
  31 |       route.fulfill({
  32 |         status: 400,
  33 |         contentType: 'application/json',
  34 |         body: JSON.stringify({ message: 'Invalid customer data' }),
  35 |       });
  36 |     });
  37 | 
  38 |     await page.goto('/customers');
  39 | 
  40 |     // Fill out the form
  41 |     await page.getByTestId('name-input').fill('Error User');
  42 |     await page.getByTestId('email-input').fill('error@example.com');
  43 | 
  44 |     // Submit the form
  45 |     await page.getByTestId('submit-button').click();
  46 | 
  47 |     // Check for the error message
  48 |     await expect(page.locator('div[role="alert"]')).toBeVisible();
  49 |     await expect(page.locator('div[role="alert"]')).toContainText('Invalid customer data');
  50 | 
  51 |     // The form should not have been cleared
  52 |     await expect(page.getByTestId('name-input')).toHaveValue('Error User');
  53 |   });
  54 | 
  55 |   test('should show an error message when email is already taken', async ({ page }) => {
  56 |     await page.goto('/customers');
  57 | 
  58 |     const duplicateEmail = `duplicate_${Date.now()}@example.com`;
  59 | 
  60 |     // Create the first customer
  61 |     await page.getByTestId('name-input').fill('First User');
  62 |     await page.getByTestId('email-input').fill(duplicateEmail);
  63 |     await page.getByTestId('submit-button').click();
  64 | 
  65 |     // Wait for the form to clear and the customer to appear in the table
  66 |     await expect(page.getByTestId('name-input')).toBeEmpty();
  67 |     await expect(page.locator(`tr:has-text("First User")`)).toBeVisible();
  68 | 
  69 |     // Attempt to create a second customer with the same email
  70 |     await page.getByTestId('name-input').fill('Second User');
  71 |     await page.getByTestId('email-input').fill(duplicateEmail);
  72 |     await page.getByTestId('submit-button').click();
  73 | 
  74 |     // Check for the error message
  75 |     const errorBanner = page.locator('div[role="alert"]');
  76 |     await expect(errorBanner).toBeVisible();
  77 |     await expect(errorBanner).toContainText('A customer with this email already exists.');
  78 | 
  79 |     // Check that the form was not cleared
  80 |     await expect(page.getByTestId('name-input')).toHaveValue('Second User');
  81 |   });
  82 | });
  83 | 
```