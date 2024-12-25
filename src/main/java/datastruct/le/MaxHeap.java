package datastruct.le;

public class MaxHeap {
    private prioTask[] taskHeap;
    private int currentSize;
    private int maxSize;

    //--------------------------------Constructor--------------------------------//
    public MaxHeap()
    {
        this.currentSize = 0;
        this.maxSize = 255;
        this.taskHeap = new prioTask[maxSize];
    }

    public MaxHeap(MaxHeap heap){
        this.currentSize = heap.getCurrentSize();
        taskHeap = heap.getTaskHeap();
    }
    //--------------------------------Getters--------------------------------//

    public int getCurrentSize() {
        return currentSize;
    }

    public prioTask[] getTaskHeap() {
        return taskHeap;
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
        prioTask tmp;
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
 
    public void insert(prioTask element)
    {
        taskHeap[currentSize] = element;
 
        // Traverse up and fix violated property
        int current = currentSize;
        while (taskHeap[current].getPrioLevel() > taskHeap[parent(current)].getPrioLevel()) {
            swap(current, parent(current));
            current = parent(current);
        }
        currentSize++;
    }
 
    public prioTask extractMax()
    {
        prioTask popped = taskHeap[0];
        taskHeap[0] = taskHeap[--currentSize];
        maxHeapify(0);
        return popped;
    }

    public void print()
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
            str += taskHeap[i].getPrioLevel() + " ";
        return str;
    }
}
