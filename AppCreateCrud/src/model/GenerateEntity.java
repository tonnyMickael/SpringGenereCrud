package model;

import java.io.File;

import config.Config;
import config.ConfigORM;
import function.FunctionUtils;

public class GenerateEntity {
    private EntityTable entityTable;

    public GenerateEntity(EntityTable entityTable){
        this.entityTable = entityTable;
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public void setEntityTable(EntityTable entityTable) {
        this.entityTable = entityTable;
    }
    

    /*
     * function which create the class java
     */
    // public void createClass() throws IOException{
    //     FunctionUtils.copyAndRenameFile(this.formatToFileJava());
    // }
    /*
     * Format Annotations
     */
    // private String nameBeforeFkId(ConfigORM configORM){
    //     String value = "";
    //     if(configORM.getOwnerFkfield().equals(configORM.getName_table_parent())){
    //         value += configORM.getAssoc_child_parent();
    //     }
    //     else if(configORM.getOwnerFkfield().equals(configORM.getName_table_child())){
    //         value += configORM.getAssoc_parent_child();
    //     }
    //     return value;
    // }

    private String generateAnnotationsORMV3(ConfigORM configORM){
        String tableName = this.entityTable.getName();
        String value = "";

        String parentNameConfigORM = configORM.getName_table_parent();
        String childNameConfigORM = configORM.getName_table_child();
        String assoc_parent_child = configORM.getAssoc_parent_child();
        String assoc_child_parent = configORM.getAssoc_child_parent();
        String type_cascade = configORM.getType_cascade();
        // String ownerFkField = configORM.getOwnerFkfield();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String optConfig = configORM.getOptionConfiguration();
        // Target not source
        String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
        String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);

        // Configure the table which hold the FK reference
        configORM.setOwnerFkfield(parentNameConfigORM);
        String ownerFkField = configORM.getOwnerFkfield();

        // Unidirectionnal With Option 2: we get the person from a region
        // @OneToMany
        // Association :1-N and 1-1
        // TableName = parent
        // Fk column name = region_id because user want to init the name fk columns in person
        if(tableName.equals(parentNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("2")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
               
            }   
        // TableName = child  
        else if(tableName.equals(childNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("1")){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name=\"fk_"+nameTableReferenced+"\"),nullable = false)\n";
                value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                                   
        }
        
        // Bidirectionnale
        // Association 1-n and 1-1, Option 2
        else if (tableName.equals(parentNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isBiDirectionnal ){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany(mappedBy = \""+ownerFkField+"\")\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
               
        }
        else if (tableName.equals(childNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isBiDirectionnal ){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name=\"fk_"+nameTableReferenced+"\"),nullable = false)\n";
                value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                
        }

        // Unidirectionnal
        // @ManyToMany
        // Association 1-N and 1-N
        else if (tableName.equals(parentNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") &&
            isUniDirectionnal && 
            optConfig.equals("2") ){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToMany\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+parentNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+childNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.generateField(typeVariable, nameVariable)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") &&
            isUniDirectionnal && 
            optConfig.equals("1") ){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToMany\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORM);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.generateField(typeVariable, nameVariable)+"\n";
        }
        // Bidirectionnal
        // @ManyToMany
        // Association 1-N and 1-N
        else if (tableName.equals(parentNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") && 
            isBiDirectionnal 
            ){
                String mappedByName = "list"+FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
                value += "\n\t@ManyToMany(mappedBy= \""+mappedByName+"\")\n\t";
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.generateField(typeVariable, nameVariable)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) &&
            assoc_child_parent.equals("1-N") &&
            assoc_parent_child.equals("1-N") &&
            isBiDirectionnal){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToMany\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.generateField(typeVariable, nameVariable)+"\n";                    
            }
        // @OneToOne
        // unidirectionnal
        else if(tableName.equals(parentNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("2")){
                value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
               
        }    
        else if(tableName.equals(childNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("1") ){
                value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
               
        }  
        // bidirectionnal
        else if(tableName.equals(parentNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isBiDirectionnal ){
                value += "\n\t@OneToOne(mappedBy = \""+ownerFkField+"\")\n\t"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
               
            }
        else if(tableName.equals(childNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isBiDirectionnal ){
                value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
               
            }
        return value;    
    }

    private String generateAnnotations (EntityField entityField){
        String value = "";
        if(entityField.getName().equals("id")){
            value += "@Id\n\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n\t@Column(name = \"id\")";
        }
        else if (entityField.getIsNullable().equals("NO")) {
            value += "\t@Column(name = \""+entityField.getName()+"\", nullable = false)";
        }
        else {
            value += "\t@Column(name = \""+entityField.getName()+"\")";
        }
        return value;
    }

    /*
     * Format fields
     */
    private String generateField(String type, String name){
        String newValue = String.format("\tprivate %s %s;", type, name);
        return newValue;
    }

    /*
     * Format the fields Annoted
     */
    private String fieldsAnnoted(ConfigORM configORM){
        String newValue="";
        EntityField [] entityFields = this.getEntityTable().getEntityFields();
        for (EntityField entityField : entityFields) {
            String type = entityField.getType();
            String nameField = entityField.getName();
            newValue += String.format("%s\n%s\n\n", generateAnnotations(entityField),generateField(type,nameField));

        }
        newValue += this.generateAnnotationsORMV3(configORM);

        return newValue;
    }

    // Template Method to generated
    private String templateSetterMethod(String type, String field){
        String fieldUpperFirstLetter = FunctionUtils.firstLetterToUpperCase(field);
        String fieldName = field;
        String template = String.format("\tpublic void set%s(%s %s){\n\t\tthis.%s = %s;\n\t}",fieldUpperFirstLetter,type,fieldName,fieldName,fieldName);
        return template;
    }

    private String templateGetterMethod(String type, String field){
        String fieldUpperFirstLetter = FunctionUtils.firstLetterToUpperCase(field);
        String fieldName = field;
        String template = String.format("\tpublic %s get%s(){\n\t\treturn this.%s;\n\t}",type,fieldUpperFirstLetter,fieldName);
        return template;
    }

    // Generate Setter for ORM (One to One/ One to Many/ Many to Many)
    private String generateSetGetORM(ConfigORM configORM){
        String value = "";
        String tableName = this.entityTable.getName();

        String parentNameConfigORM = configORM.getName_table_parent();
        String childNameConfigORM = configORM.getName_table_child();
        String assoc_parent_child = configORM.getAssoc_parent_child();
        String assoc_child_parent = configORM.getAssoc_child_parent();
        // String type_cascade = configORM.getType_cascade();
        // String ownerFkField = configORM.getOwnerFkfield();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String optConfig = configORM.getOptionConfiguration();
        // Target not source
        String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
        String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);

        // Configure the table which hold the FK reference
        // configORM.setOwnerFkfield(parentNameConfigORM);
        // String ownerFkField = configORM.getOwnerFkfield();

        // Unidirectional
        // @OneToOne
        if(tableName.equals(parentNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("2")){
            
            value += this.templateSetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
            value += this.templateGetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) && 
            assoc_child_parent.equals("1-1") && 
            assoc_parent_child.equals("1-1") && 
            isUniDirectionnal && optConfig.equals("1")){

            value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
            value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
        }
        else if(tableName.equals(parentNameConfigORM) &&
            assoc_child_parent.equals("1-1") &&
            assoc_parent_child.equals("1-1") &&
            isBiDirectionnal ){
            
            value += this.templateSetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
            value += this.templateGetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) && 
            assoc_child_parent.equals("1-1") && 
            assoc_parent_child.equals("1-1") && 
            isBiDirectionnal){

            value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
            value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
        }

        // Unidirectionnal
        // @OneToMany
        else if(tableName.equals(parentNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("2")){

            String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
            String nameVariable = "list"+childNameConfigORMUpper;
            value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
            value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
        }
        else if(tableName.equals(childNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isUniDirectionnal && optConfig.equals("1")){

            value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
            value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
        }
        // Bidirectionnal
        else if (tableName.equals(parentNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isBiDirectionnal ){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) && 
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-1") &&
            isBiDirectionnal ){
                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
        }

        // Unidriectionnal
        // @ManyToMany
        else if (tableName.equals(parentNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") &&
            isUniDirectionnal && 
            optConfig.equals("2") ){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") &&
            isUniDirectionnal && 
            optConfig.equals("1") ){
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
        }
        //Bidirectionnal
        else if (tableName.equals(parentNameConfigORM) &&
            assoc_parent_child.equals("1-N") && 
            assoc_child_parent.equals("1-N") && 
            isBiDirectionnal 
            ){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
        }
        else if (tableName.equals(childNameConfigORM) &&
            assoc_child_parent.equals("1-N") &&
            assoc_parent_child.equals("1-N") &&
            isBiDirectionnal){
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";                   
        }
        else {
            value = "";
        }
        return value;
    }

    // Generate Setter from entityField
    private String generateSetter(EntityField entityField){
        String fieldUpperFirstLetter = FunctionUtils.firstLetterToUpperCase(entityField.getName());
        String fieldName = entityField.getName();
        String type = entityField.getType();
        String template = String.format("\tpublic void set%s(%s %s){\n\t\tthis.%s = %s;\n\t}",fieldUpperFirstLetter,type,fieldName,fieldName,fieldName);
        return template;
    }

    // Generate Getter from entityField
    private String generateGetter(EntityField entityField){
        String fieldUpperFirstLetter = FunctionUtils.firstLetterToUpperCase(entityField.getName());
        String fieldName = entityField.getName();
        String type = entityField.getType();
        String template = String.format("\tpublic %s get%s(){\n\t\treturn this.%s;\n\t}",type,fieldUpperFirstLetter,fieldName);
        return template;
    }

    /*
     * Generate the methods (get and set)
     */
    private String generateEntityMethods(ConfigORM configORM){
        String newValue = "";
        EntityField [] entityFields = this.entityTable.getEntityFields();
        for (EntityField entityField : entityFields) {
            newValue += this.generateSetter(entityField)+"\n"+this.generateGetter(entityField)+"\n";

        }
        newValue += this.generateSetGetORM(configORM);
        return newValue;
    }


    /*
     * Functions list All real values
     */
    private String [] listRealValues(ConfigORM config){
        String entityTableName = this.getEntityTable().getName();
        String entityFirstLetterUpper = FunctionUtils.firstLetterToUpperCase(entityTableName);
        String[] realValues = {
            entityTableName, 
            entityFirstLetterUpper, 
            fieldsAnnoted(config),
            generateEntityMethods(config)
        };
        return realValues;
    }


    /*
     * function which replace all parameters in the file
     */
    public void createAndWriteClass(ConfigORM configORM){
        File destinationFile = new File(Config.MODEL_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(this.entityTable.getName()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(this.entityTable.getName()), Config.placeHoldersModel, listRealValues(configORM),Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_MODEL_SOURCE_FILE_NAME,Config.MODEL_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(this.entityTable.getName())+" :File Already exist...");
        }  
        
    }
}
