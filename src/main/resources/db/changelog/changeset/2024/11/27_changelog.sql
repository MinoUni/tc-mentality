-- liquibase formatted sql

-- changeset maksr:1
ALTER TABLE customers
    ADD profile_picture VARCHAR(255);
