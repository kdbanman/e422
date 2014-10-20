package sorter;

/**
 *
 * @author kdbanman
 */
public class Variant extends Thread {
    private int[] inputArray;
    private IntSorter sorter;
    
    private int[] results;
    
    public Variant(int[] inputArray, IntSorter sorter) {
        this.inputArray = inputArray;
        this.sorter = sorter;
        
        results = null;
    }
    
    @Override
    public void run() {
        results = sorter.sort(inputArray);
    }
    
    public int[] getResults() {
        return results;
    }
}
