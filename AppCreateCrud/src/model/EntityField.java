package model;


public class EntityField {

    private String name;
    private String type;
    private boolean isPrimaryKey;
    private String isNullable;
    private boolean isForeignKey;
    private String foreignKeyName;

    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
    public String getIsNullable() {
        return isNullable;
    }
    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }
    public boolean isForeignKey() {
        return isForeignKey;
    }
    public void setForeignKey(boolean isForeignKey) {
        this.isForeignKey = isForeignKey;
    }
    public String getForeignKeyName() {
        return foreignKeyName;
    }
    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }


}