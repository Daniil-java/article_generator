CREATE TABLE IF NOT EXISTS generation_request
(
    id         serial primary key,
    created_at timestamp DEFAULT current_timestamp
);

INSERT INTO generation_request(id)
VALUES (1);