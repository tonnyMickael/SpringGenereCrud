CREATE OR REPLACE FUNCTION clean(tables VARCHAR[])
RETURNS void  as $$
    DECLARE
        table_name varchar;
        sql_query TEXT;
    BEGIN
        FOREACH table_name IN ARRAY tables
        LOOP
            sql_query := 'DROP TABLE '||table_name;
            EXECUTE sql_query;
            RAISE NOTICE '%: table deleted...', table_name;
        END LOOP;
    END;
$$ LANGUAGE plpgsql;

-- @OneToMany
SELECT clean(ARRAY['person', 'region']);

-- @ManyToMany
SELECT clean(ARRAY['region_person','region','person']);
