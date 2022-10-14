create table account
(
    id          bigint not null auto_increment,
    created_at  varchar(255),
    is_blocked  bit,
    customer_id bigint,
    primary key (id)
) engine = MyISAM