create table system_parameter (
    parameter_id bigint not null auto_increment,
    type varchar(20),
    code varchar(20),
    value varchar(20),
    display_name nvarchar(300),
    status varchar(4),
    created_by bigint,
    created_time timestamp,
    updated_by bigint,
    updated_time timestamp,
    primary key(parameter_id)
);

INSERT INTO system_parameter
(parameter_id, type, code, value, display_name, status, created_by, created_time, updated_by, updated_time)
VALUES(1, 'OTP_TIME_OUT', 'OTP', '2', N'Thời gian hết hạn OTP', 'ACTV', -1, CURRENT_TIMESTAMP, NULL, NULL);