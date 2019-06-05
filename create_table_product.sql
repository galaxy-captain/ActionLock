create table mall_product_info
(
    id      integer auto_increment,
    name    varchar(32) not null,
    company varchar(32) not null,
    price   integer default 0,
    primary key (id),
    UNIQUE key (name, company)
)