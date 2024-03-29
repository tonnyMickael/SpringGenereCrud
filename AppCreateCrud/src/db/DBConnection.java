package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String port;
    private String database;
    private String user;
    private String pwd;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public DBConnection(String port, String database, String user, String pwd){
        this.port = port;
        this.database = database;
        this.user = user;
        this.pwd = pwd;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException{
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost:"+getPort()+"/"+getDatabase();
        Class.forName(driver);
        Connection log = DriverManager.getConnection(url, getUser(), getPwd());
        return log;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
