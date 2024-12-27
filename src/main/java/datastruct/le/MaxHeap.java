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
 
    public void maxHeapify(int pos)
    {
        if (isLeaf(pos)) {
            return;
        }
    
        int left = leftChild(pos);
        int right = rightChild(pos);
        int largest = pos;
    
        if (left < currentSize && taskHeap[left].getPrioLevel() > taskHeap[largest].getPrioLevel()) {
            largest = left;
        }
    
        if (right < currentSize && taskHeap[right].getPrioLevel() > taskHeap[largest].getPrioLevel()) {
            largest = right;
        }
    
        if (largest != pos) {
            swap(pos, largest);
            maxHeapify(largest);
        }
    }
 
    public void insert(Task element)
    {
        if (element.getTaskID() == 0) element.setTaskID(currentSize + 1);

        taskHeap[currentSize] = element;
 
        // Traverse up and fix violated property
        int current = currentSize;
        while (taskHeap[current].getPrioLevel() > taskHeap[parent(current)].getPrioLevel()) {
            swap(current, parent(current));
            current = parent(current);
        }
        currentSize++;
    }

    public boolean remove(Task element) {
        for (int i = 0; i < currentSize; i++) {
            if (taskHeap[i].equals(element)) { 
                swap(i, currentSize - 1);
                currentSize--; 
                maxHeapify(i); 
                if (i > 0) {
                    int parentIndex = parent(i);
                    while (i > 0 && taskHeap[i].getPrioLevel() > taskHeap[parentIndex].getPrioLevel()) {
                        swap(i, parentIndex);
                        i = parentIndex;
                        parentIndex = parent(i);
                    }
                }
                return true; 
            }
        }
        return false;
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

    //--------------------------------Traversal--------------------------------//
    @Override
    public String toString(){
        String str = "";
        for (int i = 0 ; i < currentSize ; i++)
            str += "{" + taskHeap[i].getTaskID() + ", " + taskHeap[i].getCode()+ ", " + taskHeap[i].getAddress()+ ", " + taskHeap[i].getPubtime() + ", " + taskHeap[i].getSeverity()+ ", " + taskHeap[i].getPrioLevel() + "} " ;
        return str;
    }
}
