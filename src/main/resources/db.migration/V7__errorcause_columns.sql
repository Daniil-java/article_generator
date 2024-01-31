ALTER TABLE article_generator.public.generation_request
    ADD COLUMN error_cause text;

ALTER TABLE article_generator.public.article_topics
    ADD COLUMN error_cause text;