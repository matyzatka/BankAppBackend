create table if not exists  role
(
    id   bigint not null auto_increment,
    name varchar(255),
    primary key (id)
) engine = MyISAM