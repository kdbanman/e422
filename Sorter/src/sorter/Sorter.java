package sorter;

import java.util.Timer;

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
     * Dispatches a Variant to a new, Watchdog-supervised thread. Returns true
     * on success.
     * 
     * @param var Variant to be dispatched.
     * @param config program configuration
     * @return true on success
     * @throws LocalException all failure conditions throw LocalException
     */
    public static boolean dispatch(Variant var, SorterConfig config) throws LocalException {
        
            // Supervise variant with Watchdog timer
            Timer scheduler = new Timer();
            Watchdog wdog = new Watchdog(var);
            scheduler.schedule(wdog, config.getTimer());
            
            var.start();
            
            try {
                // Wait for results
                var.join();
                scheduler.cancel();
                // Test results
                if (Adjudicator.resultsAcceptable(var.getResults())) {
                    // Use valid results
                    writeResults(var.getResults(), config.getOutFile());
                    return true;
                }
                // Throw LocalException because test was not passed
                throw new LocalException("WARN: Variant results unacceptable.");
                
            // Catch watchdog timer
            } catch (InterruptedException e) {
                throw new LocalException("WARN: Variant timed out.");
            }
    }
    
    public static void writeResults(int[] outArr, String dest) {
        for (int i = 0; i < outArr.length; i++) {
            System.out.println(outArr[i]);
        }
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
        int[] inputArray = { 
            100, 2, 300, 4, 5, 600, 100, 2, 300, 4, 5, 600, 100, 2, 300,
            4, 5, 600, 
            100, 2, 300,
            4, 5, 600, 
            700, 8, 900, 900
        };
       
        // Initialize variants
        Variant[] variants = new Variant[2];
        
        IntSorter primary = new HeapSorter(config.getPrimaryFail());
        variants[0] = new Variant(inputArray, primary);
        
        IntSorter backup = new InsertionSorter(config.getBackupFail());
        variants[1] = new Variant(inputArray, backup);
        
        // Dispatch variants
        boolean success = false;
        for (int i = 0; i < variants.length; i++) {
            try {
                success = dispatch(variants[i], config);
                if (success) break;
            } catch (LocalException e) {
                System.err.println(e.getMessage());
                System.err.println("Execution continuing...");
            }
        }
        
        if (success)  {
            System.out.println("Variant success encountered.");
            System.out.println("Output written to " + config.getOutFile());
        } else {
            throw new FailureException("ERR: Variants exhausted. No success.");
        }
    }
}