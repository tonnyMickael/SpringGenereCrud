package controller;

import model.EntityField;

public class ControllerEntity {
    private String nameEntity;
    private String idTypEntity;
    private EntityField [] entityFields;
    

    public String getIdTypEntity() {
        return idTypEntity;
    }

    public void setIdTypEntity(String idTypEntity) {
        this.idTypEntity = idTypEntity;
    }

    public ControllerEntity(String nameEntity) {
        this.nameEntity = nameEntity;
    }

    public String getNameEntity() {
        return nameEntity;
    }

    public void setNameEntity(String nameEntity) {
        this.nameEntity = nameEntity;
    }

    public EntityField[] getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(EntityField[] entityFields) {
        this.entityFields = entityFields;
    }
    
}
