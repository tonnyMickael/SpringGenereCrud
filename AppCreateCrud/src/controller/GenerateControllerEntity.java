package controller;

import java.io.File;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import function.FunctionUtils;
import model.EntityField;

public class GenerateControllerEntity {
    private ControllerEntity controllerEnity;
    
    public GenerateControllerEntity(ControllerEntity controllerEnity) {
        this.controllerEnity = controllerEnity;
    }

    public ControllerEntity getControllerEnity() {
        return controllerEnity;
    }

    public void setControllerEnity(ControllerEntity controllerEnity) {
        this.controllerEnity = controllerEnity;
    }

    /*
     * Create the condition if folders sent are not null from update url
     */


    private String isAnyFieldNullOrEmpty(){
        String entityName = this.controllerEnity.getNameEntity();
        EntityField [] entityFields = this.controllerEnity.getEntityFields();
        String newValue = "";
        for (int i = 1; i < entityFields.length; i++) {
            String fieldNameMaj = FunctionUtils.firstLetterToUpperCase(entityFields[i].getName());
            String formatValue = "";
            if(entityFields[i].getType().equals("String")){
                formatValue = String.format("%s.get%s() == null || %s.get%s().trim().isEmpty() || ", entityName,fieldNameMaj,entityName,fieldNameMaj);
            }
            else if (entityFields[i].getType().equals("int") || entityFields[i].getType().equals("double")){
                formatValue = String.format("%s.get%s() == 0 || ", entityName,fieldNameMaj);
            }
            else if (entityFields[i].getType().equals("java.time.LocalDate") || entityFields[i].getType().equals("java.sql.Timestamp") || entityFields[i].getType().equals("java.sql.Time")){
                formatValue = String.format("%s.get%s() == null || ", entityName,fieldNameMaj);
            }
            else {
                formatValue = String.format("!%s.get%s() == null || ", entityName,fieldNameMaj);
            }
            newValue += formatValue;
            // produit.getDesignation() == null || produit.getDesignation().trim().isEmpty() ||  produit.getPrix() == 0
            
        }
        // return newValue.trim();

        return newValue.substring(0, newValue.lastIndexOf("|| ")).trim();
    }

    private String isChildEntityNull(){
        String newValue ="";
        String entityName = this.controllerEnity.getNameEntity();
        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORM = configORM.listORMConfig();
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String parentEntity = configORMIndex.getName_table_parent();
            String childEntity = configORMIndex.getName_table_child();
            String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
            if(entityName.equals(parentEntity)){
                newValue += String.format("|| %s.get%s() == null", entityName,childEntityUpper);
                break;
            }
        }
        return newValue.trim();
    }

    private String generateChildEntityInIt(String blocksName){
        String value = "";
        String nameEntity = this.controllerEnity.getNameEntity();
        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORM = configORM.listORMConfig();
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String idTypeEntity = this.controllerEnity.getIdTypEntity();
            String parentEntity = configORMIndex.getName_table_parent();
            String childEntity = configORMIndex.getName_table_child();
            String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
            String assocParent = configORMIndex.getAssoc_parent_child();
            String assocChild = configORMIndex.getAssoc_child_parent();
            if(blocksName.equals("Autowired") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
                value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);
                break;
            }
            else if(blocksName.equals("RequestParam") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
                value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                break;
            }
            else if(blocksName.equals("AttributeChildEntity") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
                value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                break;
            }
            else if(blocksName.equals("setChildEntity") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
                value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                break;
            }
            else{
                value = "";
            }
        }
        return value;
    }

    private String [] listRealValues(){
        String nameEntityMaj = FunctionUtils.firstLetterToUpperCase(this.controllerEnity.getNameEntity());
        String nameEntity =  this.controllerEnity.getNameEntity();
        String idType = this.controllerEnity.getIdTypEntity();
        String [] values = {
            nameEntityMaj,
            nameEntity,
            idType,
            this.isAnyFieldNullOrEmpty()+isChildEntityNull(),
            this.generateChildEntityInIt("Autowired"),
            this.generateChildEntityInIt("RequestParam"),
            this.generateChildEntityInIt("AttributeChildEntity"),
            this.generateChildEntityInIt("setChildEntity")
        };
        return values;
    }
    private String formatClassNameController(){
        return this.controllerEnity.getNameEntity()+"Controller";
    }

    public void createControllerEntity(){
        File destinationFile = new File(ConfigSystem.path + Config.CONTROLLER_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(formatClassNameController()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(formatClassNameController()), Config.placeHoldersController, listRealValues(),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,ConfigSystem.path + Config.TEMPLATE_CONTROLLER_SOURCE_FILE_NAME,ConfigSystem.path + Config.CONTROLLER_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(formatClassNameController())+" :File Already exist...");
        }  
    }
    
}
