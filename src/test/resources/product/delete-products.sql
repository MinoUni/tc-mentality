DELETE
FROM product_images
WHERE product_sku IN (SELECT product_sku FROM products);

DELETE
FROM products
WHERE id IN (1, 2);

DELETE
FROM categories
WHERE id = 1;

ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;

