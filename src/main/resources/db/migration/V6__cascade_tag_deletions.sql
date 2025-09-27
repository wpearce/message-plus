drop table if exists message_template_tag;

create table message_template_tag
(
    message_template_id uuid not null,
    tag_id              uuid not null,
    primary key (message_template_id, tag_id),
    constraint fk_mtt_template
        foreign key (message_template_id)
            references message_template (id)
            on delete cascade,
    constraint fk_mtt_tag
        foreign key (tag_id)
            references tag (id)
            on delete cascade
);
