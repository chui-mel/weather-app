CREATE TABLE user_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(127) UNIQUE NOT NULL,
    rate_limit INT NOT NULL
);

CREATE TABLE token_usage (
    id BIGSERIAL PRIMARY KEY,
    token_id BIGINT(20) REFERENCES user_token (id),
    access_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX token_usage_access_time ON token_usage(access_time);
