package view;

import model.EntityField;

public class ViewModife {
    private String nameEntity;
    private EntityField [] entityFields;

    public ViewModife(String nameEntity, EntityField [] entityFields){
        this.nameEntity=nameEntity;
        this.entityFields=entityFields;
    }
    


    /**
     * @return String return the nameEntity
     */
    public String getNameEntity() {
        return nameEntity;
    }

    /**
     * @param nameEntity the nameEntity to set
     */
    public void setNameEntity(String nameEntity) {
        this.nameEntity = nameEntity;
    }

    /**
     * @return EntityField [] return the entityFields
     */
    public EntityField [] getEntityFields() {
        return entityFields;
    }

    /**
     * @param entityFields the entityFields to set
     */
    public void setEntityFields(EntityField [] entityFields) {
        this.entityFields = entityFields;
    }

}
