CREATE TABLE IF NOT EXISTS request_tags
(
    id      serial primary key,
    tag_name    varchar(255),
    generation_request_id   int not null references generation_request(id),
    topic_id    int references article_topics(id),
    created timestamp DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS article_topics
(
    id         serial primary key,
    topic_title    varchar(255),
    article_id int references articles(id),
    created timestamp DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS articles
(
    id         serial primary key,
    article_body text,
    created timestamp DEFAULT current_timestamp
);