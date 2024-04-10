package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ViewModifeGenerate {
    private ViewModife viewModife;
    private DBManager dbManager;

    public ViewModifeGenerate(ViewModife viewModife, DBManager dbManager){
        this.dbManager = dbManager;
        this.viewModife=viewModife;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * @return ViewModife return the viewModife
     */
    public ViewModife getViewModife() {
        return viewModife;
    }

    /**
     * @param viewModife the viewModife to set
     */
    public void setViewModife(ViewModife viewModife) {
        this.viewModife = viewModife;
    }

    private String templateinput(EntityField entityField,String nameEntity){
        String newvalue="";
        String namecolumn = entityField.getName();
        String namecolumnupper=FunctionUtils.firstLetterToUpperCase(namecolumn);
        String nametable = nameEntity;
        String type = entityField.getType();
        String typehtml=FunctionUtils.change(namecolumn,type);
        if (typehtml.equals("number")) {
            newvalue += "\t\t<p>"+namecolumn+":<input class=\"form-control\" name=\"" + namecolumn + "\" step =\"0.01\" type=\"" + typehtml + "\" value=\"${(" + nametable + ".get" + namecolumnupper + "()?string[\"0.###\"]?replace(\",\", \".\"))!}\"/><br></p>\n";
        }
        else{
            newvalue = "\t\t<p>"+namecolumn+":<input class=\"form-control\"name=\"" + namecolumn + "\" type=\"" + typehtml + "\" value=\"${" + nametable + ".get" + namecolumnupper + "()}\"/><br></p>\n";
        }
        return newvalue;
    }

    public String generateInput(EntityField [] entityField,String nameEntity){
        String result="";
        for(EntityField entityFields : entityField){
            // result.append(templateinput(entityFields,entitys));
            if (!entityFields.getName().equals("id")) {
                result += templateinput(entityFields, nameEntity);
            }
        }
        return result;
    }

    // Template DropDown
    private String templateDropDown(String entityCaller,String entityToSelect){
        String attributName = FunctionUtils.firstLetterToUpperCase(this.dbManager.columnContainsName(entityToSelect));
        String entityToSelectUpper = FunctionUtils.firstLetterToUpperCase(entityToSelect);
        String value = "<p>[entityToSelect]:<select name=\"[entityToSelect]_id\" class=\"form-control\">\n\t\t\t\t"+
                            "<#list list[entityToSelectUpper] as [entityToSelect]>\n\t\t\t\t\t"+
                                "<option value=\"${[entityToSelect].getId()}\" <#if [entityCaller].get[entityToSelectUpper]().getId() == [entityToSelect].getId()>selected</#if>>${[entityToSelect].get[attributName]()}</option>\n\t\t\t\t"+
                            "</#list>\n\t\t\t"+
                        "</select></p>";
        value = value.replace("[entityToSelect]", entityToSelect);
        value = value.replace("[entityCaller]", entityCaller);
        value = value.replace("[entityToSelectUpper]", entityToSelectUpper);
        value = value.replace("[attributName]", attributName);

        return value;
    }
    private String templateDropDownNoDefaultValue(String entityCaller,String entityToSelect){
        String attributName = FunctionUtils.firstLetterToUpperCase(this.dbManager.columnContainsName(entityToSelect));
        String entityToSelectUpper = FunctionUtils.firstLetterToUpperCase(entityToSelect);
        String value = "<p>[entityToSelect]:<select name=\"[entityToSelect]_id\" class=\"form-control \">\n\t\t\t\t"+
                            "<#list list[entityToSelectUpper] as [entityToSelect]>\n\t\t\t\t\t"+
                                "<option value=\"${[entityToSelect].getId()}\">${[entityToSelect].get[attributName]()}</option>\n\t\t\t\t"+
                            "</#list>\n\t\t\t"+
                        "</select></p>";
        value = value.replace("[entityToSelect]", entityToSelect);
        value = value.replace("[entityCaller]", entityCaller);
        value = value.replace("[entityToSelectUpper]", entityToSelectUpper);
        value = value.replace("[attributName]", attributName);

        return value;
    }

    private String generateInputFK(ConfigORM configORM){
        String nameEntity = this.viewModife.getNameEntity();
        String value = "";
        String parentTable = configORM.getName_table_parent();
        String childTable = configORM.getName_table_child();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String inputFkParent = configORM.getInputFkParent();
        String inputFkChild = configORM.getInputFkChild();
        String optionConfiguration = configORM.getOptionConfiguration();

        if(nameEntity.equals(childTable) && inputFkChild.equals("2")){
            // value += this.templateDropDown(nameEntity,parentTable);
            value += this.templateDropDownNoDefaultValue(nameEntity,parentTable);

        }
        else if (nameEntity.equals(parentTable) && inputFkParent.equals("2")){
            // value += this.templateDropDown(nameEntity,childTable);
            value += this.templateDropDownNoDefaultValue(nameEntity,parentTable);

        }
        else {
            value = "";
        }
        return value;
    }

    public String[] listRealValues(EntityField [] entityField,String nameEntity,ConfigORM configORM) {
        // String nameEntity = this.viewModife.getNameEntity();
        String inputs = generateInput(entityField,nameEntity);
        String inputFK = generateInputFK(configORM);
        String[] values = { nameEntity, inputs, inputFK };
        return values;
    }

    private String formatEntityNameModife(){
        return this.viewModife.getNameEntity()+"-edit-form";
    }

    public void createViewModifeEntity(ConfigORM configORM){
        EntityField [] entityFields = this.viewModife.getEntityFields();
        String nameEntity=this.viewModife.getNameEntity();
        // Ajoutez des objets InputGenere à inputGeneres

        // Configuration
        // String typeInputFK = configORM.getInputFkParent();

        String[] realValues = listRealValues(entityFields,nameEntity,configORM);
        File destinationFile = new File(ConfigSystem.path + Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameModife()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameModife()), Config.placeHoldersViewUpdate,realValues,ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSUPDATE_SOURCE_FILE_NAME,ConfigSystem.path + Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameModife())+" :File Already exist...");
        }  
    }

}
