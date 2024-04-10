package view;

import model.EntityField;

public class ViewModifieParentInChild {
    private String nameEntity;
    private EntityField [] entityFields;
    
    public ViewModifieParentInChild(String nameEntity, EntityField[] entityFields) {
        this.nameEntity = nameEntity;
        this.entityFields = entityFields;
    }
    public EntityField[] getEntityFields() {
        return entityFields;
    }
    public void setEntityFields(EntityField[] entityFields) {
        this.entityFields = entityFields;
    }
    public String getNameEntity() {
        return nameEntity;
    }
    public void setNameEntity(String nameEntity) {
        this.nameEntity = nameEntity;
    }
}
