CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO users (id, username, password, created_at)
VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$10$IT7aL/z2Y1tC9JKaRQCuouRfTvasnzOcDhqK2sIqd367.7R9pp5Em',
    CURRENT_DATE
);