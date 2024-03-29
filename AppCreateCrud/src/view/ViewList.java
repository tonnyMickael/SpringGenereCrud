package view;
import model.EntityField;

/*
 * Object Class for List view
 */
public class ViewList {
    private String nameEntity;
    private EntityField [] entityFields;
    private String detailNameLink;
    private String updateNameLink;
    private String deleteNameLink;
    
    public ViewList(String nameEntity, EntityField[] entityFields, String detailNameLink,String updateNameLink, String deleteNameLink) {
        this.nameEntity = nameEntity;
        this.entityFields = entityFields;
        this.detailNameLink = detailNameLink;
        this.updateNameLink = updateNameLink;
        this.deleteNameLink = deleteNameLink;
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
    public String getUpdateNameLink() {
        return updateNameLink;
    }
    public void setUpdateNameLink(String updateNameLink) {
        this.updateNameLink = updateNameLink;
    }
    public String getDeleteNameLink() {
        return deleteNameLink;
    }
    public void setDeleteNameLink(String deleteNameLink) {
        this.deleteNameLink = deleteNameLink;
    }
    public String getDetailNameLink() {
        return detailNameLink;
    }
    public void setDetailNameLink(String detailNameLink) {
        this.detailNameLink = detailNameLink;
    }
    
}
