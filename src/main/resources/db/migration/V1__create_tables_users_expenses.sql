-- =============================================================================
-- EXTENSIONS
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================================================
-- ENUMS
-- =============================================================================

CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TYPE expense_category AS ENUM (
    'FOOD',
    'TRANSPORTATION',
    'HOUSING',
    'UTILITIES',
    'HEALTHCARE',
    'ENTERTAINMENT',
    'EDUCATION',
    'CLOTHING',
    'INSURANCE',
    'SAVINGS',
    'INVESTMENTS',
    'GROCERIES',
    'PETS',
    'GIFTS',
    'TRAVEL',
    'SUBSCRIPTIONS',
    'TECHNOLOGY',
    'SPORTS',
    'OTHERS'
);

-- =============================================================================
-- FUNCTIONS
-- =============================================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

-- =============================================================================
-- TABLES
-- =============================================================================

CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(255)     NOT NULL,
    email               VARCHAR(255)     NOT NULL UNIQUE,
    password_hash       VARCHAR(255)     NOT NULL,
    role                user_role        NOT NULL DEFAULT 'USER',
    is_active           BOOLEAN          NOT NULL DEFAULT true,
    created_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    last_accessed_at    TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();


CREATE TABLE expenses (
    id          UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    name        VARCHAR(50)         NOT NULL,
    description VARCHAR(150),
    amount      DECIMAL(10, 2)      NOT NULL,
    category    expense_category    NOT NULL,
    user_id     UUID                NOT NULL REFERENCES users (id),
    date        DATE                NOT NULL,
    created_at  TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ         NOT NULL DEFAULT NOW()
);

CREATE TRIGGER update_expenses_updated_at
    BEFORE UPDATE
    ON expenses
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE INDEX idx_expenses_user_id ON expenses (user_id);
