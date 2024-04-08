package view;

import java.io.File;


import config.Config;
import config.ConfigSystem;
import function.FunctionUtils;
import model.EntityField;

public class ViewListGenerate {
    private ViewList viewList;

    public ViewListGenerate(ViewList viewList) {
        this.viewList = viewList;
    }

    public ViewList getViewList() {
        return viewList;
    }

    public void setViewList(ViewList viewList) {
        this.viewList = viewList;
    }
    
    /*
     * Generate all contains of <tr> <th></th> <tr> balise
     */

    private String templateColumn(String value){
        return "\t\t\t\t\t<th>"+value+"</th>\n";
    }    

    public String generateColumns(){
        EntityField [] entityFields = this.viewList.getEntityFields();
        String value = "";
        for (EntityField entityField : entityFields) {
            value += this.templateColumn(entityField.getName());
        }
        return value;
    }
    /*
     * Generate all valus from a specific table
     */

    private String templateRow(String nameEntity,String fieldName){
        // String newvalue = "\t\t\t\t     \t<td>${[nameEntity].get[fieldName]()}</td>\r\n";
        
        // newvalue.replace("[nameEntity]", nameEntity);
        // newvalue.replace("[fieldName]", FunctionUtils.firstLetterToUpperCase(fieldName));
        String fieldNameConvert = FunctionUtils.firstLetterToUpperCase(fieldName);
        String newvalue = String.format("\t\t\t\t     \t<td>${%s.get%s()}</td>\r\n",nameEntity,fieldNameConvert);

        return newvalue;
    }


    private String generateRows(){
        String value = "";
        String nameEntity = this.viewList.getNameEntity();
        EntityField [] entityFields = this.viewList.getEntityFields();
        for (EntityField entityField : entityFields) {
            String nameEntityField = entityField.getName();   
            value += this.templateRow(nameEntity, nameEntityField);
        }
        return value;
    }

    private String [] listRealValues(){
        String nameEntity = this.viewList.getNameEntity();
        String [] values = {
            nameEntity,
            generateColumns(),
            generateRows(),
            this.viewList.getDetailNameLink(),
            this.viewList.getUpdateNameLink(),
            this.viewList.getDeleteNameLink()
        };
        return values;
    }

    private String formatEntityNameList(){
        return this.viewList.getNameEntity()+"-list";
    }

    public void createViewListEntity(){
        File destinationFile = new File(Config.VIEWSLIST_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameList()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameList()), Config.placeHoldersViewList, listRealValues(),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,ConfigSystem.path + Config.TEMPLATE_VIEWSLIST_SOURCE_FILE_NAME,ConfigSystem.path + Config.VIEWSLIST_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameList())+" :File Already exist...");
        }  
    }

}
