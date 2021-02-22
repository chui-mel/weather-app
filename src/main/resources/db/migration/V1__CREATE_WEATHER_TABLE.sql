
CREATE TABLE weather (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(127) NOT NULL,
    country VARCHAR(2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX weather_city_country ON weather(city, country);