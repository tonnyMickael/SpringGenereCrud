package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import function.FunctionUtils;

public class EntityTable {
    private String name;
    private EntityField [] entityFields;
    // private ForeignKey [] foreignKeys;
    private DBConnection dbConnection;

    // public ForeignKey[] getForeignKeys() {
    //     return foreignKeys;
    // }

    // public void setForeignKeys(ForeignKey[] foreignKeys) {
    //     this.foreignKeys = foreignKeys;
    // }

    public EntityTable(DBConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public EntityField[] getEntityFields() {
        return entityFields;
    }
    
    
    public void setEntityFields(EntityField[] entityFields) {
        this.entityFields = entityFields;
    }

    public void columnsToEntityField() throws ClassNotFoundException{
        // String query = "select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='[tableName]') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='[tableName]'";
        // query.replace("[tableName]", getName());
        String query = "SELECT \r\n" + //
                        "    cols.table_name, \r\n" + //
                        "    cols.column_name, \r\n" + //
                        "    cols.data_type, \r\n" + //
                        "    fk.foreign_table_name, \r\n" + //
                        "    fk.foreign_column_name, \r\n" + //
                        "    COALESCE(fk.is_primary, 'false') AS is_primary, \r\n" + //
                        "    COALESCE(fk.is_foreign, 'false') AS is_foreign, \r\n" + //
                        "    cols.is_nullable,\r\n" + //
                        "    fk.constraint_name AS foreign_key_name\r\n" + //
                        "FROM \r\n" + //
                        "    information_schema.columns AS cols \r\n" + //
                        "LEFT JOIN (\r\n" + //
                        "    SELECT \r\n" + //
                        "        tc.table_name, \r\n" + //
                        "        kcu.column_name, \r\n" + //
                        "        ccu.table_name AS foreign_table_name, \r\n" + //
                        "        ccu.column_name AS foreign_column_name, \r\n" + //
                        "        CASE WHEN tc.constraint_type='PRIMARY KEY' THEN 'true' ELSE 'false' END AS is_primary, \r\n" + //
                        "        CASE WHEN tc.constraint_type='FOREIGN KEY' THEN 'true' ELSE 'false' END AS is_foreign,\r\n" + //
                        "        tc.constraint_name\r\n" + //
                        "    FROM \r\n" + //
                        "        information_schema.table_constraints AS tc \r\n" + //
                        "    JOIN \r\n" + //
                        "        information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema \r\n" + //
                        "    JOIN \r\n" + //
                        "        information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name \r\n" + //
                        "    WHERE \r\n" + //
                        "        tc.table_schema='public' AND tc.table_name=? \r\n" + //
                        ") AS fk ON cols.column_name=fk.column_name AND cols.table_name=fk.table_name \r\n" + //
                        "WHERE \r\n" + //
                        "    cols.column_name NOT IN \r\n" + //
                        "    (SELECT\r\n" + //
                        "        kcu.column_name as source_column\r\n" + //
                        "    FROM\r\n" + //
                        "        information_schema.table_constraints AS tc\r\n" + //
                        "        JOIN information_schema.key_column_usage AS kcu\r\n" + //
                        "        ON tc.constraint_name = kcu.constraint_name\r\n" + //
                        "        AND tc.table_schema = kcu.table_schema\r\n" + //
                        "        JOIN information_schema.constraint_column_usage AS ccu\r\n" + //
                        "        ON ccu.constraint_name = tc.constraint_name\r\n" + //
                        "        AND ccu.table_schema = tc.table_schema\r\n" + //
                        "        \r\n" + //
                        "        WHERE  constraint_type = 'FOREIGN KEY' and tc.table_name = ?) \r\n" + //
                        "    AND cols.table_name=? and cols.column_name NOT LIKE '%_id'";

        List<EntityField> entityFields = new ArrayList<EntityField>();
        Connection con = null;
        try {
            con = this.dbConnection.getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, this.getName());
            statement.setString(2, this.getName());
            statement.setString(3, this.getName());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                EntityField entityField = new EntityField();
                entityField.setName(result.getString("column_name"));
                entityField.setType(FunctionUtils.parseSQLType(result.getString("data_type")));
                entityField.setPrimaryKey(result.getBoolean("is_primary"));
                entityField.setIsNullable(result.getString("is_nullable"));
                entityField.setForeignKey(result.getBoolean("is_foreign"));
                entityFields.add(entityField);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            dbConnection.closeConnection(con);
        }
        EntityField[] cols=new EntityField[entityFields.size()];
        for(int i=0;i<cols.length;i++){
            cols[i]=entityFields.get(i);
        }
        this.setEntityFields(cols);
        // this.setEntityFields( entityFields.toArray(new EntityField[entityFields.size()]));
    }

    // public void setListForeignKeys(String nameTable){
    //     String query = String.format("SELECT\r\n" + //
    //                     "    tc.constraint_name as fk_name,\r\n" + //
    //                     "    kcu.column_name as source_column,\r\n" + //
    //                     "    tc.table_name as source_table,\r\n" + //
    //                     "    kcu.column_name as source_column,\r\n" + //
    //                     "    ccu.table_name AS target_table,\r\n" + //
    //                     "    ccu.column_name AS target_column\r\n" + //
    //                     "FROM\r\n" + //
    //                     "    information_schema.table_constraints AS tc\r\n" + //
    //                     "    JOIN information_schema.key_column_usage AS kcu\r\n" + //
    //                     "      ON tc.constraint_name = kcu.constraint_name\r\n" + //
    //                     "      AND tc.table_schema = kcu.table_schema\r\n" + //
    //                     "    JOIN information_schema.constraint_column_usage AS ccu\r\n" + //
    //                     "      ON ccu.constraint_name = tc.constraint_name\r\n" + //
    //                     "      AND ccu.table_schema = tc.table_schema\r\n" + //
    //                     "WHERE constraint_type = 'FOREIGN KEY' and (tc.table_name = '"+nameTable+"' OR ccu.table_name ='"+nameTable+"')");
    //     // query.replace("[tableName]", nameTable);
    //     // System.out.println(query);
    //     Connection con = null;
    //     List <ForeignKey> listRows = new ArrayList<>();
    //     try {
    //         con = this.dbConnection.getConnection();
    //         PreparedStatement statement = con.prepareStatement(query);
    //         ResultSet result = statement.executeQuery();
    //         while (result.next()) {
    //             ForeignKey foreignKey = new ForeignKey();
    //             foreignKey.setName(result.getString("fk_name"));
    //             foreignKey.setSourceColumn(result.getString("source_column"));
    //             foreignKey.setSourceTable(result.getString("source_table"));
    //             foreignKey.setTargetColumn(result.getString("target_column"));
    //             foreignKey.setTargetTable(result.getString("target_table"));
    //             listRows.add(foreignKey);
    //             // for (int i = 1; i <= columnCount; i++) {
    //             //     // listRows.add(result.getObject(i));
    //             // }
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     finally{
    //         dbConnection.closeConnection(con);
    //     }
    //     ForeignKey[] cols=new ForeignKey[listRows.size()];
    //     for(int i=0;i<cols.length;i++){
    //         cols[i]=listRows.get(i);
    //     }
    //     this.setForeignKeys(listRows.toArray(new ForeignKey[0]));
    //     // this.setForeignKeys(cols);
    //     // return listRows.toArray(new ForeignKey[0]);        
    // }
}
