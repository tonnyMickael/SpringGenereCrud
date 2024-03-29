package function;

import java.sql.SQLException;

import config.Config;

import java.io.*;

/**
 * FunctionUtils
 */
public class FunctionUtils {

    public static String formatToFileJava(String arg){
        String newValue = String.format("%s.java",FunctionUtils.firstLetterToUpperCase(arg));
        return newValue;
    }

    public static String formatToFileFtl(String arg){
        String newValue = String.format("%s.ftl",arg);
        return newValue;
    }

    public static String primitifToObject (String arg) {
        switch (arg) {
            case "int":
            case "Integer":
                return "Integer";
            case "Long":
                return "Long";
            default:
                break;
        }
        return null;
    }
    
    public static String parseSQLType(String postgresType) throws SQLException {
        switch (postgresType.toUpperCase() ) {
            case "SMALLINT":
            case "INTEGER":
                return "int";
                // return Integer.parseInt(value);
            case "BIGINT":
                return "Long";
                // return Long.parseLong(value);
            case "NUMERIC":
            case "DECIMAL":
            case "DOUBLE PRECISION":
                // return new java.math.BigDecimal(value);
                return "double";
            case "BOOLEAN":
                // return Boolean.parseBoolean(value);
                return "boolean";
            case "CHAR":
            case "VARCHAR":
            case "CHARACTER VARYING":
            case "TEXT":
                return "String";
            case "DATE":
                return "java.time.LocalDate";
            case "TIME":
                return "java.sql.Time";
            case "TIMESTAMP":
                return "java.sql.Timestamp";
            default:
                throw new SQLException("Unsupported PostgreSQL data type: " + postgresType);
        }
    }

    /*
     * A function which changes in Maj
     */
    public static String firstLetterToUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return null or empty string if input is null or empty
        }
        
        // Get the first character and convert it to uppercase
        char firstChar = Character.toUpperCase(str.charAt(0));
        
        // Concatenate the uppercase first character with the rest of the string
        return firstChar + str.substring(1);
    }


    /**
     * Specify the destination folder to copy all models
     * Copy the file from ./template/Model.tmpl to ..[destinationFolder]/ maj([EntityName.java] 
     * */ 
    public static void copyAndRenameFile(String destinationFileName) throws IOException {
        File sourceFile = new File(Config.TEMPLATE_SOURCE_FOLDER_PATH, Config.TEMPLATE_MODEL_SOURCE_FILE_NAME);
        File destinationFile = new File(Config.MODEL_DESTINATION_FOLDER_PATH, destinationFileName);

        if(!destinationFile.exists()){
            try (InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destinationFile)) {
                // Copy the contents of the source file to the destination file
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }         
            System.out.println("File copied successfully from " + sourceFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
        }
        else {
            System.out.println("File already exists...");
            
        }
    }
    

    /* 
     * Change the name of template to ex: Produit.java
     * Replace the value in [] to the EntityTable  
     * */

    private static String concatFolderFile(String folder, String file){
        return folder+"//"+file;
    }

    public static void replacePholders(String fileName,String [] placeHolders, String []realValues, String TEMPLATE_SOURCE_FOLDER_PATH, String TEMPLATE_SOURCE_FILE_NAME, String DESTINATION_FOLDER_PATH){
         
        try (BufferedReader reader = new BufferedReader(new FileReader(concatFolderFile(TEMPLATE_SOURCE_FOLDER_PATH, TEMPLATE_SOURCE_FILE_NAME)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(concatFolderFile(DESTINATION_FOLDER_PATH, fileName)))) {
                
            String line;
            while ((line = reader.readLine()) != null) {
                // Replace placeholders with real values
                for (int i = 0; i < placeHolders.length; i++) {
                    line = line.replace(placeHolders[i], realValues[i]);
                }
                // Write the modified line to the output file
                writer.write(line);
                writer.newLine();
            }
            System.out.println(fileName+": file successfully processed.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}