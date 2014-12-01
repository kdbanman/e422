package sorter;

/**
 *
 * @author kdbanman
 */
public class Adjudicator {
    public static boolean resultsAcceptable(int[] results) {
        // detect simulated hardware failure
        if (results == null) return false;
        
        // make sure results array is ascending
        for (int i = 1; i < results.length; i++) {
            if (results[i-1] > results[i]) return false;
        }
        
        // results passed
        return true;
    }
}
