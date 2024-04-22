CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    cin VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

create table address (
    id SERIAL  not null,
    streetname varchar(255) NOT NULL,
    zipcode varchar(255) NOT NULL
    primary key (id)
);

CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    postname VARCHAR(50) NOT NULL,
    matricule VARCHAR(50) NOT NULL
);

