CREATE TABLE account
(
    account_id        UUID PRIMARY KEY,
    provider          VARCHAR(100)             NOT NULL,
    provider_id       VARCHAR(4096)            NOT NULL,
    organisation_name VARCHAR(100)             NOT NULL,
    create_date       TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT account_u1 UNIQUE (provider, provider_id)
);

ALTER TABLE tribe
    ADD COLUMN account_id UUID,
    ALTER COLUMN account_id SET NOT NULL,
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE;

ALTER TABLE squad
    ADD COLUMN account_id UUID,
    ALTER COLUMN account_id SET NOT NULL,
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE;

ALTER TABLE member
    ADD COLUMN account_id UUID,
    ALTER COLUMN account_id SET NOT NULL,
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE;

ALTER TABLE chapter
    ADD COLUMN account_id UUID,
    ALTER COLUMN account_id SET NOT NULL,
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE;

ALTER TABLE iteration
    ADD COLUMN account_id UUID,
    ALTER COLUMN account_id SET NOT NULL,
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (account_id) ON DELETE CASCADE;