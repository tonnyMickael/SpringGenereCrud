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

    private String generateAnnotationsORMV3(){
        String tableName = this.entityTable.getName();
        ConfigORM cORM = new ConfigORM();
        ConfigORM [] listConfigOrm = cORM.listORMConfig();
        String value = "";

        for (int j = 0; j < listConfigOrm.length; j++) {
            String parentNameConfigORM = listConfigOrm[j].getTable_name_parent();
            String association_parent = listConfigOrm[j].getAssoc_parent();
            String childNameConfigORM = listConfigOrm[j].getTable_name_child();
            String association_child = listConfigOrm[j].getAssoc_child();
            boolean isCascadeType = listConfigOrm[j].isCascadeType();
            boolean isOrphanRemoval = listConfigOrm[j].isOrphanRemoval();
            // Target not source
            String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);
            String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);

            // TableName = parent
            if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToOne") && association_child.equals("")){
                value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+childNameConfigORM+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }

            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToOne") && association_child.equals("@OneToOne")){
                value += "\n\t@OneToOne(mappedBy = \""+tableName+"\")\n\t"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToOne")&& association_child.equals("@OneToOne") && isCascadeType ){
                value += "\n\t@OneToOne(cascade = CascadeType.ALL, mappedBy = \""+tableName+"\")\n\t"+this.generateField(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }
            
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToMany") && association_child.equals("")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany\n\t@JoinColumn(name = \""+childNameConfigORM+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                break;
            }

            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToMany")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany( mappedBy = \""+tableName+"\")\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToMany") && isCascadeType){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany(cascade = CascadeType.ALL, mappedBy = \""+tableName+"\")\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToMany") && isCascadeType && isOrphanRemoval){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += "\n\t@OneToMany(cascade = CascadeType.ALL, mappedBy = \""+tableName+"\",orphanRemoval = true)\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@ManyToMany")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                String mappedByName = "list"+parentNameConfigORMUpper;
                value += "\n\t@ManyToMany(mappedBy = \""+mappedByName+"\",cascade = {CascadeType.PERSIST, CascadeType.MERGE})\n\t"+this.generateField(typeVariable, nameVariable)+"\n";
                break;
            }
            
            // If tableName = Chilname
            else if(tableName.equals(childNameConfigORM) && association_child.equals("@OneToOne")){
                value += "\n\t@OneToOne\n\t@JoinColumn(name = \""+parentNameConfigORM+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n";
                value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if(tableName.equals(childNameConfigORM) && association_child.equals("@OneToMany")){
                value += "\n\t@OneToMany\n\t@JoinColumn(name = \""+parentNameConfigORM+"_id\", foreignKey = @ForeignKey(name = \"fk_"+parentNameConfigORM+"_"+childNameConfigORM+"\"))\n";
                value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && association_child.equals("@ManyToOne")){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToOne\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\")})\n";
                value += this.generateField(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && association_child.equals("@ManyToMany")){
                String nameTableReferenced = parentNameConfigORM+"_"+childNameConfigORM;
                value += "\n\t@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})\n\t@JoinTable(name = \""+nameTableReferenced+"\", joinColumns= {@JoinColumn(name = \""+childNameConfigORM+"_id\", referencedColumnName = \"id\")},  inverseJoinColumns = {@JoinColumn(name = \""+parentNameConfigORM+"_id\",referencedColumnName = \"id\")})\n";
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.generateField(typeVariable, nameVariable)+"\n";
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
            String parentNameConfigORM = listConfigORMs[i].getTable_name_parent();
            String association_parent = listConfigORMs[i].getAssoc_parent();
            String childNameConfigORM = listConfigORMs[i].getTable_name_child();
            String association_child = listConfigORMs[i].getAssoc_child();
            // boolean isCascadeType = listConfigORMs[i].isCascadeType();
            // boolean isOrphanRemoval = listConfigORMs[i].isOrphanRemoval();

            String childNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(childNameConfigORM);
            String parentNameConfigORMUpper = FunctionUtils.firstLetterToUpperCase(parentNameConfigORM);

            if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToOne")){
                
                value += this.templateSetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                value += this.templateGetterMethod(childNameConfigORMUpper, childNameConfigORM)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@OneToMany")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
                break;
            }
            else if(tableName.equals(parentNameConfigORM) && association_parent.equals("@ManyToMany")){
                String typeVariable = String.format("java.util.List<%s>",childNameConfigORMUpper);
                String nameVariable = "list"+childNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
                break;
            }

            else if(tableName.equals(childNameConfigORM) && association_child.equals("@OneToOne")){
                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && association_child.equals("@ManyToOne")){
                value += this.templateSetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                value += this.templateGetterMethod(parentNameConfigORMUpper, parentNameConfigORM)+"\n";
                break;
            }
            else if (tableName.equals(childNameConfigORM) && association_child.equals("@ManyToMany")){
                String typeVariable = String.format("java.util.List<%s>",parentNameConfigORMUpper);
                String nameVariable = "list"+parentNameConfigORMUpper;
                value += this.templateSetterMethod(typeVariable, nameVariable)+"\n";
                value += this.templateGetterMethod(typeVariable, nameVariable)+"\n";
                break;
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
