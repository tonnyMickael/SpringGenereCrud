create database dbconfigcrud;

create table person(
    id serial primary key,
    firstname varchar(255),
    lastname varchar(255),
    cin varchar(255),
    bithday date
);
create table region(
    id serial primary key,
    name varchar(255)
);