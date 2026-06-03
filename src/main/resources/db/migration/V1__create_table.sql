CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE short_urls (
    id UUID PRIMARY KEY,

    short_code VARCHAR(8) NOT NULL UNIQUE,

    original_url TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    expires_at TIMESTAMP NOT NULL,

    click_count BIGINT NOT NULL DEFAULT 0,

    user_id UUID NOT NULL,

    CONSTRAINT fk_short_urls_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);