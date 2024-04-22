package view;

import java.io.File;
import java.rmi.ConnectIOException;

import config.Config;
import config.ConfigORM;
import config.ConfigSystem;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;

public class ViewDetailGenerate {
    private ViewDetail viewDetail;
    private DBManager dbManager;
    private String navBarLogOut;

    public String getNavBarLogOut() {
        return navBarLogOut;
    }

    public void setNavBarLogOut(String navBarLogOut) {
        this.navBarLogOut = navBarLogOut;
    }

    public ViewDetailGenerate(ViewDetail viewDetail, DBManager dbManager) {
        this.dbManager = dbManager;
        this.viewDetail = viewDetail;
    }

    /*
     * Fonction mi genere fieldsTable
     * - creer d'abord un template 
    */

    private String templateFieldsTable(String nameEntity, String nameField){
        String nameFieldUpper = FunctionUtils.firstLetterToUpperCase(nameField);
        String value = String.format("\t\t\t\t<tr>\n"+
        "\t\t\t\t\t<td>%s</td>\n"+
        "\t\t\t\t\t<td>:</td>\n"+
        "\t\t\t\t\t<td>${%s.%s}</td>\n"+          
        "\t\t\t\t</tr>\n",nameField,nameEntity,nameField);
        return value;
    }

    private String generateFieldsTable(){
        String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        EntityField[] entityFields = this.viewDetail.getEntityFields();
        for (int i = 0; i < entityFields.length; i++) {
            EntityField entityField = entityFields[i];
            value += templateFieldsTable(nameEntity, entityField.getName());
        }
        return value;
    }

    /*
     * Fonction mi genere tableau du child entity
     * - mi-creer template anah tableau de child Entity
     * - mi-creer template Column
     * - mi-creer template rows
     */
    private String templateColumnChild(String nameTableChild){
        String value = "";
        String [] columns = this.dbManager.getColumnsFromTable(nameTableChild); 
        for (String column : columns) {
            value += String.format("\t\t\t\t\t\t\t\t<th>%s</th>\n",column);
        } 
        return value;
    }

    private String templateRowschild(String nameEntity,String nameTableChild){
        String value = "";
        String nameTableChildUpper = FunctionUtils.firstLetterToUpperCase(nameTableChild);
        String [] nameColumns = this.dbManager.getColumnsFromTable(nameTableChild); 
        for (String nameColumn : nameColumns) {
        	String nameColumnUpper = FunctionUtils.firstLetterToUpperCase(nameColumn);
            value += String.format("\t\t\t\t\t\t\t\t<td>${%s.%s.%s}</td>\n",nameEntity,nameTableChild,nameColumn);
        } 
        return value;
    }

    private String templateRowschildForList(String nameTable){
        String value = "";
        String [] nameColumns = this.dbManager.getColumnsFromTable(nameTable); 
        for (String nameColumn : nameColumns) {
            value += String.format("\t\t\t\t\t\t\t\t\t<td>${%s.%s}</td>\n",nameTable,nameColumn);
        } 
        return value;
    }

    private String templateDetailInListArrayOneToMany(String parentTable, String childTable){
        String childTableUpper = FunctionUtils.firstLetterToUpperCase(childTable);
        String value =  "\t\t\t\t<#if "+parentTable+".list"+childTableUpper+" ??>\n"+
                        "\t\t\t\t\t<tr class=\"text-muted w-lg-50\">\n"+
                        "\t\t\t\t\t\t<td scope=\"col\">"+childTable+"</td>\n"+
                        "\t\t\t\t\t\t<td>:</td>\n"+
                        "\t\t\t\t\t\t<td>\n"+
                        "\t\t\t\t\t\t\t<table border=\"1\" class=\"table\">\n"+
                        "\t\t\t\t\t\t\t<thead>\n"+
                        "\t\t\t\t\t\t\t<tr scope=\"row\">\n"+this.templateColumnChild(childTable)+
                        "\t\t\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t\t\t\t</thead>\n"+
                        "\t\t\t\t\t\t\t<tbody>\n"+
                        "\t\t\t\t\t\t\t<#list "+parentTable+".list"+childTableUpper+" as "+childTable+">\n"+
                        "\t\t\t\t\t\t\t\t<tr>\n"+this.templateRowschildForList(childTable)+
                        "\t\t\t\t\t\t\t\t\t<td>\n"+
                        "\t\t\t\t\t\t\t\t\t\t<div>\n"+
                        "\t\t\t\t\t\t\t\t\t\t\t<a href=\"${'/"+parentTable+"/detail/"+childTable+"/update/'+ "+childTable+".getId() }\"><button class=\"btn btn-warning\">Modifier</button></a> | \n"+
                        "\t\t\t\t\t\t\t\t\t\t\t<a href=\"${'/"+parentTable+"/detail/"+childTable+"/delete/'+ "+parentTable+".getId()+'/'+ "+childTable+".getId() }\"><button class=\"btn btn-danger\">Supprimer</button></a>\n"+
                        "\t\t\t\t\t\t\t\t\t\t</div>\n"+
                        "\t\t\t\t\t\t\t\t\t</td>\n"+
                        "\t\t\t\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t\t\t\t</#list>\n"+
                        "\t\t\t\t\t\t\t</tbody>\n"+
                        "\t\t\t\t\t\t\t</table>\n"+
                        "\t\t\t\t\t\t</td>\n"+
                        "\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t</#if>";
                        return value;
    }

