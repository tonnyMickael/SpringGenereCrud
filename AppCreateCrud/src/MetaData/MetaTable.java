package MetaData;

import java.util.ArrayList;


public class MetaTable {
    private String nametable;
    private ArrayList<MetaColumn> Infotable;

    private void setNameTable(String name){ this.nametable = name; }
    public String getNameTable(){ return nametable; }
    private void setInfoTable(ArrayList<MetaColumn> infoTables){ this.Infotable = infoTables; } 
    public ArrayList<MetaColumn> getInfoTable(){ return Infotable; }

    public MetaTable(String name, ArrayList<MetaColumn> information){
        setNameTable(name); setInfoTable(information);
    }

}
