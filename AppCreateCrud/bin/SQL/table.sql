CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    cin VARCHAR(20) UNIQUE NOT NULL,
    region_id bigint not null
);

CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

ALTER TABLE person
ADD CONSTRAINT fk_person_region
FOREIGN KEY (region_id)
REFERENCES region(id);

---------------------------------------------
create table address (
    id SERIAL  not null,
    street varchar(255) NOT NULL,
    zipcode varchar(255) NOT NULL,
    person_id integer NOT NULL,
    primary key (id)
);

-- @OneToOne to @OneToOne
-- @OneToMany to @ManyToOne if Only address is insertable
ALTER TABLE address
ADD CONSTRAINT fk_person_address
FOREIGN KEY (person_id)
REFERENCES person(id);

-- @OneToMany to @ManyToOne if Only address is a list
-- @OneToMany to @ManyToMany
CREATE TABLE person_address (
    person_id integer not null,
    address_id integer not null
);

ALTER TABLE person_address
ADD CONSTRAINT fk_person_address_person
FOREIGN KEY (person_id)
REFERENCES person(id);

ALTER TABLE person_address
ADD CONSTRAINT fk_person_address_address
FOREIGN KEY (address_id)
REFERENCES address(id);

-- User table
CREATE TABLE userp (
    id SERIAL NOT NULL,
    username varchar(255) NOT NULL,
    pwd varchar(255) NOT NULL,
    person_id integer NOT NULL
);

ALTER TABLE userp
ADD CONSTRAINT fk_person_user
FOREIGN KEY (person_id)
REFERENCES person(id);




-- CREATE TABLE configORM (
-- 	id SERIAL NOT NULL,
-- 	table_name_parent varchar(255) NOT NULL,
-- 	table_name_child varchar(255) NOT NULL,
-- 	association_type_parent varchar(255) NOT NULL,
-- 	association_type_child varchar(255) NOT NULL,
-- 	isJoinTable boolean default false not null,
-- 	isJoinColumn boolean default false not null
-- )

-- INSERT INTO configORM(table_name_parent, table_name_child, association_type_parent, association_type_child,isjointable)
-- values ('person','address','@OneToOne','@OneToOne', true)
