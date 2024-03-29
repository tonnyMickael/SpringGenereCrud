package model;

public class ForeignKey {
    private String name;
    private String sourceTable;
    private String sourceColumn;
    private String targetTable;
    private String targetColumn;
    
    public ForeignKey(){}

    public ForeignKey(String name, String sourceTable, String sourceColumn, String targetTable, String targetColumn) {
        this.name = name;
        this.sourceTable = sourceTable;
        this.sourceColumn = sourceColumn;
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSourceTable() {
        return sourceTable;
    }
    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }
    public String getSourceColumn() {
        return sourceColumn;
    }
    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }
    public String getTargetTable() {
        return targetTable;
    }
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }
    public String getTargetColumn() {
        return targetColumn;
    }
    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }
    
}
