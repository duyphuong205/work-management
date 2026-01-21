create table project_info (
    project_id bigint not null auto_increment,
    name nvarchar(200),
    status varchar(4),
    owner_id bigint,
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(project_id)
);