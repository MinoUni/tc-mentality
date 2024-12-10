DELETE
FROM mentality_test.public.customers c
WHERE c.id = 1;

ALTER SEQUENCE customers_id_seq RESTART WITH 1;