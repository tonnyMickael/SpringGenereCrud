package MetaData;

public class MetaColumn {
    private String nameColumn,typeColumn;

    private void setNameColumn(String nameColumn) { this.nameColumn = nameColumn; }
    public String getNameColumn() { return nameColumn; }
    private void setTypeColumn(String typeColumn) { this.typeColumn = typeColumn; }
    public String getTypeColumn(){ return typeColumn; }

    public MetaColumn(String nameColumn, String type){
        setNameColumn(nameColumn); setTypeColumn(type);
    }

}
