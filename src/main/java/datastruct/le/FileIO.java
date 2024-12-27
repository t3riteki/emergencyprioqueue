package datastruct.le;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class FileIO {
    private BufferedWriter writer;
    private BufferedReader reader;

//-----------------------------Save Files---------------------------//
    public void saveCode(hashingTable hash) {
        System.out.println("Saving Table to: code.csv \n");
        try {
            writer = new BufferedWriter(new FileWriter("FileIO\\code.csv"));
            LinkedList<Code>[] codeTable = hash.getHTable();

            for(int i = 0 ; i < codeTable.length; i ++){
                LinkedList<Code> list = codeTable[i];
                if(list != null){
                    for(int k = 0; k < list.size() ; k++ ){
                        Code currCode= list.get(k);
                        writer.write(currCode.getCode() + "," + currCode.getResources());
                        writer.newLine();
                    }
                }   
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHeap(MaxHeap heap){
        System.out.println("Saving Heap to: heap.csv \n");
        try {
            writer = new BufferedWriter(new FileWriter("FileIO\\heap.csv"));
            Task[] currHeap = heap.getTaskHeap();

            for(int i = 0; i < heap.getCurrentSize(); i ++){
                Task t = currHeap[i];
                writer.write(t.getTaskID()+ "," + t.getCode() + "," + t.getAddress() + "," + t.getPubtime() + "," + t.getSeverity());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//-----------------------------Load Files---------------------------// 
    public hashingTable loadCode(){
        System.out.println("Loading Table: code.csv \n");
        hashingTable curr = new hashingTable();
        try {
            reader = new BufferedReader(new FileReader("FileIO\\code.csv"));
            String out;
            while((out = reader.readLine()) != null){
                // System.out.println(out);
                String[] data = out.split(",");
                String code = "", resource = "";

                for (int i = 0; i < data.length ; i++){
                    if (i==0){
                        code = data[i];
                    }else if (i == data.length-1){
                        resource += data[i];
                    }else{
                        resource += data[i] + " , ";
                    }
                }
                // System.out.println("code: " + code + " resource: "+ resource);
                curr.insert_Chaining(new Code(code, resource));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();        
        }
        return curr;
    }

    public MaxHeap loadHeap(){
        System.out.println("Loading Heap: heap.csv \n");
        MaxHeap curr = new MaxHeap();
        try {
            reader = new BufferedReader(new FileReader("FileIO\\heap.csv"));
            String out;
            while((out = reader.readLine()) != null){
                String[] data = out.split(",");
                System.out.println(data[0]);
                curr.insert(new Task(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4])));
            }
            System.out.println("Loaded Heap: " + curr.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();        
        }
        return curr;
    }

}
