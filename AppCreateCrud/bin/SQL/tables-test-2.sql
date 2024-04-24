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

