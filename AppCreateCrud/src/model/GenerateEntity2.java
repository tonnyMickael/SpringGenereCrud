package model;

import java.io.File;

import config.Config;
import config.ConfigORM;
import function.FunctionUtils;

public class GenerateEntity2 {
    private EntityTable entityTable;

    public GenerateEntity2(EntityTable entityTable){
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
    // private String nameBeforeFkId(ConfigORM2 configORM2){
    //     String value = "";
    //     if(configORM2.getOwnerFkfield().equals(configORM2.getName_table_parent())){
    //         value += configORM2.getAssoc_child_parent();
    //     }
    //     else if(configORM2.getOwnerFkfield().equals(configORM2.getName_table_child())){
    //         value += configORM2.getAssoc_parent_child();
    //     }
    //     return value;
    // }

    private String generateAnnotationsORMV3(){
        String tableName = this.entityTable.getName();
        ConfigORM cORM = new ConfigORM();
        ConfigORM [] listConfigOrm = cORM.listORMConfig();
        String value = "";

        for (int j = 0; j < listConfigOrm.length; j++) {
            String parentNameConfigORM = listConfigOrm[j].getName_table_parent();
            String childNameConfigORM = listConfigOrm[j].getName_table_child();
            String assoc_parent_child = listConfigOrm[j].getAssoc_parent_child();
            String assoc_child_parent = listConfigOrm[j].getAssoc_child_parent();
            String type_cascade = listConfigOrm[j].getType_cascade();
            // String ownerFkField = listConfigOrm[j].getOwnerFkfield();
            boolean isUniDirectionnal = listConfigOrm[j].isUniDirectionnal();
            boolean isBiDirectionnal = listConfigOrm[j].isBiDirectionnal();
            String optConfig = listConfigOrm[j].getOptionConfiguration();
            // Target not source
            String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
            String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);

            // Configure the table which hold the FK reference
            listConfigOrm[j].setOwnerFkfield(parentNameConfigORM);
            String ownerFkField = listConfigOrm[j].getOwnerFkfield();

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
                    break;
                }   
            // TableName = child  
            else if(tableName.equals(childNameConfigORM) && 
                assoc_parent_child.equals("1-N") && 
                assoc_child_parent.equals("1-1") &&
                isUniDirectionnal && optConfig.equals("1")){
                    String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                    value += "\n\t@ManyToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name=\"fk_"+nameTableReferenced+"\"),nullable = false)\n";
                    value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    break;                    
            }
            // Association: N-1 and 1-1
            // else if(tableName.equals(parentNameConfigORM) && 
            //     assoc_parent_child.equals("N-1") &&
            //     assoc_child_parent.equals("1-1") &&
            //     isUniDirectionnal && optConfig.equals("2")){
            //         String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
            //         value += "\n\t@ManyToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name=\"fk_"+nameTableReferenced+"\"),nullable = false)\n";
            //         value += this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
            //         break; 
            // }
            // else if(tableName.equals(childNameConfigORMUpper) &&
            //     assoc_parent_child.equals("N-1") &&
            //     assoc_child_parent.equals("1-1") &&
            //     isUniDirectionnal && optConfig.equals("1")){
            //         String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
            //         String nameVariable = "list"+parentNameConfigORMUpper;
            //         value += "\n\t@OneToMany\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
            //         break;                    
            //     }
            
            // Bidirectionnale
            // Association 1-n and 1-1, Option 2
            else if (tableName.equals(parentNameConfigORM) && 
                assoc_parent_child.equals("1-N") && 
                assoc_child_parent.equals("1-1") &&
                isBiDirectionnal ){
                    String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                    String nameVariable = "list"+childNameConfigORMUpper;
                    value += "\n\t@OneToMany(mappedBy = \""+ownerFkField+"\")\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                    break;
            }
            else if (tableName.equals(childNameConfigORM) && 
                assoc_parent_child.equals("1-N") && 
                assoc_child_parent.equals("1-1") &&
                isBiDirectionnal ){
                    String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                    value += "\n\t@ManyToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name=\"fk_"+nameTableReferenced+"\"),nullable = false)\n";
                    value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    break; 
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
                    value += "\n\t@ManyToMany(mappedBy\""+mappedByName+"\")\n\t";
                    String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                    String nameVariable = "list"+childNameConfigORMUpper;
                    value += this.generateField(typeVariable, nameVariable)+"\n";
            }
            else if (tableName.equals(childNameConfigORMUpper) &&
                assoc_child_parent.equals("1-N") &&
                assoc_parent_child.equals("1-N") &&
                isBiDirectionnal){
                    String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                    value += "\n\t@ManyToMany\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                    String typeVariable = String.format("java.util.List<%s>",parentNameConfigORM);
                    String nameVariable = "list"+parentNameConfigORMUpper;
                    value += this.generateField(typeVariable, nameVariable)+"\n";                    
                }
            // @OneToOne
            // unidirectionnal
            else if(tableName.equals(parentNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isUniDirectionnal && optConfig.equals("1")){
                    value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
                    break;
            }    
            else if(tableName.equals(childNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isUniDirectionnal && optConfig.equals("2") ){
                    value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    break;
            }  
            // bidirectionnal
            else if(tableName.equals(parentNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isBiDirectionnal ){
                    value += "\n\t@OneToOne(mappedBy = \""+ownerFkField+"\")\n\t"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
                    break;
                }
            else if(tableName.equals(childNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isBiDirectionnal ){
                    value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+ownerFkField+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    break;
                }
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
    private String fieldsAnnoted(){
        String newValue="";
        EntityField [] entityFields = this.getEntityTable().getEntityFields();
        for (EntityField entityField : entityFields) {
            String type = entityField.getType();
            String nameField = entityField.getName();
            newValue += String.format("%s\n%s\n\n", generateAnnotations(entityField),generateField(type,nameField));

        }
        newValue += this.generateAnnotationsORMV3();

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
    private String generateSetGetORM(){
        String value = "";
        String tableName = this.entityTable.getName();
        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORMs = configORM.listORMConfig();
        for (int i = 0; i < listConfigORMs.length; i++) {
            String parentNameConfigORM = listConfigORMs[i].getName_table_parent();
            String childNameConfigORM = listConfigORMs[i].getName_table_child();
            String assoc_parent_child = listConfigORMs[i].getAssoc_parent_child();
            String assoc_child_parent = listConfigORMs[i].getAssoc_child_parent();
            // String type_cascade = listConfigORMs[i].getType_cascade();
            // String ownerFkField = listConfigORMs[i].getOwnerFkfield();
            boolean isUniDirectionnal = listConfigORMs[i].isUniDirectionnal();
            boolean isBiDirectionnal = listConfigORMs[i].isBiDirectionnal();
            String optConfig = listConfigORMs[i].getOptionConfiguration();
            // Target not source
            String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
            String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);

            // Configure the table which hold the FK reference
            // listConfigORMs[i].setOwnerFkfield(parentNameConfigORM);
            // String ownerFkField = listConfigORMs[i].getOwnerFkfield();

            // Unidirectional
            // @OneToOne
            if(tableName.equals(parentNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isUniDirectionnal && optConfig.equals("1")){
                
                value += this.templateSetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                value += this.templateGetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && 
                assoc_child_parent.equals("1-1") && 
                assoc_parent_child.equals("1-1") && 
                isUniDirectionnal && optConfig.equals("2")){

                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) &&
                assoc_child_parent.equals("1-1") &&
                assoc_parent_child.equals("1-1") &&
                isBiDirectionnal ){
                
                value += this.templateSetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                value += this.templateGetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && 
                assoc_child_parent.equals("1-1") && 
                assoc_parent_child.equals("1-1") && 
                isBiDirectionnal){

                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
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
                break;
            }
            else if(tableName.equals(childNameConfigORM) && 
                assoc_parent_child.equals("1-N") && 
                assoc_child_parent.equals("1-1") &&
                isUniDirectionnal && optConfig.equals("1")){

                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
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
                    break;
            }
            else if (tableName.equals(childNameConfigORM) && 
                assoc_parent_child.equals("1-N") && 
                assoc_child_parent.equals("1-1") &&
                isBiDirectionnal ){
                    value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                    break;
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
                    break;
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
                    break;
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
                    break;
            }
            else if (tableName.equals(childNameConfigORMUpper) &&
                assoc_child_parent.equals("1-N") &&
                assoc_parent_child.equals("1-N") &&
                isBiDirectionnal){
                    String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                    value += "\n\t@ManyToMany\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                    String typeVariable = String.format("java.util.List<%s>",parentNameConfigORM);
                    String nameVariable = "list"+parentNameConfigORMUpper;
                    value += this.generateField(typeVariable, nameVariable)+"\n";                    
            }
            else {
                value = "";
            }
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
    private String generateEntityMethods(){
        String newValue = "";
        EntityField [] entityFields = this.entityTable.getEntityFields();
        for (EntityField entityField : entityFields) {
            newValue += this.generateSetter(entityField)+"\n"+this.generateGetter(entityField)+"\n";

        }
        newValue += this.generateSetGetORM();
        return newValue;
    }


    /*
     * Functions list All real values
     */
    private String [] listRealValues(){
        String entityTableName = this.getEntityTable().getName();
        String entityFirstLetterUpper = FunctionUtils.firstLetterToUpperCase(entityTableName);
        String[] realValues = {
            entityTableName, 
            entityFirstLetterUpper, 
            fieldsAnnoted(),
            generateEntityMethods()
        };
        return realValues;
    }


    /*
     * function which replace all parameters in the file
     */
    public void createAndWriteClass(){
        File destinationFile = new File(Config.MODEL_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(this.entityTable.getName()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(this.entityTable.getName()), Config.placeHoldersModel, listRealValues(),Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_MODEL_SOURCE_FILE_NAME,Config.MODEL_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(this.entityTable.getName())+" :File Already exist...");
        }  
        
    }
}
