ALTER TABLE article_generator.public.generation_request
    ADD COLUMN status smallint DEFAULT 0 CHECK(status >-1 AND status < 11);

ALTER TABLE article_generator.public.article_topics
    ADD COLUMN status smallint DEFAULT 0 CHECK(status >-1 AND status < 11);