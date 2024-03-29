package view;

import model.EntityField;

public class ViewDetail {
    String nameTable;
    EntityField [] entityFields;
    String fieldsTable;
    String fieldChildTable;
    
    public ViewDetail(String nameTable, EntityField[] entityFields) {
        this.nameTable = nameTable;
        this.entityFields = entityFields;
    }
    public String getNameTable() {
        return nameTable;
    }
    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }
    public EntityField[] getEntityFields() {
        return entityFields;
    }
    public void setEntityFields(EntityField[] entityFields) {
        this.entityFields = entityFields;
    }
    public String getFieldsTable() {
        return fieldsTable;
    }
    public void setFieldsTable(String fieldsTable) {
        this.fieldsTable = fieldsTable;
    }
    public String getFieldChildTable() {
        return fieldChildTable;
    }
    public void setFieldChildTable(String fieldChildTable) {
        this.fieldChildTable = fieldChildTable;
    }

    
}
