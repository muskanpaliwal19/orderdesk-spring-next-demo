
import { test, expect } from '@playwright/test';

test.describe('Customer Registration', () => {
  test('should allow a user to register a new customer', async ({ page }) => {
    await page.goto('/customers');

    // Fill out the form
    const uniqueEmail = `testuser_${Date.now()}@example.com`;
    await page.getByTestId('name-input').fill('Test User');
    await page.getByTestId('email-input').fill(uniqueEmail);

    // Submit the form
    await page.getByTestId('submit-button').click();

    // Wait for the form to clear and the customer to appear in the table
    await expect(page.getByTestId('name-input')).toBeEmpty();
    await expect(page.getByTestId('email-input')).toBeEmpty();

    // Check that the new customer is in the table
    const customerRow = page.locator(`tr:has-text("Test User") and :has-text("${uniqueEmail}")`);
    await expect(customerRow).toBeVisible();
    
    const tierBadge = customerRow.locator('span:has-text("STANDARD")');
    await expect(tierBadge).toBeVisible();
  });

  test('should show an error message when registration fails', async ({ page }) => {
    // Intercept the API request and force a failure
    await page.route('/api/customers', route => {
      route.fulfill({
        status: 400,
        contentType: 'application/json',
        body: JSON.stringify({ message: 'Invalid customer data' }),
      });
    });

    await page.goto('/customers');

    // Fill out the form
    await page.getByTestId('name-input').fill('Error User');
    await page.getByTestId('email-input').fill('error@example.com');

    // Submit the form
    await page.getByTestId('submit-button').click();

    // Check for the error message
    await expect(page.locator('div[role="alert"]')).toBeVisible();
    await expect(page.locator('div[role="alert"]')).toContainText('Invalid customer data');

    // The form should not have been cleared
    await expect(page.getByTestId('name-input')).toHaveValue('Error User');
  });

  test('should show an error message when email is already taken', async ({ page }) => {
    await page.goto('/customers');

    const duplicateEmail = `duplicate_${Date.now()}@example.com`;

    // Create the first customer
    await page.getByTestId('name-input').fill('First User');
    await page.getByTestId('email-input').fill(duplicateEmail);
    await page.getByTestId('submit-button').click();

    // Wait for the form to clear and the customer to appear in the table
    await expect(page.getByTestId('name-input')).toBeEmpty();
    await expect(page.locator(`tr:has-text("First User")`)).toBeVisible();

    // Attempt to create a second customer with the same email
    await page.getByTestId('name-input').fill('Second User');
    await page.getByTestId('email-input').fill(duplicateEmail);
    await page.getByTestId('submit-button').click();

    // Check for the error message
    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('A customer with this email already exists.');

    // Check that the form was not cleared
    await expect(page.getByTestId('name-input')).toHaveValue('Second User');
  });

  test('should show an error if name is empty', async ({ page }) => {
    await page.goto('/customers');

    // Fill out the form with empty name
    await page.getByTestId('email-input').fill('test@example.com');
    await page.getByTestId('submit-button').click();

    // Check for the error message from backend validation
    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('Name is mandatory');
  });

  test('should show an error if email is empty', async ({ page }) => {
    await page.goto('/customers');

    // Fill out the form with empty email
    await page.getByTestId('name-input').fill('Test User');
    await page.getByTestId('submit-button').click();

    // Check for the error message from backend validation
    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('Email is mandatory');
  });
});
