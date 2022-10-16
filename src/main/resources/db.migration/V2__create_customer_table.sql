create table if not exists customer
(
    id            bigint not null auto_increment,
    address       varchar(255),
    date_of_birth varchar(255),
    email         varchar(255),
    enabled       bit    not null,
    first_name    varchar(255),
    last_name     varchar(255),
    password      varchar(255),
    phone         varchar(255),
    username      varchar(255),
    account_id    bigint,
    primary key (id)
) engine = MyISAM