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

-- Seed Customers and Products
INSERT INTO customers (id, name, email, created_at, updated_at) VALUES 
(1, 'Test Customer 1', 'test1@customer.com', NOW(), NOW()),
(2, 'Test Customer 2', 'test2@customer.com', NOW(), NOW());

INSERT INTO products (id, name, description, price_cents, is_active, created_at, updated_at) VALUES 
(1, 'Product A', 'Test Product A', 1999, true, NOW(), NOW()),
(2, 'Product B', 'Test Product B', 2999, true, NOW(), NOW()),
(3, 'Product C', 'Test Product C', 4000, true, NOW(), NOW()),
(4, 'Product D', 'Test Product D', 4994, true, NOW(), NOW());

-- Order 1: NEW
INSERT INTO orders (id, customer_id, order_date, total_amount_cents, status, created_at, updated_at)
VALUES (1, 1, NOW(), 1999, 'NEW', NOW(), NOW());
INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents, created_at, updated_at)
VALUES (1, 1, 1, 1999, NOW(), NOW());

-- Order 2: PAID
INSERT INTO orders (id, customer_id, order_date, total_amount_cents, status, created_at, updated_at)
VALUES (2, 1, NOW(), 2999, 'PAID', NOW(), NOW());
INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents, created_at, updated_at)
VALUES (2, 2, 1, 2999, NOW(), NOW());

-- Order 3: SHIPPED
INSERT INTO orders (id, customer_id, order_date, total_amount_cents, status, created_at, updated_at)
VALUES (3, 2, NOW(), 8994, 'SHIPPED', NOW(), NOW());
INSERT INTO order_items (order_id, product_id, quantity, unit_price_cents, created_at, updated_at)
VALUES (3, 3, 1, 4000, NOW(), NOW()),
       (3, 4, 1, 4994, NOW(), NOW());
