package sorter;

/**
 *
 * @author kdbanman - adapted from http://tinyurl.com/nwhu839
 */
public class HeapSorter {
    
    /**
     * Standard heapsort.
     * @param a an array of int items.
     */
    public int[] heapsort(int[] input)
    {
        // duplicate array so original is not mutated
        int[] a = new int[input.length];
        for (int i = 0; i < input.length; i++)
            a[i] = input[i];
        
        for(int i = a.length / 2; i >= 0; i--)
            percDown(a, i, a.length);
        for(int i = a.length - 1; i > 0; i--)
        {
            swapReferences(a, 0, i);
            percDown(a, 0, i);
        }
        
        return a;
    }

    /**
     * @param i the index of an item in the heap.
     * @return the index of the left child.
     */
    private int leftChild(int i)
    {
        return 2 * i + 1;
    }

    /**
     * @param a an array of int items.
     * @index i the position from which to percolate down.
     * @int n the logical size of the binary heap.
     */
    private void percDown(int[] a, int i, int n)
    {
        int child;
        int tmp;

        for(tmp = a[i]; leftChild(i) < n; i = child)
        {
            child = leftChild(i);
            if(child != n - 1 && a[child] < a[child + 1])
                child++;
            if(tmp < a[child])
                a[i] = a[child];
            else
                break;
        }
        a[i] = tmp;
    }
    
    
    /**
     * @param a an array of objects.
     * @param index1 the index of the first object.
     * @param index2 the index of the second object.
     */
    public void swapReferences(int[] a, int index1, int index2)
    {
        int tmp = a[index1];
        a[index1] = a[index2];
        a[index2] = tmp;
    }

}