    private String templateDetailInListArrayManyToMany(String parentTable, String childTable){
        String childTableUpper = FunctionUtils.firstLetterToUpperCase(childTable);
        String value =  "\t\t\t\t<#if "+parentTable+".list"+childTableUpper+" ??>\n"+
                        "\t\t\t\t\t<tr class=\"text-muted w-lg-50\">\n"+
                        "\t\t\t\t\t\t<td>"+childTable+"</td>\n"+
                        "\t\t\t\t\t\t<td>:</td>\n"+
                        "\t\t\t\t\t\t<td>\n"+
                        "\t\t\t\t\t\t\t<table table border=\"1\" class=\"table\">\n"+
                        "\t\t\t\t\t\t\t<thead>\n"+
                        "\t\t\t\t\t\t\t<tr>\n"+this.templateColumnChild(childTable)+
                        "\t\t\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t\t\t\t</thead>\n"+
                        "\t\t\t\t\t\t\t<tbody>\n"+
                        "\t\t\t\t\t\t\t<#list "+parentTable+".list"+childTableUpper+" as "+childTable+">\n"+
                        "\t\t\t\t\t\t\t\t<tr>\n"+this.templateRowschildForList(childTable)+
                        "\t\t\t\t\t\t\t\t\t<td>\n"+
                        "\t\t\t\t\t\t\t\t\t\t<div>\n"+
                        "\t\t\t\t\t\t\t\t\t\t\t<a href=\"${'/"+parentTable+"/detail/"+childTable+"/update/'+ "+parentTable+".getId()+'/'+"+childTable+".getId() }\"><button class=\"btn btn-warning\">Modifier</button></a> | \n"+
                        "\t\t\t\t\t\t\t\t\t\t\t<a href=\"${'/"+parentTable+"/detail/"+childTable+"/delete/'+ "+parentTable+".getId()+'/'+ "+childTable+".getId() }\"><button class=\"btn btn-danger\">Supprimer</button></a>\n"+
                        "\t\t\t\t\t\t\t\t\t\t</div>\n"+
                        "\t\t\t\t\t\t\t\t\t</td>\n"+
                        "\t\t\t\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t\t\t\t</#list>\n"+
                        "\t\t\t\t\t\t\t</tbody>\n"+
                        "\t\t\t\t\t\t\t</table>\n"+
                        "\t\t\t\t\t\t</td>\n"+
                        "\t\t\t\t\t</tr>\n"+
                        "\t\t\t\t</#if>";
                        return value;
    }
    

    private String templateFieldChildTable(String nameEntity,String nameEntityChild){
        String value =  "\t\t\t\t<#if "+nameEntity+"."+nameEntityChild+" ??>\r\n"+
                        "\t\t\t\t\t<tr class=\"text-muted w-lg-50\">\n"+
                        "\t\t\t\t\t\t<td>"+nameEntityChild+"</td>\r\n" + //
                        "\t\t\t\t\t\t<td>:</td>\r\n" + //
                        "\t\t\t\t\t\t<td>\r\n" + //
                        "\t\t\t\t\t\t\t<table table border=\"1\" class=\"table\">\r\n" + //
                        "\t\t\t\t\t\t\t<thead>\n"+//
                        "\t\t\t\t\t\t\t\t<tr>\r\n" + this.templateColumnChild(nameEntityChild) +
                        "\t\t\t\t\t\t\t\t</tr>\r\n" + //
                        "\t\t\t\t\t\t\t</thead>\n"+//
                        "\t\t\t\t\t\t\t<tbody>\n"+//
                        "\t\t\t\t\t\t\t\t<tr>\r\n" + this.templateRowschild(nameEntity, nameEntityChild)+ //
                        "\t\t\t\t\t\t\t\t</tr>\r\n" + //
                        "\t\t\t\t\t\t\t</tbody>\n"+//
                        "\t\t\t\t\t\t\t</table>\r\n" + //
                        "\t\t\t\t\t\t</td>\r\n" + //
                        "\t\t\t\t\t</tr>\r\n"+
                        "\t\t\t\t</#if>";
        return value;
    }

