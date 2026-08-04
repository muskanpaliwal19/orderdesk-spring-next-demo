# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-registration.spec.ts >> Customer Registration >> should show an error message when registration fails
- Location: tests/customer-registration.spec.ts:28:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('div[role="alert"]')
Expected: visible
Error: strict mode violation: locator('div[role="alert"]') resolved to 2 elements:
    1) <div role="alert" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">…</div> aka getByRole('alert').filter({ hasText: 'Invalid customer data' })
    2) <div role="alert" aria-live="assertive" id="__next-route-announcer__"></div> aka locator('[id="__next-route-announcer__"]')

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('div[role="alert"]')

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
        - alert [ref=e12]: Invalid customer data
        - generic [ref=e13]:
          - heading "Add New Customer" [level=2] [ref=e14]
          - generic [ref=e15]:
            - generic [ref=e16]: Name
            - textbox "Name" [ref=e17]: Error User
          - generic [ref=e18]:
            - generic [ref=e19]: Email
            - textbox "Email" [ref=e20]: error@example.com
          - generic [ref=e21]:
            - generic [ref=e22]: Tier
            - combobox "Tier" [ref=e23]:
              - option "Basic"
              - option "Standard" [selected]
              - option "Premium"
              - option "VIP"
          - button "Add Customer" [ref=e25] [cursor=pointer]
        - table [ref=e27]:
          - rowgroup [ref=e28]:
            - row [ref=e29]:
              - columnheader "Name" [ref=e30]
              - columnheader "Email" [ref=e31]
              - columnheader "Tier" [ref=e32]
          - rowgroup
  - region "Notifications alt+T"
  - button "Open Next.js Dev Tools" [ref=e38] [cursor=pointer]
  - alert [ref=e42]
```

# Test source

```ts
  1   | 
  2   | import { test, expect } from '@playwright/test';
  3   | 
  4   | test.describe('Customer Registration', () => {
  5   |   test('should allow a user to register a new customer', async ({ page }) => {
  6   |     await page.goto('/customers');
  7   | 
  8   |     // Fill out the form
  9   |     const uniqueEmail = `testuser_${Date.now()}@example.com`;
  10  |     await page.getByTestId('name-input').fill('Test User');
  11  |     await page.getByTestId('email-input').fill(uniqueEmail);
  12  | 
  13  |     // Submit the form
  14  |     await page.getByTestId('submit-button').click();
  15  | 
  16  |     // Wait for the form to clear and the customer to appear in the table
  17  |     await expect(page.getByTestId('name-input')).toBeEmpty();
  18  |     await expect(page.getByTestId('email-input')).toBeEmpty();
  19  | 
  20  |     // Check that the new customer is in the table
  21  |     const customerRow = page.locator(`tr:has-text("Test User") and :has-text("${uniqueEmail}")`);
  22  |     await expect(customerRow).toBeVisible();
  23  |     
  24  |     const tierBadge = customerRow.locator('span:has-text("STANDARD")');
  25  |     await expect(tierBadge).toBeVisible();
  26  |   });
  27  | 
  28  |   test('should show an error message when registration fails', async ({ page }) => {
  29  |     // Intercept the API request and force a failure
  30  |     await page.route('/api/customers', route => {
  31  |       route.fulfill({
  32  |         status: 400,
  33  |         contentType: 'application/json',
  34  |         body: JSON.stringify({ message: 'Invalid customer data' }),
  35  |       });
  36  |     });
  37  | 
  38  |     await page.goto('/customers');
  39  | 
  40  |     // Fill out the form
  41  |     await page.getByTestId('name-input').fill('Error User');
  42  |     await page.getByTestId('email-input').fill('error@example.com');
  43  | 
  44  |     // Submit the form
  45  |     await page.getByTestId('submit-button').click();
  46  | 
  47  |     // Check for the error message
> 48  |     await expect(page.locator('div[role="alert"]')).toBeVisible();
      |                                                     ^ Error: expect(locator).toBeVisible() failed
  49  |     await expect(page.locator('div[role="alert"]')).toContainText('Invalid customer data');
  50  | 
  51  |     // The form should not have been cleared
  52  |     await expect(page.getByTestId('name-input')).toHaveValue('Error User');
  53  |   });
  54  | 
  55  |   test('should show an error message when email is already taken', async ({ page }) => {
  56  |     await page.goto('/customers');
  57  | 
  58  |     const duplicateEmail = `duplicate_${Date.now()}@example.com`;
  59  | 
  60  |     // Create the first customer
  61  |     await page.getByTestId('name-input').fill('First User');
  62  |     await page.getByTestId('email-input').fill(duplicateEmail);
  63  |     await page.getByTestId('submit-button').click();
  64  | 
  65  |     // Wait for the form to clear and the customer to appear in the table
  66  |     await expect(page.getByTestId('name-input')).toBeEmpty();
  67  |     await expect(page.locator(`tr:has-text("First User")`)).toBeVisible();
  68  | 
  69  |     // Attempt to create a second customer with the same email
  70  |     await page.getByTestId('name-input').fill('Second User');
  71  |     await page.getByTestId('email-input').fill(duplicateEmail);
  72  |     await page.getByTestId('submit-button').click();
  73  | 
  74  |     // Check for the error message
  75  |     const errorBanner = page.locator('div[role="alert"]');
  76  |     await expect(errorBanner).toBeVisible();
  77  |     await expect(errorBanner).toContainText('A customer with this email already exists.');
  78  | 
  79  |     // Check that the form was not cleared
  80  |     await expect(page.getByTestId('name-input')).toHaveValue('Second User');
  81  |   });
  82  | 
  83  |   test('should show an error if name is empty', async ({ page }) => {
  84  |     await page.goto('/customers');
  85  | 
  86  |     // Fill out the form with empty name
  87  |     await page.getByTestId('email-input').fill('test@example.com');
  88  |     await page.getByTestId('submit-button').click();
  89  | 
  90  |     // Check for the error message from backend validation
  91  |     const errorBanner = page.locator('div[role="alert"]');
  92  |     await expect(errorBanner).toBeVisible();
  93  |     await expect(errorBanner).toContainText('Name is mandatory');
  94  |   });
  95  | 
  96  |   test('should show an error if email is empty', async ({ page }) => {
  97  |     await page.goto('/customers');
  98  | 
  99  |     // Fill out the form with empty email
  100 |     await page.getByTestId('name-input').fill('Test User');
  101 |     await page.getByTestId('submit-button').click();
  102 | 
  103 |     // Check for the error message from backend validation
  104 |     const errorBanner = page.locator('div[role="alert"]');
  105 |     await expect(errorBanner).toBeVisible();
  106 |     await expect(errorBanner).toContainText('Email is mandatory');
  107 |   });
  108 | });
  109 | 
```