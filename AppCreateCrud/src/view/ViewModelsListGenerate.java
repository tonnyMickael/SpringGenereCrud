package view;

import java.io.File;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import function.FunctionUtils;

public class ViewModelsListGenerate {
    private String [] models;

    public String[] getModels() {
        return models;
    }

    public void setModels(String[] models) {
        this.models = models;
    }

    public ViewModelsListGenerate(String[] models) {
        this.models = models;
    }

    private String generateLinks(){
        String value = "";
        for (int i = 0; i < models.length; i++) {
            value += "<p><a href=\"/"+models[i]+"/list\"><button class=\"btn btn-primary\">"+models[i]+"</button></a></p>\n\t\t\t";
        }
        return value;
    }

    private String [] listRealValues(){
        String [] values = {
            this.generateLinks()
        };
        return values;
    }

    private String formatModelsNameList(){
        return "models-list";
    }

    public void createViewModelsList(){
        File destinationFile = new File(ConfigSystem.path + Config.VIEWSLIST_DESTINATION_FOLDER_PATH, formatModelsNameList());
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatModelsNameList()), Config.placeHoldersViewModelsList, listRealValues(),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSMODELSLIST_SOURCE_FILE_NAME,ConfigSystem.path + Config.VIEWSLIST_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatModelsNameList())+" :File Already exist...");
        }  
    }
}
