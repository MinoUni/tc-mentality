INSERT INTO categories (name, description, image_filename, parent_category)
VALUES ('CARPETS', 'Category description', 'category_image.jpg', null);

INSERT INTO products (sku, name, description, price_amount, price_currency, quantity_in_stock,
                      category)
VALUES ('CAR-MAJ-1', 'Yoga Carpet Majestic K60', 'Product description', 406.56, 'USD', 100, 'CARPETS'),
       ('CAR-MAJ-2', 'Yoga Carpet Majestic K70', 'Product description', 26.56, 'USD', 10, 'CARPETS');

INSERT INTO product_images(index_id, image_url, product_sku)
VALUES (0, 'https://io.ment.strg/products/car_majk60_1.png', 'CAR-MAJ-1'),
       (1, 'https://io.ment.strg/products/car_majk60_2.png', 'CAR-MAJ-1'),
       (2, 'https://io.ment.strg/products/car_majk60_3.png', 'CAR-MAJ-1'),
       (0, 'https://io.ment.strg/products/car_majk70_1.png', 'CAR-MAJ-2'),
       (1, 'https://io.ment.strg/products/car_majk70_2.png', 'CAR-MAJ-2');