ALTER TABLE article_generator.public.generation_request
    ADD COLUMN status smallint not null DEFAULT 0;

ALTER TABLE article_generator.public.article_topics
    ADD COLUMN status smallint not null DEFAULT 0;