CREATE TABLE accounts
(
    account_id        UUID          PRIMARY KEY,
    provider          VARCHAR(100)  NOT NULL,
    provider_id       VARCHAR(4096) NOT NULL,
    organisation_name VARCHAR(100)  NOT NULL,

    CONSTRAINT account_u1 UNIQUE (provider, provider_id)
);