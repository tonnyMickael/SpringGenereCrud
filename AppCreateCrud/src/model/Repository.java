package model;

/**
 * Repository
 */
public class Repository {

    private String className;
    private String idType;

    public Repository(){
        
    }
    
    public Repository(String className, String idType) {
        this.className = className;
        this.idType = idType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    
}