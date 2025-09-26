create table if not exists tag
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64) NOT NULL UNIQUE
);

create table if not exists message_template_tag
(
    message_template_id UUID NOT NULL references message_template (id) on delete cascade,
    tag_id              UUID NOT NULL references tag (id) on delete restrict,
    primary key (message_template_id, tag_id)
);

CREATE INDEX idx_tag_name on tag (name);
CREATE INDEX idx_mtt_tag on message_template_tag (tag_id);