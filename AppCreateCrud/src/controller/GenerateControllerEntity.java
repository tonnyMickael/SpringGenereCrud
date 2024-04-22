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

    private String isChildEntityNull(ConfigORM configORM){
        String newValue ="";
        String entityName = this.controllerEnity.getNameEntity();
        String parentEntity = configORM.getName_table_parent();
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntity = configORM.getName_table_child();
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
        String assocParent = configORM.getAssoc_parent_child();
        String assocChild = configORM.getAssoc_child_parent();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String optConfiguration = configORM.getOptionConfiguration();

        // if(entityName.equals(parentEntity)){
        //     newValue += String.format("|| %s.get%s() == null", entityName,childEntityUpper);
        // }
        // Unidirectional 
        // 1-1 et 1-1
        // Option 2
        if( entityName.equals(parentEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                newValue += String.format("|| %s.get%s() == null", parentEntity,childEntityUpper);
        }
        // Option 1
        else if(entityName.equals(childEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                newValue += String.format("|| %s.get%s() == null", childEntity,parentEntityUpper);
        }
        // Bidirectional
        // 1-1 and 1-1 
        else if( entityName.equals(parentEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal ){
                newValue += String.format("|| %s.get%s() == null", parentEntity,childEntityUpper);
        }
        else if( entityName.equals(childEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal ){
                newValue += String.format("|| %s.get%s() == null", childEntity,parentEntityUpper);
        }
        // Unidirectional
        // 1-n and 1-1
        else if(entityName.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+childEntityUpper);
                newValue += String.format("|| %s.get%s() == null", parentEntity,nameFunction);
        }
        else if(entityName.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                newValue += String.format("|| %s.get%s() == null", childEntity,parentEntityUpper);
        }
        // Bidirectionl
        else if(entityName.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal){
                newValue += "";
                // String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+childEntityUpper);
                // newValue += String.format("|| %s.get%s() == null", parentEntity,nameFunction);
        }
        else if(entityName.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal){
                newValue += String.format("|| %s.get%s() == null", childEntity,parentEntityUpper);
        }
        // Unidirectional
        // 1-n and 1-n
        // option 2
        else if(entityName.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+childEntityUpper);
                newValue += String.format("|| %s.get%s() == null", parentEntity,nameFunction);
        }
        else if(entityName.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+parentEntityUpper);
                newValue += String.format("|| %s.get%s() == null", childEntity,nameFunction);
        }
        // Bidirectionnal
        else if(entityName.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isBiDirectionnal){
                newValue += "";
                // String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+childEntityUpper);
                // newValue += String.format("|| %s.get%s() == null", parentEntity,nameFunction);
        }
        // else if(entityName.equals(childEntity) && 
        //     assocParent.equals("1-N") && 
        //     assocChild.equals("1-N") && 
        //     isBiDirectionnal){
        //         String nameFunction = FunctionUtils.firstLetterToUpperCase("list"+parentEntityUpper);
        //         newValue += String.format("|| %s.get%s() == null", childEntity,nameFunction);
        // }
        return newValue.trim();
    }

    private String generateChildEntityInIt(ConfigORM configORM,String blocksName){
        String value = "";
        String nameEntity = this.controllerEnity.getNameEntity();
        String idTypeEntity = this.controllerEnity.getIdTypEntity();
        String parentEntity = configORM.getName_table_parent();
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntity = configORM.getName_table_child();
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);
        String assocParent = configORM.getAssoc_parent_child();
        String assocChild = configORM.getAssoc_child_parent();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String optConfiguration = configORM.getOptionConfiguration();
        
        // Unidirectional 
        // 1-1 et 1-1
        // Option 2
        if( nameEntity.equals(parentEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);
                }
                else if(blocksName.equals("RequestParam")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Option 1
        else if(nameEntity.equals(childEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                if(blocksName.equals("Autowired")){
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);
                }
                else if(blocksName.equals("RequestParam") ){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Bidirectional
        // 1-1 et 1-1
        else if( nameEntity.equals(parentEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal ){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);   
                }
                else if(blocksName.equals("RequestParam")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        else if( nameEntity.equals(childEntity) && 
            assocParent.equals("1-1") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal ){
                if(blocksName.equals("Autowired")){
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);
                }
                else if(blocksName.equals("RequestParam") ){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        //Unidirectional
        // 1-N et 1-1
        // Option 2
        else if(nameEntity.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);
                    // value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParam")){
                    value = "";
                    // value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    // value += String.format("theModel.addAttribute(\"%s\", %s);",parentEntity, parentEntity );
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );

                }
                else if(blocksName.equals("setChildEntity")){
                    value = "";
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // ChildEntity
        else if(nameEntity.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                if (blocksName.equals("Autowired")) {
                    // value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParam")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    // value += String.format("theModel.addAttribute(\"%s\", %s);",childEntity, childEntity );
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );

                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s dropDownSelected = %sService.detail(%s_id);\r\n\t\tdropDownSelected.getList%s().add(%s);\n",parentEntityUpper, parentEntity ,parentEntity,childEntityUpper,childEntity);
                    value += String.format("\r\t\t%sService.add(%s);\r\n\t\t%sService.add(dropDownSelected);\n",childEntity, childEntity ,parentEntity);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Option 1
        else if (nameEntity.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParam")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Bidirectional
        // 1-N et 1-1
        else if (nameEntity.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);   
                }
                else if(blocksName.equals("RequestParamInAdd")){
                    // value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                    value += "";
                }
                else if(blocksName.equals("RequestParamInUpdate")){
                    // value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                    value += "";
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                }
                else if(blocksName.equals("setChildEntityInUpdate")){
                    value += String.format("%s %sObj = this.%sService.detail(theId);\n"+
                    "\t\t%s.setList%s(%sObj.getList%s());\n",parentEntityUpper,parentEntity,parentEntity,parentEntity,childEntityUpper,parentEntity,childEntityUpper);
                   // value += "";
                   // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("setChildEntityInAdd")){
                    value += "";
                }
                else if(blocksName.equals("supplementMethods")){
                    // value += this.generateSupplementMethods(idTypeEntity,parentEntity, childEntity);
                    value += this.templateAddOneToManyChildInDetailParentGet(idTypeEntity, parentEntity, childEntity); 
                    value += this.templateAddOneToManyChildInDetailParentPost(idTypeEntity, parentEntity, childEntity);
                    value += this.templateDeleteChildInDetailParent(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateChildInDetailParentGet(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateChildInDetailParentPost(idTypeEntity, parentEntity, childEntity);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        else if (nameEntity.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-1") && 
            isBiDirectionnal){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParamInAdd")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("RequestParamInUpdate")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntityInAdd")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("setChildEntityInUpdate")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Unidirectional
        // 1-N et 1-N
        // Option 2
        else if (nameEntity.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isUniDirectionnal && optConfiguration.equals("2")){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);   
                }
                else if(blocksName.equals("RequestParam")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Option 1
        else if (nameEntity.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isUniDirectionnal && optConfiguration.equals("1")){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParam") ){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntity")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",parentEntityUpper, parentEntity ,parentEntity,nameEntity,parentEntityUpper);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Bidirectional
        // else if (nameEntity.equals(parentEntity) && 
        //     assocParent.equals("1-N") && 
        //     assocChild.equals("1-N") && 
        //     isBiDirectionnal ){
        //         if (blocksName.equals("Autowired")) {
        //             value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);   
        //         }
        //         else if(blocksName.equals("RequestParamInAdd")){
        //             value += "";
        //             // value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
        //         }
        //         else if(blocksName.equals("RequestParamInUpdate")){
        //             value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
        //         }
        //         else if(blocksName.equals("AttributeChildEntity")){
        //             value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
        //         }
        //         else if(blocksName.equals("setChildEntityInAdd")){
        //             value += "";
        //             // value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
        //             // "\t\t%s.setList%s(Arrays.asList(childSelected));\n",childEntityUpper,childEntity,childEntity,parentEntity,childEntityUpper);
        //             // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
        //         }
        //         else if(blocksName.equals("setChildEntityInUpdate")){
        //             value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
        //             "\t\t%s.setList%s(Arrays.asList(childSelected));\n",childEntityUpper,childEntity,childEntity,parentEntity,childEntityUpper);
        //             // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
        //         }
        //         else if(blocksName.equals("insidUpdateMethodInElse")){
        //             value += parentEntityUpper+" "+parentEntity+"Update = this."+parentEntity+"Service.detail(theId);\n"+
        //             "\t\t\t\tList<"+childEntityUpper+"> "+childEntity+"In"+parentEntityUpper+" =  "+parentEntity+"Update.getList"+childEntityUpper+"();\n"+
        //             "\t\t\t\t"+parentEntity+"Update.setList"+childEntityUpper+"("+childEntity+"In"+parentEntityUpper+");\n"+
        //             "\t\t\t\t"+parentEntity+".setList"+childEntityUpper+"("+childEntity+"In"+parentEntityUpper+");";    
        //         }
        //         else {
        //             value += "";
        //         }
        // }
        else if (nameEntity.equals(childEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isBiDirectionnal ){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",parentEntityUpper, parentEntity);   
                }
                else if(blocksName.equals("RequestParamInAdd")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                    // value += "";
                }
                else if(blocksName.equals("RequestParamInUpdate")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",parentEntity, idTypeEntity ,parentEntity);
                    // value += "";
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",parentEntityUpper, parentEntity );
                }
                else if(blocksName.equals("setChildEntityInAdd")){
                    value += "";
                    value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
                    "\t\t%s.setList%s(Arrays.asList(childSelected));\n",parentEntityUpper,parentEntity,parentEntity,childEntity,parentEntityUpper);
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("setChildEntityInUpdate")){
                    // value += "";
                    value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
                    "\t\t%s.setList%s(Arrays.asList(childSelected));\n",parentEntityUpper,parentEntity,parentEntity,childEntity,parentEntityUpper);
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("supplementMethods")){
                    value += this.templateAddManyToManyParentInDetailChildGet(idTypeEntity, parentEntity, childEntity); 
                    value += this.templateAddManyToManyParentInDetailParentPost(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateManyToManyParentInDetailChildGet(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateManyToManyParentInDetailChildPost(idTypeEntity, parentEntity, childEntity);
                    value += this.templateDeleteManyToManyParentInDetailChild(idTypeEntity, parentEntity, childEntity);
                }
                else if(blocksName.equals("insidUpdateMethodInElse")){
                    value += "";    
                }
                else {
                    value += "";
                }
        }
        // Bidirectional
        else if (nameEntity.equals(parentEntity) && 
            assocParent.equals("1-N") && 
            assocChild.equals("1-N") && 
            isBiDirectionnal ){
                if (blocksName.equals("Autowired")) {
                    value += String.format("@Autowired\r\n\tprivate %sService %sService;",childEntityUpper, childEntity);   
                }
                else if(blocksName.equals("RequestParamInAdd")){
                    value += "";
                    // value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("RequestParamInUpdate")){
                    value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
                }
                else if(blocksName.equals("AttributeChildEntity")){
                    value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
                }
                else if(blocksName.equals("setChildEntityInAdd")){
                    value += "";
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
                    // "\t\t%s.setList%s(Arrays.asList(childSelected));\n",childEntityUpper,childEntity,childEntity,parentEntity,childEntityUpper);
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("setChildEntityInUpdate")){
                    value += String.format("%s childSelected = %sService.detail(%s_id);\n"+
                    "\t\t%s.setList%s(Arrays.asList(childSelected));\n",childEntityUpper,childEntity,childEntity,parentEntity,childEntityUpper);
                    // value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
                }
                else if(blocksName.equals("supplementMethods")){
                    value += this.templateAddManyToManyChildInDetailParentGet(idTypeEntity, parentEntity, childEntity); 
                    value += this.templateAddManyToManyChildInDetailParentPost(idTypeEntity, parentEntity, childEntity);
                    value += this.templateDeleteManyToManyChildInDetailParent(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateManyToManyChildInDetailParentGet(idTypeEntity, parentEntity, childEntity);
                    value += this.templateUpdateManyToManyChildInDetailParentPost(idTypeEntity, parentEntity, childEntity);
                }
                else {
                    value += "";
                }
        }
        // else if(blocksName.equals("RequestParam") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
        //     value += String.format(", @RequestParam(\"%s_id\") %s %s_id",childEntity, idTypeEntity ,childEntity);
        // }
        // else if(blocksName.equals("AttributeChildEntity") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
        //     value += String.format("theModel.addAttribute(\"list%s\", %sService.fetch());",childEntityUpper, childEntity );
        // }
        // else if(blocksName.equals("setChildEntity") && nameEntity.equals(parentEntity)&& assocParent.equals("@OneToOne") && assocChild.equals("")){
        //     value += String.format("%s childSelected = %sService.detail(%s_id);\r\n\t\t%s.set%s(childSelected);\t",childEntityUpper, childEntity ,childEntity,nameEntity,childEntityUpper);
        // }

        else{
            value = "";
        }
        return value;
    }

    // OneToMany Bidirectional
    private String templateAddOneToManyChildInDetailParentGet(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/{id}/"+childEntity+"/add\")\n"+
        "\tpublic String add"+childEntityUpper+"InDetail"+parentEntityUpper+"(Model theModel, @PathVariable(\"id\") "+idType+" id) {\n"+
            "\t\t"+childEntityUpper+" "+childEntity+" = new "+childEntityUpper+"();\n"+
            "\t\ttry {\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = "+parentEntity+"Service.detail(id);\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+parentEntity+");\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+parentEntity+"-"+childEntity+"-form\";\n"+
        "\t}\n\n"; 
        return value;
    }

    private String templateAddOneToManyChildInDetailParentPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+childEntity+"/add\")\n"+
        "\tpublic String save"+childEntityUpper+"InDetail"+parentEntityUpper+"(@ModelAttribute(\""+childEntity+"\") "+childEntityUpper+" "+childEntity+",@RequestParam(\""+parentEntity+"_id\") "+idType+" "+parentEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = new "+parentEntityUpper+"();\n"+
                "\t\t\t"+parentEntity+".setId("+parentEntity+"_id);\n"+
                "\t\t\t"+childEntity+".set"+parentEntityUpper+"("+parentEntity+");\n"+
                "\t\t\tthis."+childEntity+"Service.add("+childEntity+");\n"+
                "\t\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }

    private String templateDeleteChildInDetailParent(String idType, String parentEntity, String childEntity){
        // String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@GetMapping(\"/detail/"+childEntity+"/delete/{"+parentEntity+"_id}/{id}\")\n"+
        "\tpublic String delete"+childEntityUpper+"InDetail(@PathVariable(\""+parentEntity+"_id\") Integer "+parentEntity+"_id, @PathVariable(\"id\") "+idType+" theId) {\n"+
            "\t\t"+childEntity+"Service.delete(theId);\n"+
            "\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
        "\t}\n\n";
        return value;
    }

    private String templateUpdateChildInDetailParentGet(String idType,String parentEntity, String childEntity){ 
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/"+childEntity+"/update/{id}\")\n"+
        "\tpublic String update"+childEntityUpper+"InDetailForm (Model theModel, @PathVariable(\"id\") "+idType+" id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = "+childEntity+"Service.detail(id);\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+childEntity+".get"+parentEntityUpper+"());\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+parentEntity+"-"+childEntity+"-edit-form\";\n"+
        "\t}\n\n"; 
        return value;  
    }

    private String templateUpdateChildInDetailParentPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+childEntity+"/update\")\n"+
        "\tpublic String update"+childEntityUpper+"InDetail"+parentEntityUpper+"(@ModelAttribute(\""+childEntity+"\") "+childEntityUpper+" "+childEntity+",@RequestParam(\""+parentEntity+"_id\") Integer "+parentEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = this."+parentEntity+"Service.detail("+parentEntity+"_id);\n"+
                "\t\t\t"+childEntity+".set"+parentEntityUpper+"("+parentEntity+");\n"+
                "\t\t\tthis."+childEntity+"Service.update("+childEntity+", "+childEntity+".getId());\n"+
                "\t\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }
    
    // ManyToMany Bidirectional
    // From Child to add region
    private String templateAddManyToManyParentInDetailChildGet(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/{id}/"+parentEntity+"/add\")\n"+
        "\tpublic String add"+parentEntityUpper+"InDetail"+childEntityUpper+"(Model theModel, @PathVariable(\"id\") "+idType+" id) {\n"+
            "\t\t"+parentEntityUpper+" "+parentEntity+" = new "+parentEntityUpper+"();\n"+
            "\t\ttry {\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+parentEntity+");\n"+
                "\t\t\ttheModel.addAttribute(\"list"+parentEntityUpper+"\", this."+parentEntity+"Service.fetch());\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = "+childEntity+"Service.detail(id);\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+childEntity+"-"+parentEntity+"-form\";\n"+
        "\t}\n\n"; 
        return value;
    }

    private String templateAddManyToManyParentInDetailParentPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+parentEntity+"/add\")\n"+
        "\tpublic String save"+parentEntityUpper+"InDetail"+childEntityUpper+"(@ModelAttribute(\""+parentEntity+"\") "+parentEntityUpper+" "+parentEntity+",@RequestParam(\""+childEntity+"_id\") "+idType+" "+childEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = this."+childEntity+"Service.detail("+childEntity+"_id);\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+"Obj = this."+parentEntity+"Service.detail("+parentEntity+".getId());\n"+
                "\t\t\t"+childEntity+".getList"+parentEntityUpper+"().add("+parentEntity+"Obj); \n"+
                "\t\t\tthis."+childEntity+"Service.add("+childEntity+");\n"+
                "\t\t\treturn \"redirect:/"+childEntity+"/detail/\"+"+childEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }

    // Parent Update from child
    private String templateUpdateManyToManyParentInDetailChildGet(String idType,String parentEntity, String childEntity){ 
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/"+parentEntity+"/update/{"+childEntity+"_id}/{"+parentEntity+"_id}\")\n"+
        "\tpublic String update"+parentEntityUpper+"InDetailForm (Model theModel,  @PathVariable(\""+childEntity+"_id\") "+idType+" "+childEntity+"_id, @PathVariable(\""+parentEntity+"_id\") "+idType+" "+parentEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = "+parentEntity+"Service.detail("+parentEntity+"_id);\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = "+childEntity+"Service.detail("+childEntity+"_id);\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+parentEntity+");\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
                "\t\t\ttheModel.addAttribute(\"list"+parentEntityUpper+"\", this."+parentEntity+"Service.fetch());\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+childEntity+"-"+parentEntity+"-edit-form\";\n"+
        "\t}\n\n"; 
        return value;  
    }

    private String templateUpdateManyToManyParentInDetailChildPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+parentEntity+"/update\")\n"+
        "\tpublic String update"+parentEntityUpper+"InDetail"+childEntityUpper+"(@ModelAttribute(\""+parentEntity+"\") "+parentEntityUpper+" "+parentEntity+",@RequestParam(\""+parentEntity+"_id\") "+idType+" "+parentEntity+"_id, @RequestParam(\""+childEntity+"_id\") "+idType+" "+childEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = this."+childEntity+"Service.detail("+childEntity+"_id);\n"+
                "\t\t\tList<"+parentEntityUpper+"> list"+parentEntityUpper+" = "+childEntity+".getList"+parentEntityUpper+"();\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+"Updated = this."+parentEntity+"Service.detail("+parentEntity+".getId());\n"+
                "\t\t\tint indexToRemove = -1;\n"+
                "\t\t\tfor (int i = 0; i < list"+parentEntityUpper+".size(); i++) {\n"+
                "\t\t\t\tif (list"+parentEntityUpper+".get(i).getId() == ("+parentEntity+"_id)) {\n"+
                "\t\t\t\t\tindexToRemove = i;\n"+
                "\t\t\t\t\tbreak;\n"+
                "\t\t\t\t}\n"+
                "\t\t\t}\n"+
                "\t\t\tif (indexToRemove != -1) {\n"+
                "\t\t\t\tlist"+parentEntityUpper+".set(indexToRemove, "+parentEntity+"Updated);\n"+
                "\t\t\t\t"+childEntity+".setList"+parentEntityUpper+"(list"+parentEntityUpper+");\n"+
                "\t\t\t\tthis."+childEntity+"Service.update("+childEntity+", "+childEntity+".getId());\n"+
                "\t\t\t\treturn \"redirect:/person/detail/\"+"+childEntity+"_id;\n"+
                "\t\t\t}\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }
    
    private String templateDeleteManyToManyParentInDetailChild(String idType, String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@GetMapping(\"/detail/"+parentEntity+"/delete/{"+childEntity+"_id}/{id}\")\n"+
        "\tpublic String delete"+childEntityUpper+"InDetail(@PathVariable(\""+childEntity+"_id\") "+idType+" "+childEntity+"_id, @PathVariable(\"id\") "+idType+" theId) {\n"+
            "\t\ttry {\n"+
            "\t\t\t"+parentEntityUpper+" "+parentEntity+" = this."+parentEntity+"Service.detail(theId);\n"+
            "\t\t\t"+childEntityUpper+" "+childEntity+" = this."+childEntity+"Service.detail("+childEntity+"_id);\n"+
            "\t\t\tList<"+parentEntityUpper+"> list"+parentEntityUpper+" = person.getList"+parentEntityUpper+"();\n"+
            "\t\t\t"+parentEntityUpper+" "+parentEntity+"FromList =  list"+parentEntityUpper+".stream().filter("+parentEntity+"Index -> "+parentEntity+"Index.getId() == "+parentEntity+".getId()).findFirst().orElse(null);\n"+
            "\t\t\tlist"+parentEntityUpper+".remove("+parentEntity+"FromList);\n"+
            "\t\t\t"+childEntity+".setList"+parentEntityUpper+"(list"+parentEntityUpper+");\n"+
            "\t\t\tthis."+childEntity+"Service.update("+childEntity+", "+childEntity+".getId());\n"+
            "\t\t\treturn \"redirect:/"+childEntity+"/detail/\"+"+childEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
            "\t\t\tex.getMessage();\n"+
        "\t\t}\n"+
        "\t\t return null;\n"+
        "\t} \n\n";
        return value;
    }

    // From parent to add & update child
    private String templateAddManyToManyChildInDetailParentGet(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/{id}/"+childEntity+"/add\")\n"+
        "\tpublic String add"+childEntityUpper+"InDetail"+parentEntityUpper+"(Model theModel, @PathVariable(\"id\") "+idType+" id) {\n"+
            "\t\t"+childEntityUpper+" "+childEntity+" = new "+childEntityUpper+"();\n"+
            "\t\ttry {\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = "+parentEntity+"Service.detail(id);\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+parentEntity+");\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+parentEntity+"-"+childEntity+"-form\";\n"+
        "\t}\n\n"; 
        return value;
    }

    private String templateAddManyToManyChildInDetailParentPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+childEntity+"/add\")\n"+
        "\tpublic String save"+childEntityUpper+"InDetail"+parentEntityUpper+"(@ModelAttribute(\""+childEntity+"\") "+childEntityUpper+" "+childEntity+",@RequestParam(\""+parentEntity+"_id\") "+idType+" "+parentEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = this."+parentEntity+"Service.detail("+parentEntity+"_id);\n"+
                "\t\t\t"+childEntity+".setList"+parentEntityUpper+"(Arrays.asList("+parentEntity+"));\n"+
                "\t\t\tthis."+childEntity+"Service.add("+childEntity+");\n"+
                "\t\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }

    private String templateDeleteManyToManyChildInDetailParent(String idType, String parentEntity, String childEntity){
        // String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@GetMapping(\"/detail/"+childEntity+"/delete/{"+parentEntity+"_id}/{id}\")\n"+
        "\tpublic String delete"+childEntityUpper+"InDetail(@PathVariable(\""+parentEntity+"_id\") Integer "+parentEntity+"_id, @PathVariable(\"id\") "+idType+" theId) {\n"+
            "\t\t"+childEntity+"Service.delete(theId);\n"+
            "\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
        "\t}\n\n";
        return value;
    }

    private String templateUpdateManyToManyChildInDetailParentGet(String idType,String parentEntity, String childEntity){ 
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "@GetMapping(\"/detail/"+childEntity+"/update/{"+parentEntity+"_id}/{"+childEntity+"_id}\")\n"+
        "\tpublic String update"+childEntityUpper+"InDetailForm (Model theModel, @PathVariable(\""+parentEntity+"_id\") "+idType+" "+parentEntity+"_id, @PathVariable(\""+childEntity+"_id\") "+idType+" "+childEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+" = "+childEntity+"Service.detail("+childEntity+"_id);\n"+
                "\t\t\t"+parentEntityUpper+" "+parentEntity+" = "+parentEntity+"Service.detail("+parentEntity+"_id);\n"+
                "\t\t\ttheModel.addAttribute(\""+childEntity+"\", "+childEntity+");\n"+
                "\t\t\ttheModel.addAttribute(\""+parentEntity+"\", "+parentEntity+");\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\ttheModel.addAttribute(\"errorMessage\", ex.getMessage());\n"+
            "\t\t}\n"+
            "\t\treturn \""+parentEntity+"-"+childEntity+"-edit-form\";\n"+
        "\t}\n\n"; 
        return value;  
    }
   
    // From parent to update child
    private String templateUpdateManyToManyChildInDetailParentPost(String idType,String parentEntity, String childEntity){
        String parentEntityUpper = FunctionUtils.firstLetterToUpperCase(parentEntity);
        String childEntityUpper = FunctionUtils.firstLetterToUpperCase(childEntity);

        String value = "\t@PostMapping(\"/detail/"+childEntity+"/update\")\n"+
        "\tpublic String update"+childEntityUpper+"InDetail"+parentEntityUpper+"(@ModelAttribute(\""+childEntity+"\") "+childEntityUpper+" "+childEntity+", @RequestParam(\"id\") "+idType+" "+childEntity+"_id, @RequestParam(\""+parentEntity+"_id\") Integer "+parentEntity+"_id) {\n"+
            "\t\ttry {\n"+
                "\t\t\t"+childEntityUpper+" "+childEntity+"ToUpdate = this."+childEntity+"Service.detail("+childEntity+"_id);\n"+
                "\t\t\tList<"+parentEntityUpper+"> list"+parentEntityUpper+" = "+childEntity+"ToUpdate.getList"+parentEntityUpper+"();\n"+
                "\t\t\t"+childEntity+".setList"+parentEntityUpper+"(list"+parentEntityUpper+");\n"+
                "\t\t\tthis."+childEntity+"Service.update("+childEntity+","+childEntity+"_id);\n"+
                "\t\t\treturn \"redirect:/"+parentEntity+"/detail/\"+"+parentEntity+"_id;\n"+
            "\t\t} catch (Exception ex) {\n"+
                "\t\t\tex.getMessage();\n"+
            "\t\t}\n"+
            "\t\treturn null;\n"+
        "\t}\n\n"; 
        return value;
    }
    
    // Generate the supplements method
    // private String generateSupplementMethods(String idType,String parentEntity, String childEntity){

    //     String value = "";
    //     value += this.templateAddOneToManyChildInDetailParentGet(idType, parentEntity, childEntity); 
    //     value += this.templateAddOneToManyChildInDetailParentPost(idType, parentEntity, childEntity);
    //     value += this.templateDeleteChildInDetailParent(idType, parentEntity, childEntity);
    //     value += this.templateUpdateChildInDetailParentGet(idType, parentEntity, childEntity);
    //     value += this.templateUpdateChildInDetailParentPost(idType, parentEntity, childEntity);
    //     return value;
    // }



    private String [] listRealValues(ConfigORM configORM) {
        String nameEntityMaj = FunctionUtils.firstLetterToUpperCase(this.controllerEnity.getNameEntity());
        String nameEntity =  this.controllerEnity.getNameEntity();
        String idType = this.controllerEnity.getIdTypEntity();
        String [] values = {
            nameEntityMaj,
            nameEntity,
            idType,
            this.isAnyFieldNullOrEmpty()+isChildEntityNull(configORM),
            this.generateChildEntityInIt(configORM,"Autowired"),
            this.generateChildEntityInIt(configORM,"RequestParamInAdd"),
            this.generateChildEntityInIt(configORM,"RequestParamInUpdate"),
            this.generateChildEntityInIt(configORM,"AttributeChildEntity"),
            this.generateChildEntityInIt(configORM,"setChildEntityInAdd"),
            this.generateChildEntityInIt(configORM,"setChildEntityInUpdate"),
            this.generateChildEntityInIt(configORM,"supplementMethods"),
            this.generateChildEntityInIt(configORM, "insidUpdateMethodInElse")
        };
        return values;
    }

    private String formatClassNameController(){
        return this.controllerEnity.getNameEntity()+"Controller";
    }

    public void createControllerEntity(ConfigORM configORM){
        File destinationFile = new File(ConfigSystem.path + Config.CONTROLLER_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(formatClassNameController()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(formatClassNameController()), Config.placeHoldersController, listRealValues(configORM),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_CONTROLLER_SOURCE_FILE_NAME,ConfigSystem.path + Config.CONTROLLER_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(formatClassNameController())+" :File Already exist...");
        }  
    }
    
}
