package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.Config;
import function.FunctionUtils;
import model.EntityField;

public class ViewModifeGenerate {
    private ViewModife viewModife;
    
    public ViewModifeGenerate(ViewModife viewModife){
        this.viewModife=viewModife;
    }

    /**
     * @return ViewModife return the viewModife
     */
    public ViewModife getViewModife() {
        return viewModife;
    }

    /**
     * @param viewModife the viewModife to set
     */
    public void setViewModife(ViewModife viewModife) {
        this.viewModife = viewModife;
    }
    private List<String> ReadFileTypeHTML(){
        List<String> lignes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Config.DATATYPE_HTML_PATH+"/MetaDataTypeHTML.txt"))) {
           
            String ligne;
            while ((ligne = br.readLine()) != null) {
                lignes.add(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }    
        return lignes;
    }
    private String change(String nomColumn, String typeColumn){
        String field = "";
        List<String> getlist=this.ReadFileTypeHTML();
        switch (nomColumn) {
            case "telephone":
                field = getlist.get(0);
                break;
            case "email":
                field = getlist.get(1);
                break;
            case "password":    
                field = getlist.get(2);
                break;
            default:
                field = htmltype(typeColumn);
                break;
        }
        return field;
    }
    public String htmltype(String Type) {
        switch (Type.toLowerCase()) {
            case "int":
            case "integer":
            case "bigint":
            case "smallint":
            case "tinyint":
                return "number";
            case "float":
            case "double":
            case "decimal":
                return "number";
            case "char":
            case "varchar":
            case "text":
            case "longtext":
                return "text";
            case "date":
            case "datetime":
            case "timestamp":
            case"java.time.localdate":
                return "date";
            default:
                return "text"; // Defaulting to text input for unknown types
        }
    }
    private String templateinput(EntityField entityField,String nameEntity){
        String newvalue="";
        String namecolumn = entityField.getName();
        String namecolumnupper=FunctionUtils.firstLetterToUpperCase(namecolumn);
        String nametable = nameEntity;
        String type = entityField.getType();
        String typehtml=change(namecolumn,type);
        if (typehtml.equals("number")) {
            newvalue += "\t\t"+namecolumn+":<input name=\"" + namecolumn + "\" step =\"0.01\" type=\"" + typehtml + "\" value=\"${(" + nametable + ".get" + namecolumnupper + "()?string[\"0.###\"]?replace(\",\", \".\"))!}\"/><br>\n";

        }
        // String typehtml =(namecolumn,type);

        // Construction de la chaîne de template avec les valeurs
        // String newvalue = "\t\t<input name='" + namecolumn + "' type='" + type + "' value='${" + nametable + "." + "get"+namecolumnupper + "()! }'>\t\t\n";
        else{
            newvalue = "\t\t"+namecolumn+":<input name=\"" + namecolumn + "\" type=\"" + typehtml + "\" value=\"${" + nametable + ".get" + namecolumnupper + "()}\"/><br>\n";
        }
        return newvalue;
    }
    public String generateInput(EntityField [] entityField,String nameEntity){
    String result="";
    for(EntityField entityFields : entityField){
        // result.append(templateinput(entityFields,entitys));
        if (!entityFields.getName().equals("id")) {
        result+=templateinput(entityFields, nameEntity);
        }
    }
   
    
    return result;
}
    
public String[] listRealValues(EntityField [] entityField,String nameEntity) {
    // String nameEntity = this.viewModife.getNameEntity();
    String inputs = generateInput(entityField,nameEntity);
    String[] values = { nameEntity, inputs };
    return values;
}

     private String formatEntityNameModife(){
        return this.viewModife.getNameEntity()+"-edit-form";
    }

    public void createViewModifeEntity(){
        EntityField [] entityFields = this.viewModife.getEntityFields();
        String nameEntity=this.viewModife.getNameEntity();
        // Ajoutez des objets InputGenere à inputGeneres

        String[] realValues = listRealValues(entityFields,nameEntity);
        File destinationFile = new File(Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameModife()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameModife()), Config.placeHoldersViewUpdate,realValues,Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWSUPDATE_SOURCE_FILE_NAME,Config.VIEWSUPDATE_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameModife())+" :File Already exist...");
        }  
    }

}
