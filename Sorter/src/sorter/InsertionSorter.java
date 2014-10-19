package sorter;

/**
 *
 * @author kdbanman
 */
public class InsertionSorter {
    
    static {
        System.loadLibrary("libinsertionsort");
    }
    
    public native int[] insertionsort(int[] inarr);
}
