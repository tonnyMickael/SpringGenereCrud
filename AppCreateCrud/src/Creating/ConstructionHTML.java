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
import config.ConfigSystem;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ConstructionHTML {

    DBManager dManager;

    public ConstructionHTML(ArrayList<MetaTable> dataTable,DBManager dbManager, ConfigORM[] configORM) throws IOException{
        this.dManager = dbManager;
        createFormulaire(dataTable,configORM);
        createFormInDetail(dataTable,configORM);
    }

    private void createFormulaire(ArrayList<MetaTable> dataTable,ConfigORM [] listConfigORM) throws IOException{
        for (int i = 0; i < dataTable.size(); i++) {
            String nameForm = dataTable.get(i).getNameTable()+"-form";
            // Créer un objet File représentant le fichier à créer.
            File fichier = new File(ConfigSystem.path + Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameForm+".ftl");
            // Si le fichier n'existe pas, le créer.
            if (!fichier.exists()) {
                fichier.createNewFile();    
            }
            WritePageForm(dataTable.get(i).getNameTable(),nameForm,dataTable.get(i).getInfoTable(),listConfigORM);

        }      
    }

    private void createFormInDetail(ArrayList<MetaTable> dataTable, ConfigORM [] listConfigORM) throws IOException{
        for (int i = 0; i < dataTable.size(); i++) {
            String tableName = dataTable.get(i).getNameTable();
            // Créer un objet File représentant le fichier à créer.
            for (int j = 0; j < listConfigORM.length; j++) {
                ConfigORM configORM = listConfigORM[j];
                String parentEntity = configORM.getName_table_parent();
                String childEntity = configORM.getName_table_child();
                String assocParentChild = configORM.getAssoc_parent_child();
                String assocChildParent = configORM.getAssoc_child_parent();
                boolean isBiDirectionnal = configORM.isBiDirectionnal();
                if (tableName.equals(childEntity) &&
                    assocParentChild.equals("1-N") && 
                    assocChildParent.equals("1-1") && isBiDirectionnal){

                    String outPutName = parentEntity+"-"+childEntity;
                    String nameForm = outPutName+"-form";
                    File fichier = new File(ConfigSystem.path + Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameForm+".ftl");
                    if (!fichier.exists()) {
                        fichier.createNewFile();    
                    }
                    writePageFormAddInDetail(dataTable.get(i).getNameTable(),nameForm,dataTable.get(i).getInfoTable(),configORM);
                }
                else if(tableName.equals(childEntity) &&
                    assocParentChild.equals("1-N") && 
                    assocChildParent.equals("1-N") && isBiDirectionnal){

                    String outPutName = childEntity+"-"+parentEntity;
                    String nameForm = outPutName+"-form";
                    File fichier = new File(ConfigSystem.path + Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameForm+".ftl");
                    if (!fichier.exists()) {
                        fichier.createNewFile();    
                        // writePageFormAddInDetail(dataTable.get(i).getNameTable(),nameForm,dataTable.get(i).getInfoTable(),configORM);
                    }
                    writePageFormAddInDetail(dataTable.get(i).getNameTable(),nameForm,dataTable.get(i).getInfoTable(),configORM);
                }
            }
        }   
    }

    private void writePageFormAddInDetail(String nametable, String nameform, ArrayList<MetaColumn> column,ConfigORM configORM) throws IOException{
        String outputPath = ConfigSystem.path + Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameform+".ftl";    
        // String templatePath = "/template/ParentChildForm.txt";
        String templatePath = "";


        String attributName = "";
        String [][] replacements = new String[2][2];
        // child Form add in detail parent 
        String parentEntity = configORM.getName_table_parent();
        String childEntity = configORM.getName_table_child();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();

        if (nametable.equals(childEntity) &&
            assocParentChild.equals("1-N") && 
            assocChildParent.equals("1-1") && isBiDirectionnal){

            templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/ParentChildForm.txt";
            String formulaire = champSaisie(column);
            // replacements = new String[3][2];
            attributName = this.dManager.columnContainsName(parentEntity);
            replacements = new String[4][2];
            replacements[0][0] = "[parentEntity]";
            replacements[0][1] = parentEntity;
            replacements[1][0] = "[childEntity]";
            replacements[1][1] = childEntity;
            replacements[2][0] = "[parentEntitySecondField]";
            replacements[2][1] = attributName;
            replacements[3][0] = "[formulaire]";
            replacements[3][1] = formulaire;
            String templateContent = readFromFile(templatePath);

            // Apply replacements
            for (String[] replacement : replacements) {
                templateContent = templateContent.replace( replacement[0] , replacement[1]);
            }
    
            // Write the modified content to a new file
            writeToFile(outputPath, templateContent);
        }
        else if(nametable.equals(childEntity) &&
            assocParentChild.equals("1-N") && 
            assocChildParent.equals("1-N") && isBiDirectionnal){

            templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/ChildParentForm.txt";
            // String formulaire = champSaisie(column);

            replacements = new String[5][2];

            String parentEntityMaj = FunctionUtils.firstLetterToUpperCase(parentEntity);

            attributName = this.dManager.columnContainsName(parentEntity);
            // replacements = new String[4][2];
            replacements[0][0] = "[parentEntity]";
            replacements[0][1] = parentEntity;
            replacements[1][0] = "[childEntity]";
            replacements[1][1] = childEntity;
            replacements[2][0] = "[parentEntitySecondField]";
            replacements[2][1] = attributName;
            replacements[3][0] = "[fieldsTable]";
            replacements[3][1] = this.generateFieldsTable(childEntity);
            replacements[4][0] = "[parentEntityMaj]";
            replacements[4][1] = parentEntityMaj;
            String templateContent = readFromFile(templatePath);

            // Apply replacements
            for (String[] replacement : replacements) {
                templateContent = templateContent.replace( replacement[0] , replacement[1]);
            }
    
            // Write the modified content to a new file
            writeToFile(outputPath, templateContent);
        }
        else {
            System.out.println("Fichier creer fotsiny...");
        }
    }

    String getTheSecondAttributFromTable(String tableName){
        String [] listColumns = this.dManager.getColumnsFromTable(tableName);
        return listColumns[1];
    }

    private String templateFieldsTable(String nameEntity, String nameField){
        // String nameFieldUpper = FunctionUtils.firstLetterToUpperCase(nameField);
        String value = String.format("\t\t\t\t\t<tr>\n"+
        "\t\t\t\t\t\t<td>%s</td>\n"+
        "\t\t\t\t\t\t<td>:</td>\n"+
        "\t\t\t\t\t\t<td>${%s.%s}</td>\n"+          
        "\t\t\t\t\t</tr>\n",nameField,nameEntity,nameField);
        return value;
    }

    private String generateFieldsTable(String nameEntity){
        // String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        EntityField[] entityFields = this.dManager.entityFieldColumnsFromTable(nameEntity);
        for (int i = 0; i < entityFields.length; i++) {
            EntityField entityField = entityFields[i];
            value += templateFieldsTable(nameEntity, entityField.getName());
        }
        return value;
    }

    private void WritePageForm(String nametable, String nameform, ArrayList<MetaColumn> column,ConfigORM [] listConfigORM) throws IOException{
        // File paths (replace with your actual paths)
        // String templatePath = Config.DATATYPE_HTML_PATH+"/template/formulaire.txt";
        String templatePath = "";
        String outputPath = ConfigSystem.path + Config.VIEWFORM_PAGE_DESTINATION_FOLDER_PATH+"/"+nameform+".ftl";

        String entityFk = "";
        String entityFkMaj = "";

        String attributName = "";

        String formulaire = champSaisie(column);

        String [][] replacements = new String[2][2];

        // ConfigORM configORM = new ConfigORM();
        // ConfigORM [] listConfigORM = configORM.listORMConfig();
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String parentEntity = configORMIndex.getName_table_parent();
            String childEntity = configORMIndex.getName_table_child();
            String inputFkParent = configORMIndex.getInputFkParent();
            String inputFkChild = configORMIndex.getInputFkChild();
            String assoc_parent_child = configORMIndex.getAssoc_parent_child();
            String assoc_child_parent = configORMIndex.getAssoc_child_parent();
            // String secondAttribut = this.getTheSecondAttributFromTable(childEntity);
            String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
            String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
            // String childEntity = configORMIndex.getTable_name_child();
            boolean isUniDirectionnal = configORMIndex.isUniDirectionnal();
            boolean isBiDirectionnal = configORMIndex.isBiDirectionnal();
            if(nametable.equals(childEntity) && inputFkChild.equals("2")){

                templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/formulaireWithSelect.txt";
                entityFk = parentEntity;
                entityFkMaj = parentEntityUpper;
                attributName = this.dManager.columnContainsName(parentEntity);
                replacements = new String[5][2];
                replacements[0][0] = "[nametable]";
                replacements[0][1] = nametable;
                replacements[1][0] = "[formulaire]";
                replacements[1][1] = formulaire;
                replacements[2][0] = "[entityFk]";
                replacements[2][1] = entityFk;
                replacements[3][0] = "[entityFkMaj]";
                replacements[3][1] = entityFkMaj;
                replacements[4][0] = "[attributName]";
                replacements[4][1] = attributName;
                break;
            }
            else if(nametable.equals(parentEntity) && inputFkParent.equals("2")){

                templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/formulaireWithSelect.txt";
                entityFk = childEntity;
                entityFkMaj = childEntityUpper;
                attributName = this.dManager.columnContainsName(childEntity);
                replacements = new String[5][2];
                replacements[0][0] = "[nametable]";
                replacements[0][1] = nametable;
                replacements[1][0] = "[formulaire]";
                replacements[1][1] = formulaire;
                replacements[2][0] = "[entityFk]";
                replacements[2][1] = entityFk;
                replacements[3][0] = "[entityFkMaj]";
                replacements[3][1] = entityFkMaj;
                replacements[4][0] = "[attributName]";
                replacements[4][1] = attributName;
                break;
            }
            else if(nametable.equals(parentEntity) && (inputFkParent.equals("") || inputFkChild.equals(""))){
                templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/formulaire.txt";
                replacements = new String[2][2];
                replacements[0][0] = "[nametable]";
                replacements[0][1] = nametable;
                replacements[1][0] = "[formulaire]";
                replacements[1][1] = formulaire;
                break;
            }
            else {
                templatePath = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/template/formulaire.txt";
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