    private String generateFieldChildTable(ConfigORM configORM){
        String nameEntity = this.viewDetail.getNameTable();
        String value = "";
        String parentTable = configORM.getName_table_parent();
        String childTable = configORM.getName_table_child();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        boolean isUniDirectionnal = configORM.isUniDirectionnal();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String optionConfiguration = configORM.getOptionConfiguration();

        // Unidirectional
        // 1-1 and 1-1
        // Opt: 1
        if(nameEntity.equals(childTable) && 
            assocParentChild.equals("1-1") &&
            assocChildParent.equals("1-1") && 
            isUniDirectionnal && optionConfiguration.equals("1")){
            value += this.templateFieldChildTable(nameEntity,parentTable);
        }
        // Opt: 2
        else if(nameEntity.equals(parentTable) && 
            assocParentChild.equals("1-1") &&
            assocChildParent.equals("1-1") && 
            isUniDirectionnal && optionConfiguration.equals("2")){
            value += this.templateFieldChildTable(nameEntity,childTable);
        }
        // bidirectional
        else if(nameEntity.equals(parentTable) && 
            assocParentChild.equals("1-1") &&
            assocChildParent.equals("1-1") &&
            isBiDirectionnal){
            value += this.templateFieldChildTable(nameEntity,childTable);
        }
        else if(nameEntity.equals(childTable) && 
            assocParentChild.equals("1-1") &&
            assocChildParent.equals("1-1") && 
            isBiDirectionnal){
            value += this.templateFieldChildTable(nameEntity,parentTable);
        }

        // Unidirectional 
        // 1-N and 1-1
        // Opt: 1
        else if (nameEntity.equals(childTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isUniDirectionnal && optionConfiguration.equals("1")){
            value += this.templateFieldChildTable(nameEntity,parentTable);
        }
        // Opt: 2
        else if(nameEntity.equals(parentTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isUniDirectionnal && optionConfiguration.equals("2")){
            // value += this.templateFieldChildTable(nameEntity,childTable);
            value += this.templateDetailInListArrayOneToMany(nameEntity,childTable);

        }
        // Bidirectional
        else if(nameEntity.equals(parentTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isBiDirectionnal){
            // value += this.templateFieldChildTable(nameEntity,childTable);
            value += this.templateDetailInListArrayOneToMany(nameEntity,childTable);
        }
        else if (nameEntity.equals(childTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isBiDirectionnal){
            value += this.templateFieldChildTable(nameEntity,parentTable);
        }
        // Bidirectional 
        // 1-N and 1-N
        else if (nameEntity.equals(childTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal){
            value += this.templateDetailInListArrayManyToMany(nameEntity,parentTable);
        }
        else if (nameEntity.equals(parentTable) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal){
            value += this.templateDetailInListArrayManyToMany(nameEntity,childTable);
        }
        
        else {
            value ="";
        }
        return value;
    }

    private String generateLinkToAddChild(ConfigORM configORM){
        String nameEntity = this.viewDetail.getNameTable();
        String parentEntity = configORM.getName_table_parent();
        String childEntity = configORM.getName_table_child();
        String assocParentChild = configORM.getAssoc_parent_child();
        String assocChildParent = configORM.getAssoc_child_parent();
        boolean isBiDirectionnal = configORM.isBiDirectionnal();
        String value = "";
        if(nameEntity.equals(parentEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-1") && 
            isBiDirectionnal) {
                value += "<a href=\"${'/"+parentEntity+"/detail/'+"+parentEntity+".id+'/"+childEntity+"/add' }\"><button class=\"btn btn-primary\">Ajouter "+childEntity+"</button></a>";
        }
        else if(nameEntity.equals(childEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal) {
                value += "<a href=\"${'/"+childEntity+"/detail/'+"+childEntity+".id+'/"+parentEntity+"/add' }\"><button class=\"btn btn-primary\">Ajouter "+parentEntity+"</button></a>";
        }
        else if(nameEntity.equals(parentEntity) && 
            assocParentChild.equals("1-N") &&
            assocChildParent.equals("1-N") && 
            isBiDirectionnal) {
                value += "<a href=\"${'/"+parentEntity+"/detail/'+"+parentEntity+".id+'/"+childEntity+"/add' }\"><button class=\"btn btn-primary\">Ajouter "+childEntity+"</button></a>";
        }
        else {
            value += "";
        }
        return value;
    }
    
    private String [] listRealValues(ConfigORM configORM){
        String nameEntity = this.viewDetail.getNameTable();
        if(nameEntity.equals(this.navBarLogOut)){
            String [] values = {
                nameEntity,
                generateFieldsTable(),
                generateFieldChildTable(configORM),
                generateLinkToAddChild(configORM),
                FunctionUtils.logOutTmpl()
            };
            return values;            
        }
        else {
            String [] values = {
                nameEntity,
                generateFieldsTable(),
                generateFieldChildTable(configORM),
                generateLinkToAddChild(configORM),
                ""
            };
            return values;
        }
    }

    private String formatEntityNameDetail(){
        return this.viewDetail.getNameTable()+"-detail";
    }

    public void createViewDetailEntity(ConfigORM configORM){
        File destinationFile = new File(ConfigSystem.path + Config.VIEWDETAIL_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileFtl(this.formatEntityNameDetail()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileFtl(formatEntityNameDetail()), Config.placeHoldersViewDetail, listRealValues(configORM),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_VIEWDETAIL_SOURCE_FILE_NAME,ConfigSystem.path + Config.VIEWDETAIL_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileFtl(formatEntityNameDetail())+" :File Already exist...");
        }  
    }

}
