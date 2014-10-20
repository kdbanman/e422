package sorter;

/**
 *
 * @author kdbanman
 */
public class Variant extends Thread {
    private int[] inputArray;
    private IntSorter sorter;
    private boolean interrupted;
    
    private int[] results;
    
    public Variant(int[] inputArray, IntSorter sorter) {
        this.inputArray = inputArray;
        this.sorter = sorter;
        interrupted = false;
        
        results = null;
    }
    
    @Override
    public void run() {
        try {
            results = sorter.sort(inputArray);
        } catch (ThreadDeath e) {
            interrupted = true;
            throw new ThreadDeath();
        }
    }
    
    public int[] getResults() throws InterruptedException {
        if (interrupted) throw new InterruptedException();
        return results;
    }
}
