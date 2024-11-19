--liquibase formatted sql

--changeset minouni:1
-- init database schema
CREATE TABLE customers(
    id INT GENERATED ALWAYS AS IDENTITY,
    email VARCHAR NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(17),
    address VARCHAR,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE categories(
    name VARCHAR,
    description TEXT,
    image_filename VARCHAR,
    parent_category VARCHAR,
    PRIMARY KEY(name),
    CONSTRAINT fk_category
        FOREIGN KEY(parent_category)
        REFERENCES categories(name)
);

CREATE TABLE products(
    id INT GENERATED ALWAYS AS IDENTITY,
    sku VARCHAR NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    description TEXT,
    price_amount DECIMAL(8,2) NOT NULL,
    sale_price_amount DECIMAL(8,2),
    status VARCHAR NOT NULL,
    quantity_in_stock INT NOT NULL,
    category VARCHAR,
    PRIMARY KEY(id),
    CONSTRAINT fk_category
        FOREIGN KEY(category)
            REFERENCES categories(name)
);

CREATE TABLE customers_carts(
    customer_email VARCHAR NOT NULL,
    product_sku VARCHAR NOT NULL,
    quantity INT NOT NULL,
    price_amount DECIMAL(8,2) NOT NULL,
    PRIMARY KEY(customer_email, product_sku),
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_email)
            REFERENCES customers(email),
    CONSTRAINT fk_product
        FOREIGN KEY(product_sku)
            REFERENCES products(sku)
);

CREATE TABLE products_images(
    image_filename VARCHAR,
    product_sku VARCHAR,
    PRIMARY KEY(image_filename),
    CONSTRAINT fk_product
        FOREIGN KEY(product_sku)
            REFERENCES products(sku)
);

CREATE TABLE payment_details(
    id INT GENERATED ALWAYS AS IDENTITY,
    price_amount DECIMAL(8,2),
    price_currency VARCHAR,
    payment_method VARCHAR NOT NULL,
    payment_status VARCHAR NOT NULL,
    payment_date TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE orders(
    id INT GENERATED ALWAYS AS IDENTITY,
    order_code VARCHAR NOT NULL UNIQUE,
    status VARCHAR,
    price_amount DECIMAL(8,2),
    order_date TIMESTAMP,
    customer_email VARCHAR,
    payment_id INT,
    PRIMARY KEY(id),
    CONSTRAINT fk_payment
        FOREIGN KEY(payment_id)
            REFERENCES payment_details(id)
);

CREATE TABLE orders_items(
    product_sku VARCHAR NOT NULL,
    order_code VARCHAR NOT NULL,
    quantity INT NOT NULL,
    price_amount DECIMAL(8,2) NOT NULL,
    PRIMARY KEY(product_sku, order_code),
    CONSTRAINT fk_product
        FOREIGN KEY(product_sku)
            REFERENCES products(sku),
    CONSTRAINT fk_order
        FOREIGN KEY(order_code)
            REFERENCES orders(order_code)
);

CREATE TABLE notifications(
    order_code VARCHAR NOT NULL,
    sender VARCHAR NOT NULL,
    recipient VARCHAR NOT NULL,
    content TEXT,
    created_at TIMESTAMP,
    send_at TIMESTAMP,
    PRIMARY KEY(order_code),
    CONSTRAINT fk_order
        FOREIGN KEY(order_code)
        REFERENCES orders(order_code)
);