package view;

import java.io.File;

import config.Config;
import config.ConfigORM;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ViewDetailGenerate {
    private ViewDetail viewDetail;
    private DBManager dbManager;

    public ViewDetailGenerate(ViewDetail viewDetail, DBManager dbManager) {
        this.dbManager = dbManager;
        this.viewDetail = viewDetail;
    }

    /*
     * Fonction mi genere fieldsTable
     * - creer d'abord un template 
    */

    private String templateFieldsTable(String nameEntity, String nameField){
        String nameFieldUpper = FunctionUtils.firstLetterToUpperCase(nameField);
        String value = String.format("\t\t\t\t<tr>\n"+
        "\t\t\t\t\t<td>%s</td>\n"+
        "\t\t\t\t\t<td>:</td>\n"+
        "\t\t\t\t\t<td>${%s.get%s()}</td>\n"+          
        "\t\t\t\t</tr>\n",nameField,nameEntity,nameFieldUpper);
        return value;
    }

    private String generateFieldsTable(){
        String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        EntityField[] entityFields = this.viewDetail.getEntityFields();
        for (int i = 0; i < entityFields.length; i++) {
            EntityField entityField = entityFields[i];
            value += templateFieldsTable(nameEntity, entityField.getName());
        }
        return value;
    }

    /*
     * Fonction mi genere tableau du child entity
     * - mi-creer template anah tableau de child Entity
     * - mi-creer template Column
     * - mi-creer template rows
     */
    private String templateColumnChild(String nameTableChild){
        String value = "";
        String [] columns = this.dbManager.getColumnsFromTable(nameTableChild); 
        for (String column : columns) {
            value += String.format("\t\t\t\t\t\t\t\t<th>%s</th>\n",column);
        } 
        return value;
    }

    private String templateRowschild(String nameEntity,String nameTableChild){
        String value = "";
        String nameTableChildUpper = FunctionUtils.firstLetterToUpperCase(nameTableChild);
        String [] nameColumns = this.dbManager.getColumnsFromTable(nameTableChild); 
        for (String nameColumn : nameColumns) {
        	String nameColumnUpper = FunctionUtils.firstLetterToUpperCase(nameColumn);
            value += String.format("\t\t\t\t\t\t\t\t<td>${%s.get%s().get%s()}</td>\n",nameEntity,nameTableChildUpper,nameColumnUpper);
        } 
        return value;
    }

    private String templateFieldChildTable(String nameEntity,String nameEntityChild){
        String value = "\t\t\t\t<tr>\r\n" + //
                        "\t\t\t\t\t<td>"+nameEntityChild+"</td>\r\n" + //
                        "\t\t\t\t\t<td>:</td>\r\n" + //
                        "\t\t\t\t\t<td>\r\n" + //
                        "\t\t\t\t\t\t<table table border=\"1\">\r\n" + //
                        "\t\t\t\t\t\t\t<tr>\r\n" + this.templateColumnChild(nameEntityChild) +
                        "\t\t\t\t\t\t\t</tr>\r\n" + //
                        "\t\t\t\t\t\t\t<tr>\r\n" + this.templateRowschild(nameEntity, nameEntityChild)+ //
                        "\t\t\t\t\t\t\t</tr>\r\n" + //
                        "\t\t\t\t\t\t</table>\r\n" + //
                        "\t\t\t\t\t</td>\r\n" + //
                        "\t\t\t\t</tr>";
        return value;
    }

    private String generateFieldChildTable(){
        String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORM = configORM.listORMConfig();
        if(listConfigORM.length<0){
            return "";
        }
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String parentTable = configORMIndex.getName_table_parent();
            String childTable = configORMIndex.getName_table_child();
            if(nameEntity.equals(parentTable)){
                value += this.templateFieldChildTable(nameEntity,childTable);
            }
            else {
                value ="";
            }
        }
        return value;
    }

    private String [] listRealValues(){
        String nameEntity = this.viewDetail.getNameTable();
        String [] values = {
            nameEntity,
            generateFieldsTable(),
            generateFieldChildTable()
        };
        return values;
    }

    private String formatEntityNameDetail(){
        return this.viewDetail.getNameTable()+"-detail";
    }

    public void createViewDetailEntity(){
        File destinationFile = new File(Config.VIEWDETAIL_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameDetail()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameDetail()), Config.placeHoldersViewDetail, listRealValues(),Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWDETAIL_SOURCE_FILE_NAME,Config.VIEWDETAIL_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameDetail())+" :File Already exist...");
        }  
    }

}
