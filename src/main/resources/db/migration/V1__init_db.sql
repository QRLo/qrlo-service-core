CREATE TABLE users(
    id SERIAL PRIMARY KEY NOT NULL,
    email VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    verified BOOLEAN,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT
);

CREATE TABLE business_cards(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    company VARCHAR,
    email VARCHAR,
    phone VARCHAR,
    position VARCHAR,
    email_verified BOOLEAN,
    version BIGINT,

    CONSTRAINT fk_users_business_cards FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);



create table oauths(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    oauth_type VARCHAR,
    connection_id VARCHAR,
    version BIGINT,

    CONSTRAINT fk_users_oauths FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);