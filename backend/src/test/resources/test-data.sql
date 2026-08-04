-- Clear existing data
DELETE FROM products;

-- Reset sequences for H2
ALTER TABLE products ALTER COLUMN id RESTART WITH 1;

-- Seed Products
INSERT INTO products (id, name, description, price_cents, is_active, category, sku) VALUES
(1, 'Product C', 'Test Product C', 4000, true, 'cat2', 'SKU3'),
(2, 'Product A', 'Test Product A', 1999, true, 'cat1', 'SKU1'),
(3, 'Product D', 'Test Product D', 4994, true, 'cat2', 'SKU4'),
(4, 'Product B', 'Test Product B', 2999, true, 'cat1', 'SKU2'),
(5, 'Product E', 'Test Product E', 5995, false, 'cat1', 'SKU5');
