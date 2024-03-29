package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBManager {
    private DBConnection dbConnection;

    public DBManager(DBConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    public String[] getColumnsFromTable(String tableName){
    //     // String query = "select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='[tableName]') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='[tableName]'";
        // query.replace("[tableName]", getName());
        String query = String.format("SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '%s'",tableName);
        // query.replace("[tableName]", tableName);
        Connection con = null;
        List <Object> listColumns = new ArrayList<>();
        try {
            con = this.dbConnection.getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                listColumns.add(result.getString("column_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            dbConnection.closeConnection(con);
        }
        return listColumns.toArray(new String[0]);
        // this.setEntityFields( entityFields.toArray(new EntityField[entityFields.size()]));
    }

	public String[] allTables() throws ClassNotFoundException {
		List<String> tabs = new ArrayList<String>();
		Connection con = null;
		try {
            con = dbConnection.getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                // Exclude "configorm" and "person_address" tables
                if (!tableName.equalsIgnoreCase("person_address")) {
                    tabs.add(tableName);
                }
				// tabs.add(rs.getString("TABLE_NAME"));
			}
			con.close();
		} catch (SQLException ex) {
			throw new RuntimeException("Erreur lors de la récupération des tables de la base de données", ex);
		} finally {
            dbConnection.closeConnection(con);
        }
		String[] tablesArray = tabs.toArray(new String[0]);
		return tablesArray;
	}

    public Object[] listRowsFromTable(String nameTable) {
        // String query = "select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='[tableName]') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='[tableName]'";
        // query.replace("[tableName]", getName());
        String query = "SELECT * FROM [tableName]";
        query.replace("[tableName]", nameTable);
        Connection con = null;
        List <Object> listRows = new ArrayList<>();
        try {
            con = this.dbConnection.getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    listRows.add(result.getObject(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            dbConnection.closeConnection(con);
        }
        return listRows.toArray(new Object[0]);
        // this.setEntityFields( entityFields.toArray(new EntityField[entityFields.size()]));
    }



    // public Object[] listColumnsFromTable(String nameTable) {
    //     // String query = "select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='[tableName]') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='[tableName]'";
    //     // query.replace("[tableName]", getName());
    //     String query = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '[tableName]'";
    //     query.replace("[tableName]", nameTable);
    //     Connection con = null;
    //     List <Object> listColumns = new ArrayList<>();
    //     try {
    //         con = this.dbConnection.getConnection();
    //         PreparedStatement statement = con.prepareStatement(query);
    //         ResultSet result = statement.executeQuery();
    //         while (result.next()) {
    //             listColumns.add(result.getString("column_name"));
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     finally{
    //         dbConnection.closeConnection(con);
    //     }
    //     return listColumns.toArray(new Object[0]);
    //     // this.setEntityFields( entityFields.toArray(new EntityField[entityFields.size()]));
    // }


    // public EntityField[] columnsToEntityField(String nameTable) throws ClassNotFoundException{
    //     String query = "select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='[tableName]') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='[tableName]'";
    //     query.replace("[tableName]", nameTable);
    //     List<EntityField> entityFields = new ArrayList<EntityField>();
    //     Connection con = null;
    //     try {
    //         con = dbConnection.getConnection();
    //         PreparedStatement statement = con.prepareStatement(query);
    //         // System.out.println(statement);
    //         ResultSet result = statement.executeQuery();
    //         while (result.next()) {
    //             EntityField entityField = new EntityField();
    //             entityField.setName(result.getString("column_name"));
    //             entityField.setType(FunctionUtils.parseSQLType(result.getString("data_type")));
    //             entityField.setPrimaryKey(result.getBoolean("is_primary"));
    //             entityFields.add(entityField);
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     finally{
    //         dbConnection.closeConnection(con);
    //     }

    //     return entityFields.toArray(new EntityField[entityFields.size()]);
    // }

}   