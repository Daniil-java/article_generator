CREATE TABLE IF NOT EXISTS generation_request
(
    id         serial primary key,
    created timestamp DEFAULT current_timestamp
);
