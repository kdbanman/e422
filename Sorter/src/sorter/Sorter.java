package sorter;

/**
 *
 * @author kdbanman
 */
public class Sorter {

    /**
     * Prints CLI usage to stderr.
     */
    public static void usage() {
        // Get executable names
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
    public static void main(String[] args) {
        
     
        int[] testarr = { 
            100, 2, 300,
            4, 5, 600, 
            700, 8, 900, 900
        };
        HeapSorter hs = new HeapSorter();
        int[] out = hs.heapsort(testarr);
        
        for (int i : out) {
            System.out.println(i);
        }
        
        int[] testarr2 = { 
            100, 2, 300,
            4, 5, 600, 
            700, 8, 900, 900
        };
        
        InsertionSorter is = new InsertionSorter();
        out = is.insertionsort(testarr2);
        
        for (int i : out) {
            System.out.println(i);
        }
        
        // Get configuration from command line arguments.
        try {
            SorterConfig config = new SorterConfig(args);
        } catch (IllegalArgumentException e) {
            usage();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}