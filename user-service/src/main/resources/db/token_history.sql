create table token_history (
    token_id bigint not null auto_increment,
    user_id bigint,
    token text,
    refresh_token text,
    status varchar(4),
    created_time timestamp,
    expire_time timestamp,
    refresh_expire_time timestamp,
    primary key(token_id)
);