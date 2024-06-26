package Creating;

public class ConstructionField {
    private String label, type;

    public ConstructionField(String label, String type){
        setLabel(label); setType(type);
    }

    private void setLabel(String label){ this.label = label; }
    private String getLabel(){ return label; }
    private void setType(String type){ this.type = type; }
    private String getType(){ return type; }

    public String getInputHtml(){
        return "<p>"+ getLabel() +": <input class=\"form-control\" type = \""+getType()+"\" name = \""+ getLabel() +"\"> </p>"+"\n\t\t\t\t" ;
    }
}
