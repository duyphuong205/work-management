create table project_member_info (
    id bigint not null auto_increment,
    project_id bigint,
    email varchar(100),
    role varchar(10),
    joined_date timestamp,
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(id)
);