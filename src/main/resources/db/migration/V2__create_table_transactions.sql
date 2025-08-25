CREATE TABLE transactions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    source_account_id BIGINT REFERENCES accounts(id),
    destination_account_id BIGINT REFERENCES accounts(id),
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT check_accounts CHECK (source_account_id IS NOT NULL OR destination_account_id IS NOT NULL)
);