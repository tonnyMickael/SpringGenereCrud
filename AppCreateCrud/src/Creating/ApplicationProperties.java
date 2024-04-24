package Creating;

import java.io.File;

import config.Config;
import config.ConfigSystem;
import db.DBConnection;
import function.FunctionUtils;

public class ApplicationProperties {

    private DBConnection dbConnection;
    
    public ApplicationProperties(DBConnection dbConnection){
        this.dbConnection = dbConnection;
    }
    private String [] listRealValues(){
        String [] values =  {
            this.dbConnection.getPort(),
            this.dbConnection.getDatabase(),
            this.dbConnection.getUser(),
            this.dbConnection.getPwd()
        };
        return values;
    }
    
    public void createApplicationPropreties(){
        File destinationFile = new File(ConfigSystem.path + Config.APPLICATION_PROPERTIES_DESTINATION_FOLDER_PATH, "application.properties");
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders("application.properties", Config.placeHoldersApplicationProperties, listRealValues(),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_APPLICATION_PROPERTIES_SOURCE_FILE_NAME,ConfigSystem.path + Config.APPLICATION_PROPERTIES_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println("application.properties"+" :File Already exist...");
        }  
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
