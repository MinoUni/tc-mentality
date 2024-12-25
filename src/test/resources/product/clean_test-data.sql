DELETE
FROM product_images;

DELETE
FROM products
WHERE id = 1;

DELETE
FROM categories
WHERE id = 1;

ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;

