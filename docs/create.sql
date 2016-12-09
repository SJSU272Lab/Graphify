create database if not exists expense;

create table if not exists entity
(
entity_id int auto_increment,
name varchar(80) not null,
email varchar(150)not null,
primary key (entity_id)
);

create table if not exists status
(
status_id int,
status_descr varchar(80) not null,
primary key (status_id)
);

create table if not exists expense_category
(
expense_category_id int,
exp_cat_descr varchar(80) not null,
primary key (expense_category_id)
);


create table if not exists expense
(
expense_id int auto_increment,
entity_id int not null,
expense_category_id int not null,
item_descr varchar(150) not null,
item_link varchar(150),
estimated_cost int not null,
submit_date date not null,
status_id int not null,
primary key (expense_id),
foreign key (entity_id) references entity(entity_id),
foreign key (expense_category_id) references expense_category(expense_category_id),
foreign key (status_id) references status(status_id)
);



insert into status (status_id, status_descr)
values (1,'pending');
insert into status (status_id, status_descr)
values (2,'approved');
insert into status (status_id, status_descr)
values (3,'rejected');
insert into status (status_id, status_descr)
values (4,'overbudget');

insert into expense_category (expense_category_id, exp_cat_descr)
values (1, 'office supplies');
insert into expense_category (expense_category_id, exp_cat_descr)
values (2, 'travel');
insert into expense_category (expense_category_id, exp_cat_descr)
values (3, 'training');
