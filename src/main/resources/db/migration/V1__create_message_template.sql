CREATE TABLE message_template
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    title      TEXT        NOT NULL,
    body       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- indexes for  searching by title/updated time
CREATE INDEX idx_message_template_title ON message_template USING gin (to_tsvector('simple', title));
CREATE INDEX idx_message_template_updated_at ON message_template (updated_at);