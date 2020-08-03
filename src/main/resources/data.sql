create table client(
    id int generated always as ( identity ),
    first_name varchar(40) not null ,
    last_name varchar(40) not null ,
    father_name varchar(40),
    phone varchar(15) not null
);

insert into client (name_column,last_name,father_name,phone) values ('Иванов','Иван','Иванович','899923344455');
insert into client (name_column,last_name,father_name,phone) values ('Петров','Петр','Петрович','892345663423');
insert into client (name_column,last_name,father_name,phone) values ('Марков','Марк','Маркович','834929394923');
insert into client (name_column,last_name,father_name,phone) values ('Виолова','Виола','Вячеславовна','898877774477');
insert into client (name_column,last_name,father_name,phone) values ('Касаткин','Артем','Игоревич','8956473746737');
