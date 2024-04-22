package view;

import java.io.File;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ViewModifieParentInChildGenerate {
    private ViewModifieParentInChild viewModifieParentInChild;
    private DBManager dbManager;
    private String navBarLogOut;

    public String getNavBarLogOut() {
        return navBarLogOut;
    }

    public void setNavBarLogOut(String navBarLogOut) {
        this.navBarLogOut = navBarLogOut;
    }

    public ViewModifieParentInChild getViewModifieParentInChild() {
        return viewModifieParentInChild;
    }

    public void setViewModifieParentInChild(ViewModifieParentInChild viewModifieParentInChild) {
        this.viewModifieParentInChild = viewModifieParentInChild;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public ViewModifieParentInChildGenerate(ViewModifieParentInChild viewModifieParentInChild, DBManager dbManager) {
        this.viewModifieParentInChild = viewModifieParentInChild;
        this.dbManager = dbManager;
    }
    private String templateFieldsTable(String nameEntity, String nameField){
        // String nameFieldUpper = FunctionUtils.firstLetterToUpperCase(nameField);
        String value = String.format("\t\t\t\t\t<tr class=\"text-muted w-lg-50\">\n"+
        "\t\t\t\t\t\t<td>%s</td>\n"+
        "\t\t\t\t\t\t<td>:</td>\n"+
        "\t\t\t\t\t\t<td>${%s.%s}</td>\n"+          
        "\t\t\t\t\t</tr>\n",nameField,nameEntity,nameField);
        return value;
    }

    private String generateFieldsTable(String nameEntity){
        // String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        EntityField[] entityFields = this.dbManager.entityFieldColumnsFromTable(nameEntity);
        for (int i = 0; i < entityFields.length; i++) {
            EntityField entityField = entityFields[i];
            value += templateFieldsTable(nameEntity, entityField.getName());
        }
        return value;
    }
    public String[] listRealValues(EntityField [] entityField,String entityInInput, String entityInDropDown) {

        // String inputs = FunctionUtils.generateInput(entityField,entityInInput);
        String fieldsTable = this.generateFieldsTable(entityInInput);
        String attributName = this.dbManager.columnContainsName(entityInDropDown);
        String entityInDropDownMaj = FunctionUtils.firstLetterToUpperCase(entityInDropDown);
        if(this.viewModifieParentInChild.getNameEntity().equals(navBarLogOut)){
            String[] values = { 
                entityInInput, 
                entityInDropDown, 
                entityInDropDownMaj,
                fieldsTable, 
                attributName,
                FunctionUtils.logOutTmpl() 
            };
            return values;            
        }
        else{
            String[] values = { 
                entityInInput, 
                entityInDropDown, 
                entityInDropDownMaj,
                fieldsTable, 
                attributName,
                "" 
            };
            return values;
        }

    }

    private String formatEntityNameModife(String childEntity, String parentEntity){
        return childEntity+"-"+parentEntity+"-edit-form";
    }

    public void createViewModifeParentInChild(ConfigORM configORM){
        // EntityField [] entityFields = this.viewModifieChildInParent.getEntityFields();
        String nameEntity = this.viewModifieParentInChild.getNameEntity();
        String parentEntity = configORM.getName_table_parent();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        String childEntity = configORM.getName_table_child();
        
        //Ajouter parent dans child
        if(nameEntity.equals(childEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal){
                EntityField [] entityFields = this.dbManager.entityFieldColumnsFromTable(parentEntity);
                String[] realValues = listRealValues(entityFields,childEntity,parentEntity);
                File destinationFile = new File(Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameModife(childEntity,parentEntity)));
                
                if(!destinationFile.exists()){
                    FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameModife(childEntity,parentEntity)), Config.placeHoldersViewUpdateParentInChild,realValues,ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSUPDATE_PARENT_IN_CHILD_SOURCE_FILE_NAME,ConfigSystem.path + Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH);
                }
                else{
                    System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameModife(childEntity,parentEntity))+" :File Already exist...");
                }  
        }
    }

}
