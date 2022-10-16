create table if not exists account_products
(
    account_id  bigint not null,
    products_id bigint not null
) engine = MyISAM