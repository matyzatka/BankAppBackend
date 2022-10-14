create table verification_token
(
    id          bigint not null auto_increment,
    expiry_date datetime,
    token       varchar(255),
    customer_id bigint not null,
    primary key (id)
) engine = MyISAM