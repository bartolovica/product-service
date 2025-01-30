CREATE TABLE IF NOT EXISTS public.product (
    id uuid NOT NULL,
    code varchar(10) NOT NULL,
    name varchar(255) NOT NULL,
    priceeur numeric(19, 3) NULL,
    priceusd numeric(19, 3) NULL,
    isavailable bool DEFAULT true NULL,
    conversionrate numeric(19, 3) NULL,
    createdby varchar(255) NULL,
    createdat timestamp NULL,
    updatedby varchar(255) NULL,
    updatedat timestamp NULL,
    CONSTRAINT product_code_key UNIQUE (code),
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT product_priceeur_check CHECK ((priceeur >= 0.0)),
    CONSTRAINT product_priceusd_check CHECK ((priceusd >= 0.0))
    );

INSERT INTO public.product (id, code, name, priceeur, priceusd, isavailable, conversionrate, createdby, createdat, updatedby, updatedat)
VALUES
    ('e1a1c1d2-3b4c-4d5e-6f7g-8h9i0j1k2l3m', 'P000000001', 'Product 1', 100.000, 120.000, true, 1.200, 'admin', NOW(), 'admin', NOW()),
    ('e2b2c2d3-4e5f-6g7h-8i9j-0k1l2m3n4o5p', 'P000000002', 'Product 2', 200.000, 240.000, true, 1.200, 'admin', NOW(), 'admin', NOW()),
    ('e3c3d4e5-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 'P000000003', 'Product 3', 300.000, 360.000, true, 1.200, 'admin', NOW(), 'admin', NOW());