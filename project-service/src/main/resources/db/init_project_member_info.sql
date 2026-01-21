create table project_member_info (
    id bigint not null auto_increment,
    project_id bigint,
    user_id bigint,
    role varchar(10),
    joined_date timestamp,
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(id)
);