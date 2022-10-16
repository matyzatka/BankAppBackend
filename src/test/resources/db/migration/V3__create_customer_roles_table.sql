create table if not exists  customer_roles
(
    customer_id bigint not null,
    roles_id    bigint not null
) engine = MyISAM