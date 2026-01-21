create table task_info (
    task_id bigint not null auto_increment,
    title nvarchar(300),
    description mediumtext,
    comment nvarchar(500),
    status varchar(4),
    assignee_id bigint,
    project_id bigint,
    start_date date,
    end_date date,
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(task_id)
);