package datastruct.le;

public class Code {
    private String code, resources;

    public Code(){
        this.code = "";
        this.resources = "";
    }

    public Code(String Code, String Resources){
        this.code = Code;
        this.resources = Resources;
    }

//--------------------------------Getter--------------------------------//
    public String getCode() {
        return code;
    }

    public String getResources() {
        return resources;
    }

//--------------------------------Setter--------------------------------//
    public void setCode(String code) {
        this.code = code;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

//--------------------------------Overrides--------------------------------//

    @Override
    public String toString() {
        return "{Code: " + getCode() + ", Resources: " + getResources()+"}";
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }


}
