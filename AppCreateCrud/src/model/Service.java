package model;

public class Service {
    String name;
    String idType;
    
    public Service(){}

    public Service(String name, String idType) {
        this.name = name;
        this.idType = idType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }


}
