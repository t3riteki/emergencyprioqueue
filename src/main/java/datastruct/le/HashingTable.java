package  datastruct.le;
import java.util.LinkedList;

public class hashingTable {
    
    private LinkedList<Code>[] HTable;
    private int size;
    private final int maxSize = 30;

    public hashingTable(){
        this.HTable = new LinkedList[30];
        this.size = 0;
    }

    public hashingTable(int length){
        this.HTable = new LinkedList[length];
        this.size = 0;
    }

    public LinkedList<Code>[] getHTable() {
        return HTable;
    }

    public int getSize() {
        return size;
    }

    public int getHashvalue(Code code){
        return (code.getCode().hashCode() & 0x7fffffff)/2;
    }

    public void insert_Chaining(Code code){
        // System.out.println("Hash Val: " + getHashvalue(code));
        int tableIndex = getHashvalue(code) % HTable.length;
        // System.out.println("Table idx: " + tableIndex);
        if (HTable[tableIndex] == null){
            LinkedList<Code> nodeList = new LinkedList();
            nodeList.add(code);
            HTable[tableIndex] = nodeList;
            size++;
        }

        else {
            HTable[tableIndex].add(code);
        }
    }

    public void insert_LinearProbe(Code code){ 
        for (int i = 0; i < HTable.length ; i++){
            int tableIndex  = (getHashvalue(code) + i)%HTable.length;
            if (HTable[tableIndex] == null){
                LinkedList<Code> node = new LinkedList<>();
                node.add(code);
                HTable[tableIndex] = node;
                size++;
                break;
            }
        }
    }

    public void insert_QuadraticProbe(Code code){
        for (int i = 0; i < HTable.length ; i++){
            int tableIndex  = (getHashvalue(code) + (int)Math.pow(i, 2))%HTable.length;
            if (HTable[tableIndex] == null){
                LinkedList<Code> node = new LinkedList<>();
                node.add(code);
                HTable[tableIndex] = node;
                size++;
                break;
            }
        }
    }

    public void remove(Code code){ // grabe ka pangit pagka nest :<
        for(int i = 0 ; i < HTable.length; i++){
            int tableIndex = (getHashvalue(code) + i) % HTable.length;
            LinkedList<Code> nodeList = HTable[tableIndex];
            if(HTable[tableIndex] !=null){
                for(Code c : nodeList){
                    if (c.getCode() == code.getCode()) {
                        nodeList.remove(c); 
                        if (nodeList.size() == 0) {
                            HTable[tableIndex] = null;
                            size--;
                            return;
                        }
                        return;
                    }
                }
            }
        }
        System.out.println("Value not Found");
    }

    public Code search(Code code){
        for(int i = 0 ; i < HTable.length; i++){
            int tableIndex = (getHashvalue(code) + i) % HTable.length;
            LinkedList<Code> nodeList = HTable[tableIndex];
            if(HTable[tableIndex] !=null){
                for(Code c : nodeList){
                    if (c.getCode() == code.getCode()) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public String resourceLookUp(String code){
        for(int i = 0 ; i < HTable.length; i++){

            int hashVal = (code.hashCode() & 0x7fffffff)/2;
            int tableIndex = (hashVal + i) % HTable.length;

            LinkedList<Code> nodeList = HTable[tableIndex];
            if(HTable[tableIndex] !=null){
                for(Code c : nodeList){
                    if (c.getCode() == code) {
                        return c.getResources();
                    }
                }
            }
        }
        return "Null";
    }

    public void display(){
        System.out.println("Size: "+size);
        for (int i = 0 ; i < HTable.length; i++){
            LinkedList<Code> nodeList = HTable[i];
            if (nodeList != null){
                System.out.print("[" + i + "]" + " -> ");
                for(Code code : nodeList){
                    System.out.print(code.toString() + " ");
                }
                System.out.println();
            }
        }
    }
}
