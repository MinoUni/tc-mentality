DELETE
FROM product_images
WHERE product_sku IN (SELECT product_sku FROM products);

DELETE
FROM products
WHERE id BETWEEN 1 AND 10;

DELETE
FROM categories
WHERE id BETWEEN 1 AND 10;

ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;
