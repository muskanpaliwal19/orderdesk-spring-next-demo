# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-registration.spec.ts >> Customer Registration >> should show a server-side error for invalid email format
- Location: tests/customer-registration.spec.ts:123:7

# Error details

```
Error: expect(locator).toContainText(expected) failed

Locator: locator('div[role="alert"]')
Expected substring: "Email should be valid"
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
  54  |   test('should default to standard tier when no tier is selected', async ({ page }) => {
  55  |     const uniqueName = `Standard User ${Date.now()}`;
  56  |     const uniqueEmail = `standard_${Date.now()}@example.com`;
  57  |     const newCustomer = {
  58  |       id: 1,
  59  |       name: uniqueName,
  60  |       email: uniqueEmail,
  61  |       tier: 'STANDARD',
  62  |     };
  63  | 
  64  |     // This will override the beforeEach mock for POST requests
  65  |     await page.route('**/api/customers', async (route) => {
  66  |       if (route.request().method() === 'POST') {
  67  |         const postData = route.request().postDataJSON();
  68  |         expect(postData.tier).toBe('standard');
  69  |         
  70  |         await route.fulfill({
  71  |           status: 201,
  72  |           contentType: 'application/json',
  73  |           body: JSON.stringify(newCustomer),
  74  |         });
  75  |         return;
  76  |       }
  77  |       // Let GET requests be handled by the beforeEach mock
  78  |       await route.continue();
  79  |     });
  80  | 
  81  |     await page.goto('/customers');
  82  |     
  83  |     // beforeEach ensures table is empty because it mocks the GET request
  84  |     await expect(page.getByRole('cell', { name: 'No customers found.' })).toBeVisible();
  85  | 
  86  |     // Fill form, leave tier as default
  87  |     await page.getByPlaceholder('Name').fill(uniqueName);
  88  |     await page.getByPlaceholder('Email').fill(uniqueEmail);
  89  |     await page.getByRole('button', { name: 'Add Customer' }).click();
  90  | 
  91  |     // The component optimistically updates, so new customer appears
  92  |     const newCustomerRow = page.getByRole('row', { name: uniqueName });
  93  |     await expect(newCustomerRow).toBeVisible();
  94  |     
  95  |     const tierBadge = newCustomerRow.getByText('Standard');
  96  |     await expect(tierBadge).toBeVisible();
  97  |   });
  98  | 
  99  |   test('should show a client-side error if name is left empty', async ({ page }) => {
  100 |     await page.goto('/customers');
  101 | 
  102 |     // Leave name empty
  103 |     await page.getByTestId('email-input').fill('test@example.com');
  104 |     await page.getByTestId('submit-button').click();
  105 | 
  106 |     const errorBanner = page.locator('div[role="alert"]');
  107 |     await expect(errorBanner).toBeVisible();
  108 |     await expect(errorBanner).toContainText('Name and email are required.');
  109 |   });
  110 | 
  111 |   test('should show a client-side error if email is left empty', async ({ page }) => {
  112 |     await page.goto('/customers');
  113 | 
  114 |     // Leave email empty
  115 |     await page.getByTestId('name-input').fill('Test User');
  116 |     await page.getByTestId('submit-button').click();
  117 | 
  118 |     const errorBanner = page.locator('div[role="alert"]');
  119 |     await expect(errorBanner).toBeVisible();
  120 |     await expect(errorBanner).toContainText('Name and email are required.');
  121 |   });
  122 | 
  123 |   test('should show a server-side error for invalid email format', async ({ page }) => {
  124 |     // This test will check if the backend validation is also working
  125 |     // To bypass client-side validation, we remove the "if" block temporarily
  126 |     // For the purpose of this test, we assume we want to test the backend.
  127 |     // In a real scenario, we might have separate API tests for this.
  128 |     // Here, we will mock the POST response to simulate a backend validation error.
  129 | 
  130 |     await page.route('**/api/customers', async (route) => {
  131 |       if (route.request().method() === 'POST') {
  132 |         route.fulfill({
  133 |           status: 400,
  134 |           contentType: 'application/json',
  135 |           body: JSON.stringify({ message: 'Email should be valid' }),
  136 |         });
  137 |       } else {
  138 |          route.fulfill({
  139 |             status: 200,
  140 |             contentType: 'application/json',
  141 |             body: JSON.stringify([]),
  142 |           });
  143 |       }
  144 |     });
  145 | 
  146 |     await page.goto('/customers');
  147 | 
  148 |     await page.getByTestId('name-input').fill('Test User');
  149 |     await page.getByTestId('email-input').fill('not-an-email');
  150 |     await page.getByTestId('submit-button').click();
  151 | 
  152 |     const errorBanner = page.locator('div[role="alert"]');
  153 |     await expect(errorBanner).toBeVisible();
> 154 |     await expect(errorBanner).toContainText('Email should be valid');
      |                               ^ Error: expect(locator).toContainText(expected) failed
  155 |   });
  156 | });
  157 | 
```