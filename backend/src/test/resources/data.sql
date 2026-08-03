
-- Clear existing data
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM customers;
DELETE FROM products;

-- Reset sequences for H2
ALTER TABLE customers ALTER COLUMN id RESTART WITH 1;
ALTER TABLE products ALTER COLUMN id RESTART WITH 1;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 1;


-- Seed data
INSERT INTO customers (id, name, email, created_at, updated_at)
VALUES (1, 'Test Customer', 'test@customer.com', NOW(), NOW());

INSERT INTO products (id, name, description, price_cents, is_active, created_at, updated_at)
VALUES (1, 'Product A', 'Description for Product A', 1000, true, NOW(), NOW()),
       (2, 'Product B', 'Description for Product B', 1550, true, NOW(), NOW());

-- shipped Orders
INSERT INTO orders (id, customer_id, order_date, total_amount_cents, status, created_at, updated_at)
VALUES (1, 1, '2023-01-15T10:00:00Z', 2000, 'SHIPPED', NOW(), NOW()),
       (2, 1, '2023-01-16T11:00:00Z', 1550, 'SHIPPED', NOW(), NOW());

INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents, created_at, updated_at)
VALUES (1, 1, 2, 1000, NOW(), NOW()),
       (2, 2, 1, 1550, NOW(), NOW());

-- New Order
INSERT INTO orders (id, customer_id, order_date, total_amount_cents, status, created_at, updated_at)
VALUES (3, 1, '2023-02-01T14:00:00Z', 1000, 'NEW', NOW(), NOW());

INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents, created_at, updated_at)
VALUES (3, 1, 1, 1000, NOW(), NOW());
