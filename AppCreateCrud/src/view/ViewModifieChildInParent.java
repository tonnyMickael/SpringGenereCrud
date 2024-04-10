package view;

import model.EntityField;

public class ViewModifieChildInParent {
    private String nameEntity;
    private EntityField [] entityFields;
    
    public ViewModifieChildInParent(String nameEntity, EntityField[] entityFields) {
        this.nameEntity = nameEntity;
        this.entityFields = entityFields;
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
