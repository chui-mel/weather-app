
CREATE TABLE weather (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(127) UNIQUE NOT NULL,
    country VARCHAR(2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);