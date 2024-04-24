package MetaData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Meta {
    DatabaseMetaData metaData;
    Connection acces = null;
    String nametable;
    String[] infoTable = new String[2];
    ArrayList<MetaTable> TableMetaData = new ArrayList<MetaTable>();
    MetaColumn columnData;
    MetaTable tableData;

    public Meta(){}

    // public ArrayList<MetaTable> metaDataBase(Connection log) throws ClassNotFoundException, SQLException{
    //     acces = log;
    //     String nameTable = "", nameColumn, typeColumn, schemaName;
    //     metaData = acces.getMetaData();
    //     schemaName = "public";
    //     //recuperation des noms des tables dans la base de donnée
    //     ResultSet getTableName = metaData.getTables(null, schemaName, null, new String[] {"TABLE"});
    //     while (getTableName.next()) {
    //         nameTable = getTableName.getString("TABLE_NAME");
    //         ArrayList<MetaColumn> ColumnMetaData = new ArrayList<MetaColumn>();
    //         //recuperation les noms des colonnes et leur types dans la base de donnée
    //         ResultSet getTableColmun = metaData.getColumns(null, null, nameTable, "%");
    //         while (getTableColmun.next()) {
    //             if(!getTableColmun.getString("COLUMN_NAME").contains("_id")){
    //                 nameColumn = getTableColmun.getString("COLUMN_NAME");
    //                 typeColumn = getTableColmun.getString("TYPE_NAME");
    //                 columnData = new MetaColumn(nameColumn, typeColumn);
    //                 ColumnMetaData.add(columnData);
    //             }
    //         }
    //         tableData = new MetaTable(nameTable, ColumnMetaData);
    //         TableMetaData.add(tableData);
    //     }
    //     acces.close();
    //     MetaFileConvertion convertion = new MetaFileConvertion();
    //     return convertion.convertionFieldHTML(TableMetaData);
    // }
    public ArrayList<MetaTable> metaDataBase(Connection log) throws ClassNotFoundException, SQLException {
        acces = log;
        String nameTable = "", nameColumn, typeColumn, schemaName;
        metaData = acces.getMetaData();
        schemaName = "public";
        ArrayList<MetaTable> filteredTableData = new ArrayList<>();
        String query = "SELECT table_name "+
        "FROM information_schema.tables "+
        "WHERE table_schema = 'public' AND table_name NOT IN ('users', 'roles', 'users_roles', 'token')";
        // Get table names from the database
        PreparedStatement statement = log.prepareStatement(query);
        ResultSet getTableName = statement.executeQuery();
        while (getTableName.next()) {
            nameTable = getTableName.getString("TABLE_NAME").trim();
            ArrayList<MetaColumn> ColumnMetaData = new ArrayList<>();
            // Get column names and types for each table
            ResultSet getTableColmun = metaData.getColumns(null, null, nameTable, "%");
            while (getTableColmun.next()) {
                if (!getTableColmun.getString("COLUMN_NAME").contains("_id")) {
                    nameColumn = getTableColmun.getString("COLUMN_NAME");
                    typeColumn = getTableColmun.getString("TYPE_NAME");
                    columnData = new MetaColumn(nameColumn, typeColumn);
                    ColumnMetaData.add(columnData);
                }
            }
            tableData = new MetaTable(nameTable, ColumnMetaData);
            filteredTableData.add(tableData);
        }
        acces.close();
        MetaFileConvertion convertion = new MetaFileConvertion();
        return convertion.convertionFieldHTML(filteredTableData);
    }
    
}
