package sorter;

/**
 * This is the entry point to the program, and serves as the variant executor.
 * The other major threads are the Watchdog timer and the (currently running) 
 * Variant.
 * 
 * See the README for notes and assumptions.
 * 
 * @author kdbanman
 */
public class Sorter {

    /**
     * Prints CLI usage to stderr.
     */
    public static void usage() {
        // Get executable name
        String name = new java.io.File(Sorter.class.getProtectionDomain()
                                        .getCodeSource()
                                        .getLocation()
                                        .getPath())
                                    .getName();
        
        System.err.print("Usage: " + name);
        System.err.println(" <infile> <outfile> <prob1> <prob2> <time>");
        System.err.println("     - infile: input file path");
        System.err.println("     - outfile: ouput file path");
        System.err.println("     - prob1: primary variant failure probability");
        System.err.println("     - prob2: backup variant failure probability");
        System.err.println("     - time: max interval allowed for variants");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FailureException {
        
        // Get configuration from command line arguments.
        SorterConfig config = null;
        try {
            config = new SorterConfig(args);
        } catch (InterfaceException e) {
            usage();
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        // Read array from input file
        
        boolean success = false;
        // Dispatch variants
        
            // Supervise variant with Watchdog timer
        
            // Execute adjudicator
            // if (adj.acceptable(results))
        
                // Call success if acceptance test passes
                // success(results);
                // success = true;
        
                // Throw LocalException if test is not passed
        
            // Catch watchdog timer 
                //Throw LocalException
        
        if (!success) {
            throw new FailureException("ERROR: Variants exhausted without success.");
        }
                
     
        int[] testarr = { 
            100, 2, 300,
            4, 5, 600, 
            700, 8, 900, 900
        };
        IntSorter s = new HeapSorter(config.getPrimaryFail());
        int[] out = s.sort(testarr);
        
        for (int i : out) {
            System.out.println(i);
        }
        
        int[] testarr2 = { 
            100, 2, 300,
            4, 5, 600, 
            700, 8, 900, 900
        };
        
        s = new InsertionSorter(config.getBackupFail());
        out = s.sort(testarr2);
        
        for (int i : out) {
            System.out.println(i);
        }
        
    }
}