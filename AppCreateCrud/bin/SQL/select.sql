
-- Query to get all necessary columns informations 
SELECT cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, COALESCE(fk.is_primary, 'false') AS is_primary, COALESCE(fk.is_foreign, 'false') AS is_foreign, cols.is_nullable
    FROM information_schema.columns AS cols 
    LEFT JOIN (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, 
                CASE WHEN tc.constraint_type='PRIMARY KEY' THEN 'true' ELSE 'false' END AS is_primary, 
                CASE WHEN tc.constraint_type='FOREIGN KEY' THEN 'true' ELSE 'false' END AS is_foreign 
                FROM information_schema.table_constraints AS tc 
                JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema 
                JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name 
                WHERE tc.table_schema='public' AND tc.table_name='person' ) AS fk 
    ON cols.column_name=fk.column_name AND cols.table_name=fk.table_name 
    WHERE cols.table_name='person' and cols.column_name NOT LIKE '%_id%'


SELECT 
    cols.table_name, 
    cols.column_name, 
    cols.data_type, 
    fk.foreign_table_name, 
    fk.foreign_column_name, 
    COALESCE(fk.is_primary, 'false') AS is_primary, 
    COALESCE(fk.is_foreign, 'false') AS is_foreign, 
    cols.is_nullable,
    fk.constraint_name AS foreign_key_name
FROM 
    information_schema.columns AS cols 
LEFT JOIN (
    SELECT 
        tc.table_name, 
        kcu.column_name, 
        ccu.table_name AS foreign_table_name, 
        ccu.column_name AS foreign_column_name, 
        CASE WHEN tc.constraint_type='PRIMARY KEY' THEN 'true' ELSE 'false' END AS is_primary, 
        CASE WHEN tc.constraint_type='FOREIGN KEY' THEN 'true' ELSE 'false' END AS is_foreign,
        tc.constraint_name
    FROM 
        information_schema.table_constraints AS tc 
    JOIN 
        information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema 
    JOIN 
        information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name 
    WHERE 
        tc.table_schema='public' AND tc.table_name='address' 
) AS fk ON cols.column_name=fk.column_name AND cols.table_name=fk.table_name 
WHERE 
    cols.column_name NOT LIKE '%_id%' AND cols.table_name='address';

-- Query list columns without foreign key
SELECT 
    cols.table_name, 
    cols.column_name, 
    cols.data_type, 
    fk.foreign_table_name, 
    fk.foreign_column_name, 
    COALESCE(fk.is_primary, 'false') AS is_primary, 
    COALESCE(fk.is_foreign, 'false') AS is_foreign, 
    cols.is_nullable,
    fk.constraint_name AS foreign_key_name
FROM 
    information_schema.columns AS cols 
LEFT JOIN (
    SELECT 
        tc.table_name, 
        kcu.column_name, 
        ccu.table_name AS foreign_table_name, 
        ccu.column_name AS foreign_column_name, 
        CASE WHEN tc.constraint_type='PRIMARY KEY' THEN 'true' ELSE 'false' END AS is_primary, 
        CASE WHEN tc.constraint_type='FOREIGN KEY' THEN 'true' ELSE 'false' END AS is_foreign,
        tc.constraint_name
    FROM 
        information_schema.table_constraints AS tc 
    JOIN 
        information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema 
    JOIN 
        information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name 
    WHERE 
        tc.table_schema='public' AND tc.table_name='address' 
) AS fk ON cols.column_name=fk.column_name AND cols.table_name=fk.table_name 
WHERE 
    cols.column_name NOT IN 
    (SELECT
        kcu.column_name as source_column
    FROM
        information_schema.table_constraints AS tc
        JOIN information_schema.key_column_usage AS kcu
        ON tc.constraint_name = kcu.constraint_name
        AND tc.table_schema = kcu.table_schema
        JOIN information_schema.constraint_column_usage AS ccu
        ON ccu.constraint_name = tc.constraint_name
        AND ccu.table_schema = tc.table_schema
        
        WHERE constraint_type = 'FOREIGN KEY' and tc.table_name = 'address') 
    AND cols.table_name='address';

-- List foreign key exist in a specific table
SELECT
    tc.constraint_name as fk_name,
    kcu.column_name as source_column,
    tc.table_name as source_table,
    kcu.column_name as source_column,
    ccu.table_name AS target_table,
    ccu.column_name AS target_column
FROM
    information_schema.table_constraints AS tc
    JOIN information_schema.key_column_usage AS kcu
      ON tc.constraint_name = kcu.constraint_name
      AND tc.table_schema = kcu.table_schema
    JOIN information_schema.constraint_column_usage AS ccu
      ON ccu.constraint_name = tc.constraint_name
      AND ccu.table_schema = tc.table_schema
WHERE constraint_type = 'FOREIGN KEY' and (tc.table_name = 'address' OR ccu.table_name ='address');


-- Query to get the name of foreign key
SELECT constraint_name
FROM information_schema.table_constraints
WHERE table_name = 'address'
  AND constraint_type = 'FOREIGN KEY';

-- Query to get all columns from table
SELECT column_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'produit';

-- 21 Mars 2023
-- Query to list foreign key name
SELECT
    tc.table_schema,
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_schema AS foreign_table_schema,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY'
    AND tc.table_schema = 'public'
    AND tc.table_name = 'person';

-- v2
SELECT
    tc.table_schema,
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_schema AS foreign_table_schema,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY'
    AND tc.table_schema = 'public'
    AND tc.table_name = 'person';


SELECT cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, COALESCE(fk.is_primary, 'false') AS is_primary, COALESCE(fk.is_foreign, 'false') AS is_foreign , cols.is_nullable FROM information_schema.columns AS cols LEFT JOIN (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, CASE WHEN tc.constraint_type='PRIMARY KEY' THEN 'true' ELSE 'false' END AS is_primary, CASE WHEN tc.constraint_type='FOREIGN KEY' THEN 'true' ELSE 'false' END AS is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='person' ) AS fk ON cols.column_name=fk.column_name AND cols.table_name=fk.table_name WHERE cols.table_name='person'