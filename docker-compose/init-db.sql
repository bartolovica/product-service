CREATE TABLE IF NOT EXISTS public.product (
                                              id uuid NOT NULL,
                                              code varchar(10) NOT NULL,
    name varchar(255) NOT NULL,
    priceeur numeric(10, 3) NULL,
    priceusd numeric(10, 3) NULL,
    isavailable bool DEFAULT true NULL,
    conversionrate numeric(19, 4) NULL,
    createdby varchar(255) NULL,
    createdat timestamp NULL,
    updatedby varchar(255) NULL,
    updatedat timestamp NULL,
    CONSTRAINT product_code_key UNIQUE (code),
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT product_priceeur_check CHECK ((priceeur >= 0.0)),
    CONSTRAINT product_priceusd_check CHECK ((priceusd >= 0.0))
    );