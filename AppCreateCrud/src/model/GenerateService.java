package model;

import java.io.File;

import config.Config;
import config.ConfigORM;
import function.FunctionUtils;

public class GenerateService {

    private Service service;


    public GenerateService(){}

    public Service getService() {
        return service;
    }


    public void setService(Service service) {
        this.service = service;
    }


    public GenerateService(Service service) {
        this.service = service;
    }


    private String generateAdd(){
        String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String newValue = String.format("public %s add(%s %s) {\r\n" + //
                        "\t\treturn this.%sRepository.save(%s);\r\n" + //
                        "\t}",className,className,varClassName,varClassName,varClassName);

        return newValue;
    }

    private String generateFetch(){
        String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String newValue = String.format("public List<%s> fetch() {\r\n" + //
                        "\t\treturn this.%sRepository.findAll();\r\n" + //
                        "\t} ",className,varClassName);

        return newValue;
    }

    /* 
     * Function generate all the necessary methods in the update
    */
    public String generateMethodInUpdate(EntityField[] entityFields){
        // String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String value = "";
        for (EntityField entityField : entityFields) {
            if(!entityField.getName().equals("id")){
                String fieldMaj = FunctionUtils.firstLetterToUpperCase(entityField.getName());
                value+=String.format("\n\t\t%sUpdated.set%s(%s.get%s());\r",varClassName,fieldMaj,varClassName,fieldMaj);
            }
        }
        return value;
    }

    private String generateSetChildTable(String name){
        // produitUpdated.setTag(produit.getTag());
        String value = "";
        ConfigORM configORM = new ConfigORM();
        ConfigORM [] listConfigORM = configORM.listORMConfig(); 
        for (int i = 0; i < listConfigORM.length; i++) {
            ConfigORM configORMIndex = listConfigORM[i];
            String parentTable = configORMIndex.getTable_name_parent();
            String childTable = configORMIndex.getTable_name_child();
            String childTableUpper = FunctionUtils.firstLetterToUpperCase(childTable);
            if(parentTable.equals(name)){
                value += String.format("\n\t\t%sUpdated.set%s(%s.get%s());\r",name,childTableUpper,name,childTableUpper);
                break;
            }
            else {
                value = "";
            }
        }
        return value;
    }

    private String generateUpdate(String generateMethodInUpdate){
        String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String idType = this.service.getIdType();
        String newValue = String.format("public %s update (%s %s, %s id) {\r\n" + //
                        "\t\t%s %sUpdated = new %s();\r\n" + //
                        "\t\t%sUpdated.setId(id);\r" + generateMethodInUpdate + generateSetChildTable(this.service.getName())+
                        "\t\treturn this.%sRepository.save(%sUpdated);\r\n" + //
                        "\t}",className,className,varClassName,idType,className,varClassName,className,varClassName,varClassName,varClassName);
        return newValue;
    }

    private String generateDelete() {
        // String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String idType = this.service.getIdType();
        String newValue = String.format("public void delete(%s id) {\r\n" + //
                        "\t\tthis.%sRepository.deleteById(id);\r\n" + //
                        "\t}",idType,varClassName);
        return newValue;
    }

    private String generateDetail(){
        String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String idType = this.service.getIdType();
        String newValue = String.format("public %s detail (%s id) {\r\n" + //
                        "\t\t%s %sDB = %sRepository.findById(id).get();\r\n" + //
                        "\t\treturn %sDB;\r\n" + //
                        "\t}", className,idType,className,varClassName,varClassName,varClassName);
        return newValue;
    }

    private String []listRealValues(String argGenerateUpdate){
        String className = FunctionUtils.firstLetterToUpperCase(this.service.getName());
        String varClassName = this.service.getName();
        String [] values = {
            className,
            varClassName,
            generateAdd(),
            generateFetch(),
            generateUpdate(argGenerateUpdate),
            generateDelete(),
            generateDetail()
        };
        return values;
    }

    private String formatClassNameService(){
        return this.service.getName()+"Service";
    }

    public void createServiceEntity(String argGenerateUpdate){
        File destinationFile = new File(Config.SERVICE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(formatClassNameService()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(formatClassNameService()), Config.placeHoldersService, listRealValues(argGenerateUpdate),Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_SERVICE_SOURCE_FILE_NAME,Config.SERVICE_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(formatClassNameService())+" :File Already exist...");
        }  
    }

}
