CREATE TABLE IF NOT EXISTS customers
(
    id              INT GENERATED ALWAYS AS IDENTITY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    phone           VARCHAR(17),
    address         VARCHAR(255),
    profile_picture VARCHAR(255),
    created_at      TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id              INT GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    description     TEXT,
    image_filename  VARCHAR(255),
    parent_category VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_category
        FOREIGN KEY (parent_category)
            REFERENCES categories (name)
);

CREATE TABLE IF NOT EXISTS products
(
    id                INT GENERATED ALWAYS AS IDENTITY,
    sku               VARCHAR(255)  NOT NULL UNIQUE,
    name              VARCHAR(255)  NOT NULL,
    description       TEXT,
    price_amount      DECIMAL(8, 2) NOT NULL,
    sale_price_amount DECIMAL(8, 2),
    status            VARCHAR(255)  NOT NULL,
    quantity_in_stock INT           NOT NULL,
    category          VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_category
        FOREIGN KEY (category)
            REFERENCES categories (name)
);

CREATE TABLE IF NOT EXISTS customers_carts
(
    customer_email VARCHAR(255)  NOT NULL,
    product_sku    VARCHAR(255)  NOT NULL,
    quantity       INT           NOT NULL,
    price_amount   DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (customer_email, product_sku),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_email)
            REFERENCES customers (email),
    CONSTRAINT fk_product
        FOREIGN KEY (product_sku)
            REFERENCES products (sku)
);

CREATE TABLE IF NOT EXISTS products_images
(
    id             INT GENERATED ALWAYS AS IDENTITY,
    image_filename VARCHAR(255) NOT NULL,
    product_sku    VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_sku)
            REFERENCES products (sku)
);

CREATE TABLE IF NOT EXISTS payment_details
(
    id             INT GENERATED ALWAYS AS IDENTITY,
    price_amount   DECIMAL(8, 2),
    price_currency VARCHAR(255),
    payment_method VARCHAR(255) NOT NULL,
    payment_status VARCHAR(255) NOT NULL,
    payment_date   TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders
(
    id             INT GENERATED ALWAYS AS IDENTITY,
    order_code     VARCHAR(255) NOT NULL UNIQUE,
    status         VARCHAR(255),
    price_amount   DECIMAL(8, 2),
    order_date     TIMESTAMP,
    customer_email VARCHAR(255),
    payment_id     INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment
        FOREIGN KEY (payment_id)
            REFERENCES payment_details (id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_email)
            REFERENCES customers (email)
);

CREATE TABLE IF NOT EXISTS orders_items
(
    product_sku  VARCHAR(255)  NOT NULL,
    order_code   VARCHAR(255)  NOT NULL,
    quantity     INT           NOT NULL,
    price_amount DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (product_sku, order_code),
    CONSTRAINT fk_product
        FOREIGN KEY (product_sku)
            REFERENCES products (sku),
    CONSTRAINT fk_order
        FOREIGN KEY (order_code)
            REFERENCES orders (order_code)
);

CREATE TABLE IF NOT EXISTS notifications
(
    id         INT GENERATED ALWAYS AS IDENTITY,
    order_code VARCHAR(255) NOT NULL,
    sender     VARCHAR(255) NOT NULL,
    recipient  VARCHAR(255) NOT NULL,
    content    TEXT,
    created_at TIMESTAMP,
    send_at    TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_order
        FOREIGN KEY (order_code)
            REFERENCES orders (order_code)
);
