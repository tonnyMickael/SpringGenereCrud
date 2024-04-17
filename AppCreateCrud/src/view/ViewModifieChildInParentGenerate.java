package view;

import java.io.File;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ViewModifieChildInParentGenerate {
    private ViewModifieChildInParent viewModifieChildInParent;
    private DBManager dbManager;
    
    public ViewModifieChildInParentGenerate(ViewModifieChildInParent viewModifieChildInParent, DBManager dbManager) {
        this.viewModifieChildInParent = viewModifieChildInParent;
        this.dbManager = dbManager;
    }

    public ViewModifieChildInParent getViewModifieChildInParent() {
        return viewModifieChildInParent;
    }

    public void setViewModifieChildInParent(ViewModifieChildInParent viewModifieChildInParent) {
        this.viewModifieChildInParent = viewModifieChildInParent;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public String[] listRealValues(EntityField [] entityField,String entityInInput, String entityInDropDown) {

        String inputs = FunctionUtils.generateInput(entityField,entityInInput);
        String attributName = this.dbManager.columnContainsName(entityInDropDown);
        // String inputFK = generateInputFK(configORM);
        String[] values = { entityInInput, entityInDropDown, inputs, attributName };
        return values;
    }

    private String formatEntityNameModife(String parentEntity, String childEntity){
        return parentEntity+"-"+childEntity+"-edit-form";
    }

    public void createViewModifeChildInParent(ConfigORM configORM){
        // EntityField [] entityFields = this.viewModifieChildInParent.getEntityFields();
        String nameEntity=this.viewModifieChildInParent.getNameEntity();
        String parentEntity = configORM.getName_table_parent();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        String childEntity = configORM.getName_table_child();
        // Ajoutez des objets InputGenere à inputGeneres
        if(nameEntity.equals(parentEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isBiDirectionnal){

                EntityField [] entityFields = this.dbManager.entityFieldColumnsFromTable(childEntity);
                String[] realValues = listRealValues(entityFields,childEntity,parentEntity);
                File destinationFile = new File(Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameModife(parentEntity,childEntity)));
                
                if(!destinationFile.exists()){
                    FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameModife(parentEntity,childEntity)), Config.placeHoldersViewUpdateChildInParent,realValues,ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSUPDATE_CHILD_IN_PARENT_SOURCE_FILE_NAME,ConfigSystem.path+Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH);
                }
                else{
                    System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameModife(parentEntity,childEntity))+" :File Already exist...");
                }  
        }

        // Ajout des Object Input Parents dans Enfants
        else if(nameEntity.equals(parentEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal){
                EntityField [] entityFields = this.dbManager.entityFieldColumnsFromTable(childEntity);
                String[] realValues = listRealValues(entityFields,childEntity,parentEntity);
                File destinationFile = new File(Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameModife(parentEntity,childEntity)));
                
                if(!destinationFile.exists()){
                    FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameModife(parentEntity,childEntity)), Config.placeHoldersViewUpdateChildInParent,realValues,ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSUPDATE_CHILD_IN_PARENT_SOURCE_FILE_NAME,ConfigSystem.path+Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH);
                }
                else{
                    System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameModife(parentEntity,childEntity))+" :File Already exist...");
                }  
        }
    }

}
