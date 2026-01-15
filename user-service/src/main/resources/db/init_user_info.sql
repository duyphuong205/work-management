create table user_info (
    user_id bigint not null auto_increment,
    full_name nvarchar(100),
    email varchar(100) unique,
    password varchar(150),
    role varchar(10),
    status varchar(4),
    created_by bigint,
    created_time timestamp,
    update_by bigint,
    update_time timestamp,
    primary key(user_id)
);