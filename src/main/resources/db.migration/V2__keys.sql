CREATE TABLE IF NOT EXISTS open_ai_key
(
    id          serial primary key,
    name        varchar(255),
    key         varchar(255),
    created     timestamp DEFAULT current_timestamp
);