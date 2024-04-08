package MetaData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import config.Config;
import config.ConfigSystem;

public class MetaFileConvertion {
    private List<String> listTypeHTML = new ArrayList<String>();

    private void setListTypeHTML(List<String> listHTML){ this.listTypeHTML = listHTML; }
    private List<String> getListHTML(){ return listTypeHTML; }
   
    public MetaFileConvertion(){  
        ReadFileTypeHTML(); 
    }

    private void ReadFileTypeHTML(){
<<<<<<< Updated upstream
=======
        // String test = ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/MetaDataTypeHTML.txt";
        // System.out.println(test);
>>>>>>> Stashed changes
        try (BufferedReader br = new BufferedReader(new FileReader(ConfigSystem.path + Config.DATATYPE_HTML_PATH+"/MetaDataTypeHTML.txt"))) {
            List<String> lignes = new ArrayList<>();
            String ligne;
            while ((ligne = br.readLine()) != null) {
                lignes.add(ligne);
            }
            setListTypeHTML(lignes);
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }


    private String changeSpecial(String type){
        String typefield ="";
        switch (type) {
            case "varchar":
                typefield = getListHTML().get(3);
                break;
            case "integer":
                typefield = getListHTML().get(4);
                break;
            case "decimal":
                typefield = getListHTML().get(4);
                break;
            case "numeric":
                typefield = getListHTML().get(4);
                break;
            case "double precision":
                typefield = getListHTML().get(4);
                break;
            case "float8":
                typefield = getListHTML().get(4);
                break; 
            case "datetime-local":
                typefield = getListHTML().get(5);
                break;
            case "date":
                typefield = getListHTML().get(6);
                break;
            case "time":
                typefield = getListHTML().get(7);
                break;
            case "serial":
                typefield = getListHTML().get(20);
                break;
            case "bigserial":
                typefield = getListHTML().get(20);
                break;
            default:
                break;
        }
        return typefield;
    }

    private String change(String nomColumn, String typeColumn){
        String field = "";
        switch (nomColumn) {
            case "telephone":
                field = getListHTML().get(0);
                break;
            case "email":
                field = getListHTML().get(1);
                break;
            case "password":    
                field = getListHTML().get(2);
                break;
            default:
                field = changeSpecial(typeColumn);
                break;
        }
        return field;
    }

    private ArrayList<MetaColumn> typeHtml(ArrayList<MetaColumn> typeDB){
        String htmlFied = "";
        MetaColumn newtype;
        ArrayList<MetaColumn> convertedFieldHTML = new ArrayList<>();
        for (int i = 0; i < typeDB.size(); i++) {
            if(!typeDB.get(i).getNameColumn().equals("id")){
                htmlFied = change(typeDB.get(i).getNameColumn(), typeDB.get(i).getTypeColumn());
                newtype = new MetaColumn(typeDB.get(i).getNameColumn(), htmlFied);
                convertedFieldHTML.add(newtype);
            }
        }
        return convertedFieldHTML;  
    }

    public ArrayList<MetaTable> convertionFieldHTML(ArrayList<MetaTable> dataTable){
        MetaTable tableNewData = null;
        ArrayList<MetaTable> transformed = new ArrayList<>();
        ArrayList<MetaColumn> typeHTML = null;
        for (int index = 0; index < dataTable.size(); index++) {
            typeHTML = typeHtml(dataTable.get(index).getInfoTable());
            tableNewData = new MetaTable(dataTable.get(index).getNameTable(), typeHTML);
            transformed.add(tableNewData);
        }        
        return transformed;
    }    
}