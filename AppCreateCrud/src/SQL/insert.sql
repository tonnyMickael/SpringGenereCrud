-- Insert values into the 'region' table
INSERT INTO region (name) VALUES
    ('Analamanga'), -- Region ID 1
    ('Atsinanana'), -- Region ID 1
    ('Boeny'), -- Region ID 1
    ('Diana'), -- Region ID 2
    ('Sava'); -- Region ID 2

-- Insert values into the 'person' table
INSERT INTO person (firstName, lastName, birthday, cin, region_id) VALUES
    ('John', 'Doe', '1990-05-15', '123456', 1), -- Analamanga
    ('Jane', 'Smith', '1985-08-20', '789012', 1), -- Atsinanana
    ('Alice', 'Johnson', '2000-03-10', '345678', 1), -- Boeny
    ('Bob', 'Brown', '1978-11-25', '901234', 2), -- Diana
    ('Eva', 'Lee', '1995-06-05', '567890', 2); -- Sava

    
-- Test @OneToOne    
INSERT INTO person (firstName, lastName, birthday, cin) VALUES
    ('John', 'Doe', '1990-05-15', '123456'),('Jane', 'Smith', '1985-08-20', '789012');  
    
INSERT INTO region (name,person_id) VALUES
    ('Analamanga',1), -- Region ID 1
    ('Atsinanana',2); -- Region ID 1
    
    
-- Test @ManyToMany mappedBy
INSERT INTO person (firstName, lastName, birthday, cin) VALUES
    ('John', 'Doe', '1990-05-15', '123456'), -- Analamanga
    ('Jane', 'Smith', '1985-08-20', '789012'), -- Atsinanana
    ('Alice', 'Johnson', '2000-03-10', '345678');
    
  INSERT INTO region (name) VALUES
    ('Analamanga'), -- Region ID 1
    ('Atsinanana'), -- Region ID 1
    ('Boeny');
    
 INSERT INTO person_region (person_id, region_id) values 
 	(1,1),(1,2),(2,1),(2,2),(2,3);