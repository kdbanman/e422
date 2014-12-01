package sorter;

/**
 * Variable accesses are counted with the parent class's varAcc() every
 * time a variable is read or written to.
 * 
 * @author kdbanman - adapted from http://tinyurl.com/nwhu839
 */
public class HeapSorter extends IntSorter{
    
    public HeapSorter(float failProb) {
        super(failProb);
    }
    
    /**
     * Standard heapsort.
     * @param a an array of int items.
     */
    @Override
    public int[] sort(int[] input)
    {
        // duplicate array so original is not mutated
        int[] a = new int[input.length];
        
        varAcc(); // read length
        varAcc(); // create array
        
        varAcc(); // create i
        for (int i = 0; i < input.length; i++) {
            a[i] = input[i];
            
            varAcc(); // read input
            varAcc(); // read i
            varAcc(); // write a
            varAcc(); // read i
            varAcc(); // increment i read
            varAcc(); // increment i write
            varAcc(); // loop condition
            varAcc(); // loop condition
        }
        
        varAcc(); // read length
        varAcc(); // create i
        varAcc(); // loop condition
        for(int i = a.length / 2; i >= 0; i--) {
            percDown(a, i, a.length);
            
            varAcc(); // decrement i read
            varAcc(); // decrement i write
            varAcc(); // loop condition
        }
        
        varAcc(); // read length
        varAcc(); // create i
        varAcc(); // loop condition
        for(int i = a.length - 1; i > 0; i--)
        {
            swapReferences(a, 0, i);
            percDown(a, 0, i);
            
            varAcc(); // decrement i read
            varAcc(); // decrement i write
            varAcc(); // loop condition
        }
        
        if (didFail()) return null;  // see parent class IntSorter
        
        return a;
    }

    /**
     * @param i the index of an item in the heap.
     * @return the index of the left child.
     */
    private int leftChild(int i)
    {
        varAcc(); // read i
        
        return 2 * i + 1;
    }

    /**
     * @param a an array of int items.
     * @index i the position from which to percolate down.
     * @int n the logical size of the binary heap.
     */
    private void percDown(int[] a, int i, int n)
    {
        varAcc(); // create child
        varAcc(); // create tmp
        
        int child;
        int tmp;

        varAcc(); // read i
        varAcc(); // read a
        varAcc(); // write tmp
        varAcc(); // loop condition
        for(tmp = a[i]; leftChild(i) < n; i = child)
        {
            child = leftChild(i);
            
            varAcc(); // write child
            
            varAcc(); // read n
            varAcc(); // read child
            varAcc(); // read child
            varAcc(); // read a
            varAcc(); // read child
            varAcc(); // read a
            if(child != n - 1 && a[child] < a[child + 1]) {
                child++;
                
                varAcc(); // read child
                varAcc(); // write child
            }
            
            varAcc(); // read tmp
            varAcc(); // read child
            varAcc(); // read a
            if(tmp < a[child]) {
                a[i] = a[child];
                
                varAcc(); // read child
                varAcc(); // read i
                varAcc(); // read a
                varAcc(); // write a
            } else
                break;
        }
        a[i] = tmp;
        
        varAcc(); // read tmp
        varAcc(); // read i
        varAcc(); // write a
    }
    
    
    /**
     * @param a an array of objects.
     * @param index1 the index of the first object.
     * @param index2 the index of the second object.
     */
    public void swapReferences(int[] a, int index1, int index2)
    {
        varAcc(); // read index1
        varAcc(); // read a
        varAcc(); // write tmp
        int tmp = a[index1];
        
        varAcc(); // read index2
        varAcc(); // read a
        varAcc(); // read index1
        varAcc(); // write a
        a[index1] = a[index2];
        
        varAcc(); // read tmp
        varAcc(); // read index2
        varAcc(); // write a
        a[index2] = tmp;
    }
}
