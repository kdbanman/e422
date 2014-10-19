package sorter;

/**
 *
 * @author kdbanman
 */
public class SorterConfig {
    private String infile;
    private String outfile;
    private float primaryFail;
    private float backupFail;
    private int timer;
    
    private void fail(String msg) throws IllegalArgumentException {
        throw new IllegalArgumentException(msg);
    }
    
    public SorterConfig(String[] args) throws IllegalArgumentException {
        if (args.length != 5) fail("5 arguments required.");
    }
}