
import { test, expect } from '@playwright/test';

test.describe('Customer Registration', () => {
  // Mock initial customer list for all tests in this describe block
  test.beforeEach(async ({ page }) => {
    await page.route('**/api/customers', route => {
      // For POST requests, let them pass through to be handled by specific tests
      if (route.request().method() === 'POST') {
        return route.continue();
      }
      // For GET requests, return an empty list
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([]),
      });
    });
  });

  test('should allow a user to register a new customer', async ({ page }) => {
    await page.route('**/api/customers', async route => {
      if (route.request().method() === 'POST') {
        const json = route.request().postDataJSON();
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({ id: 123, ...json })
        });
      } else {
        await route.fulfill({ status: 200, contentType: 'application/json', body: '[]' });
      }
    });

    await page.goto('/customers');

    const uniqueName = `Test User ${Date.now()}`;
    const uniqueEmail = `testuser_${Date.now()}@example.com`;
    await page.getByTestId('name-input').fill(uniqueName);
    await page.getByTestId('email-input').fill(uniqueEmail);
    await page.locator('select').selectOption({ label: 'Premium' });
    await page.getByTestId('submit-button').click();

    await expect(page.getByTestId('name-input')).toBeEmpty();
    await expect(page.getByTestId('email-input')).toBeEmpty();
  });

  test('should show a client-side error if name is left empty', async ({ page }) => {
    await page.goto('/customers');

    // Leave name empty
    await page.getByTestId('email-input').fill('test@example.com');
    await page.getByTestId('submit-button').click();

    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('Name and email are required.');
  });

  test('should show a client-side error if email is left empty', async ({ page }) => {
    await page.goto('/customers');

    // Leave email empty
    await page.getByTestId('name-input').fill('Test User');
    await page.getByTestId('submit-button').click();

    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('Name and email are required.');
  });

  test('should show a server-side error for invalid email format', async ({ page }) => {
    // This test will check if the backend validation is also working
    // To bypass client-side validation, we remove the "if" block temporarily
    // For the purpose of this test, we assume we want to test the backend.
    // In a real scenario, we might have separate API tests for this.
    // Here, we will mock the POST response to simulate a backend validation error.

    await page.route('**/api/customers', async (route) => {
      if (route.request().method() === 'POST') {
        route.fulfill({
          status: 400,
          contentType: 'application/json',
          body: JSON.stringify({ message: 'Email should be valid' }),
        });
      } else {
         route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify([]),
          });
      }
    });

    await page.goto('/customers');

    await page.getByTestId('name-input').fill('Test User');
    await page.getByTestId('email-input').fill('not-an-email');
    await page.getByTestId('submit-button').click();

    const errorBanner = page.locator('div[role="alert"]');
    await expect(errorBanner).toBeVisible();
    await expect(errorBanner).toContainText('Email should be valid');
  });
});
