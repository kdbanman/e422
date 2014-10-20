package sorter;

/**
 *
 * @author kdbanman
 */
public class InsertionSorter {
    
    static {
        // It's *insane* that this works, but 
        // System.loadLibrary("libinsertionsort") doesn't
        System.load(System.getProperty("java.library.path") + 
                    "/libinsertionsort.so");
    }
    
    public native int[] insertionsort(int[] inarr);
}
