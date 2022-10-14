create table products
(
    dtype           varchar(31) not null,
    id              bigint      not null auto_increment,
    iban            varchar(255),
    balance         bigint,
    currency        varchar(255),
    interest_rate   decimal(19, 2),
    interest_rating varchar(255),
    product_type    varchar(255),
    account_id      bigint,
    primary key (id)
) engine = MyISAM