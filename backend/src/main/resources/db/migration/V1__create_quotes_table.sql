CREATE TABLE quotes (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    author VARCHAR(255)
);
