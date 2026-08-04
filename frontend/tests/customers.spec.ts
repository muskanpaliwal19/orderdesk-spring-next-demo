
import { test, expect } from '@playwright/test';

const mockCustomers = [
  { id: 1, name: 'Alice Wonder', email: 'alice@example.com', tier: 'VIP' },
  { id: 2, name: 'Bob Builder', email: 'bob@example.com', tier: 'BASIC' },
];

test.describe('Customers Page', () => {
  test('should display a list of customers', async ({ page }) => {
    // Mock the API response before navigating
    await page.route('**/api/customers', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(mockCustomers),
      });
    });

    await page.goto('/customers');

    // Check for the heading
    await expect(page.getByRole('heading', { name: 'Customers' })).toBeVisible();

    // Check for table content from mock data
    await expect(page.getByText('Alice Wonder')).toBeVisible();
    await expect(page.getByText('alice@example.com')).toBeVisible();
    await expect(page.getByText('VIP')).toBeVisible();

    await expect(page.getByText('Bob Builder')).toBeVisible();
    await expect(page.getByText('bob@example.com')).toBeVisible();
    await expect(page.getByText('BASIC')).toBeVisible();
  });
});
