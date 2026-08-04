
import { test, expect } from '@playwright/test';

test.describe('Revenue Report', () => {
  test('should display the grand total revenue in USD format', async ({ page }) => {
    // Mock the API response
    await page.route('/api/reports/revenue', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          totalRevenue: 123456, // 1234.56
          revenueByDate: [],
          revenueByStatus: [],
        }),
      });
    });

    await page.goto('/');

    // Check for the "Total Revenue" card
    const revenueCard = page.locator('div', { has: page.locator('span:has-text("Total Revenue")') });
    await expect(revenueCard).toBeVisible();

    // Check for the correctly formatted currency
    const revenueValue = revenueCard.locator('p');
    await expect(revenueValue).toHaveText('$1,234.56');
  });
});
