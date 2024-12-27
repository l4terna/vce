-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE IF NOT EXISTS access_tokens (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    is_revoked BOOLEAN NOT NULL DEFAULT false,
    expires_at TIMESTAMP NOT NULL,
    user_session_id BIGINT REFERENCES user_sessions(id),
    created_at TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP NOT NULL
);
-- rollback DROP TABLE IF EXISTS accessTokens;
