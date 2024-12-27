package datastruct.le;

// Reference and Templated from https://www.geeksforgeeks.org/max-heap-in-java/ big thanks, big thanks


public class MaxHeap {
    private Task[] taskHeap;
    private int currentSize;
    private final int maxSize = 255;

    //--------------------------------Constructor--------------------------------//
    public MaxHeap(){
        this.taskHeap = new Task[maxSize];
        this.currentSize = 0;
    }

    public MaxHeap(MaxHeap heap){
        taskHeap = heap.getTaskHeap();
        this.currentSize = heap.getCurrentSize();
    }
    //--------------------------------Getters--------------------------------//

    public int getCurrentSize() {
        return currentSize;
    }

    public Task[] getTaskHeap() {
        Task[] minimizedHeap = new Task[currentSize];
        System.arraycopy(this.taskHeap, 0, minimizedHeap, 0, currentSize);
        return minimizedHeap;
    }

    public int getMaxSize() {
        return maxSize;
    }

    
    //--------------------------------Traversal--------------------------------//
    private int parent(int pos) { return (pos - 1) / 2; }
 
    private int leftChild(int pos) { return (2 * pos) + 1; }
 
    private int rightChild(int pos) { return (2 * pos) + 2;}


    //--------------------------------Methods--------------------------------//
    private boolean isLeaf(int pos)
    {
        if (pos > (currentSize / 2) && pos <= currentSize) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos)
    {
        Task tmp;
        tmp = taskHeap[fpos];
        taskHeap[fpos] = taskHeap[spos];
        taskHeap[spos] = tmp;
    }
 
    private void maxHeapify(int pos)
    {
        if (isLeaf(pos))
            return;
 
        if (taskHeap[pos].getPrioLevel() < taskHeap[leftChild(pos)].getPrioLevel()
            || taskHeap[pos].getPrioLevel() < taskHeap[rightChild(pos)].getPrioLevel()) {
 
            if (taskHeap[leftChild(pos)].getPrioLevel()
                > taskHeap[rightChild(pos)].getPrioLevel()) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
    }
 
    public void insert(Task element)
    {
        element.setTaskID(currentSize + 1);
        taskHeap[currentSize] = element;
 
        // Traverse up and fix violated property
        int current = currentSize;
        while (taskHeap[current].getPrioLevel() > taskHeap[parent(current)].getPrioLevel()) {
            swap(current, parent(current));
            current = parent(current);
        }
        currentSize++;
    }
 
    public Task extractMax()
    {
        Task popped = taskHeap[0];
        taskHeap[0] = taskHeap[--currentSize];
        maxHeapify(0);
        return popped;
    }

    public void display()
    {
 
        for (int i = 0; i < currentSize / 2; i++) {
 
            System.out.print("Parent Node : " + taskHeap[i].getPrioLevel());
 
            if (leftChild(i)
                < currentSize) // if the child is out of the bound
                        // of the array
                System.out.print(" Left Child Node: "
                                 + taskHeap[leftChild(i)].getPrioLevel());
 
            if (rightChild(i)
                < currentSize) // the right child index must not
                        // be out of the index of the array
                System.out.print(" Right Child Node: "
                                 + taskHeap[rightChild(i)].getPrioLevel());
 
            System.out.println(); // for new line
        }
    }

    public String localResourceLookUp(String code){
        String necResource;

        switch (code) {
            case "Fire": necResource = "Firefighters, Paramedics "; break;
            case "Theft": necResource = "Police"; break;
            case "Murder": necResource = "Police, Paramedics"; break;
            case "Hostage": necResource = "Police"; break;
            case "Shooting": necResource = "Police, Paramedics"; break;
            case "Building Collapse": necResource = "Firefighters, Paramedics, Rescuers"; break;
            case "Rescue Situation": necResource = "Rescuers, paramedics"; break;
            case "Animal Emergencies": necResource = "Firefighters, Emergency Veterinarians"; break;
            default: necResource = "Null "; break;
        }

        return necResource;
    }

    //--------------------------------Traversal--------------------------------//
    @Override
    public String toString(){
        String str = "";
        for (int i = 0 ; i < currentSize ; i++)
            str += taskHeap[i].getPrioLevel() + " ";
        return str;
    }
}
