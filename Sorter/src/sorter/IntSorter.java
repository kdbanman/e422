package sorter;

import java.util.Random;

/**
 *
 * @author kdbanman
 */

public abstract class IntSorter {
    private float numVarAccess;
    private float failProb;
    
    public IntSorter(float failureProb) {
        numVarAccess = 0f;
        failProb = failureProb;
    }
    /**
     * @param inputArray array to be sorted
     * @param failureProb failure probability on variable access
     * @return sorted (new) array
     */
    public abstract int[] sort(int[] inputArray);
    
    /**
     * Every time a variable is accessed, increment the number of variable
     * accesses by two (to account for this function's read and write.)
     */
    protected void varAcc() {
        numVarAccess += 3f;
    }
    
    public boolean didFail() {
        float hazard = numVarAccess * failProb;
        float draw = new Random().nextFloat();
        return draw >= 0.5 && draw <= 0.5 + hazard;
    }
}
