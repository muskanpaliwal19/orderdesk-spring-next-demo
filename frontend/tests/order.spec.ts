
import { test, expect } from '@playwright/test';

test.describe('Order Creation', () => {
  test('should allow a user to create a new order', async ({ page }) => {
    await page.goto('/');

    // Click the 'New Order' button to open the slide panel
    await page.getByRole('button', { name: 'New Order' }).click();

    // Wait for the panel to be visible
    await expect(page.getByRole('heading', { name: 'New Order' })).toBeVisible();

    // Select a customer
    await page.locator('#customerSelect').selectOption({ index: 1 });

    // Wait for products to load - check for product selector
    const productSelector = page.getByRole('combobox').nth(1);
    await expect(productSelector).toBeVisible();

    // Select a product in the first line item
    await productSelector.selectOption({ index: 1 });

    // Enter quantity
    await page.getByRole('spinbutton').fill('2');

    // Click the 'Create Order' button
    await page.getByRole('button', { name: 'Create Order' }).click();

    // Check for the success toast message
    await expect(page.getByText('Order created successfully!')).toBeVisible();

    // The panel should close upon successful order creation
    await expect(page.getByRole('heading', { name: 'New Order' })).not.toBeVisible();
  });
});
