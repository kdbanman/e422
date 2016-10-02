package sorter;

/**
 *
 * @author kdbanman
 */
public class InsertionSorter extends IntSorter {
    private float failProb;
    
    static {
        // It's *insane* that this works, but 
        // System.loadLibrary("libinsertionsort") doesn't
        System.load(System.getProperty("java.library.path") + 
                    "/libinsertionsort.so");
    }
    
    public InsertionSorter(float failureProb) {
        super(failureProb);
        failProb = failureProb;
    }
    
    @Override
    public int[] sort(int[] inarr) {
        return nativesort(inarr, failProb);
    }
    
    private native int[] nativesort(int[] inarr, float failprob);
}
