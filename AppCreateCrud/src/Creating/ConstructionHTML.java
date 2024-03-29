package Creating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import MetaData.MetaColumn;
import MetaData.MetaTable;
import config.Config;
import config.ConfigORM;
import db.DBManager;
import function.FunctionUtils;

public class ConstructionHTML {

    DBManager dManager;

    public ConstructionHTML(ArrayList<MetaTable> dataTable,DBManager dbManager) throws IOException{
        this.dManager = dbManager;
        createFormulaire(dataTable);
    }

    private void createFormulaire(ArrayList<MetaTable> dataTable) throws IOException{
        for (int i = 0; i < dataTable.size(); i++) {
            String nameForm = dataTable.get(i).getNameTable()+"-form";
            // Créer un objet File représentant le fichier à créer.
            File fichier = new File(Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameForm+".ftl");
            // Si le fichier n'existe pas, le créer.
            if (!fichier.exists()) {
                fichier.createNewFile();    
            }
            WritePageForm(dataTable.get(i).getNameTable(),nameForm,dataTable.get(i).getInfoTable());
            // String namelist = dataTable.get(i).getNameTable()+"-list";
            // // Créer un objet File représentant le fichier à créer.
            // File fichiers = new File(Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+namelist+".ftl");
            // // Si le fichier n'existe pas, le créer.
            // if (!fichiers.exists()) {
            //     fichiers.createNewFile();    
            // }  
            // WritePageList(namelist);
            // ConstructionModel controller = new ConstructionModel(dataTable);
        }      
    }


    // private String selectTemplate (String childEntityUpper, String childEntity, String fieldName){
    //     String value = "\t\t\t\t<select name=\""+childEntity+"_id\">\n"+
    //     "\t\t\t\t\t<#list list"+childEntityUpper+" as "+childEntity+">\n"+
    //         "\t\t\t\t\t\t<option value=\"${"+childEntity+".id}\">${"+childEntity+"."+fieldName+"}</option>\n"+
    //     "\t\t\t\t\t</#list>\n"+
    // "\t\t\t\t</select>\n";
    //     return value;
    // }

    String getTheSecondAttributFromTable(String tableName){
        String [] listColumns = this.dManager.getColumnsFromTable(tableName);
        return listColumns[1];
    }

    // // Generate the Select input child entity
    // private String selectChildEntity(String nameForm){
    //     String value = "";
    //     String nameEntity = nameForm.split("-")[0];
    //     ConfigORM configORM = new ConfigORM();
    //     ConfigORM [] listConfigORM = configORM.listORMConfig();
    //     for (int i = 0; i < listConfigORM.length; i++) {
    //         ConfigORM configORMIndex = listConfigORM[i];
    //         String parentEntity = configORMIndex.getTable_name_parent();
    //         String childEntity = configORMIndex.getTable_name_child();
    //         String secondAttribut = this.getTheSecondAttributFromTable(childEntity);
    //         String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
    //         // String childEntity = configORMIndex.getTable_name_child();
    //         if(nameEntity.equals(parentEntity)){
    //             value += this.selectTemplate(childEntityUpper, childEntity, secondAttribut); 
    //             break;
    //         }
    //         else{
    //             value = "";
    //             break;
    //         }
    //     }
    //     return value;
    // }


    private void WritePageForm(String nametable, String nameform, ArrayList<MetaColumn> column) throws IOException{
        // File paths (replace with your actual paths)
        // String templatePath = Config.DATATYPE_HTML_PATH+"/template/formulaire.txt";
        String templatePath = "";
        String outputPath = Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameform+".ftl";

        String childEntity = "";
        String childEntityMaj = "";

        String formulaire = champSaisie(column);

        String [][] replacements = new String[2][2];

        // Replacements (adjust key-value pairs as needed)
        // String[][] replacements = {
        //     {"[nametable]", nametable},
        //     {"[formulaire]", formulaire},
        // };

        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORM = configORM.listORMConfig();
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String parentEntity = configORMIndex.getName_table_parent();
            String childEntityConfig = configORMIndex.getName_table_child();
            // String secondAttribut = this.getTheSecondAttributFromTable(childEntity);
            String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntityConfig);
            // String childEntity = configORMIndex.getTable_name_child();
            if(nametable.equals(parentEntity)){

                templatePath = Config.DATATYPE_HTML_PATH+"/template/formulaireWithSelect.txt";
                childEntity = childEntityConfig;
                childEntityMaj = childEntityUpper;
                replacements = new String[4][2];
                replacements[0][0] = "[nametable]";
                replacements[0][1] = nametable;
                replacements[1][0] = "[formulaire]";
                replacements[1][1] = formulaire;
                replacements[2][0] = "[childEntity]";
                replacements[2][1] = childEntity;
                replacements[3][0] = "[childEntityMaj]";
                replacements[3][1] = childEntityMaj;
                break;
            }
            else {
                templatePath = Config.DATATYPE_HTML_PATH+"/template/formulaire.txt";
                replacements = new String[2][2];
                replacements[0][0] = "[nametable]";
                replacements[0][1] = nametable;
                replacements[1][0] = "[formulaire]";
                replacements[1][1] = formulaire;
                break;
            }
        }

        // Read the template file
        String templateContent = readFromFile(templatePath);

        // Apply replacements
        for (String[] replacement : replacements) {
            // System.out.println("Replace this  :"+replacement[0]+"\n");
            // System.out.println("to this :"+replacement[1]+"\n");
            templateContent = templateContent.replace(  replacement[0] , replacement[1]);
        }

        // Write the modified content to a new file
        writeToFile(outputPath, templateContent);

        // System.out.println("Template processed and output file created successfully!");
    }

    private void WritePageList(String nameform) throws IOException{
        // File paths (replace with your actual paths)
        String templatePath = Config.DATATYPE_HTML_PATH+"/template/liste.txt";
        String outputPath = Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameform+".ftl";

        // Replacements (adjust key-value pairs as needed)
        String[][] replacements = {
                {"nameClass", "nameClass"}, // Replace with your desired class name
                {"urlclass", "url"},  // Replace with your class name (lowercase, hyphen-separated)
                {"navigationUrlForm", "url"},  // Replace with your form navigation URL segment
                {"urlForm", "url"+"_formuliare"},         // Replace with your form template file name
                {"navigationUrlList", "url"}, // Replace with your list navigation URL segment
                {"UrlList", "url"+"_liste"}        // Replace with your list template file name
        };

        // Read the template file
        String templateContent = readFromFile(templatePath);

        // Apply replacements
        for (String[] replacement : replacements) {
            templateContent = templateContent.replaceAll("\\{" + replacement[0] + "\\}", replacement[1]);
        }

        // Write the modified content to a new file
        writeToFile(outputPath, templateContent);

        // System.out.println("Template processed and output file created successfully!");
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private String champSaisie(ArrayList<MetaColumn> columns){
        String formInput = "";
        ArrayList<ConstructionField> field = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            field.add(new ConstructionField(columns.get(i).getNameColumn(), columns.get(i).getTypeColumn()));
        }
        for (ConstructionField constructionField : field) {
            formInput += constructionField.getInputHtml();
        }
        String Input = formInput;
        return Input;
    }
}
