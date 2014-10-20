package sorter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 *
 * @author kdbanman
 */
public class SorterConfig {
    private String infile;
    private String outfile;
    private float primaryfail;
    private float backupfail;
    private int timer;
    
    private void fail(String msg) throws IllegalArgumentException {
        throw new IllegalArgumentException("ERROR: " + msg);
    }
    
    /**
     * Parses a standard command line arguments string array into an app config.
     * @param args standard CLI args array
     * @throws IllegalArgumentException on parse error
     */
    public SorterConfig(String[] args) throws IllegalArgumentException {
        if (args.length != 5) fail("5 arguments required.");
        
        if (!Files.isReadable(FileSystems.getDefault().getPath(args[0])))
            fail("Input file must be readable.");
        infile = args[0];
        
        if (!canWrite(args[1]))
            fail("Output destination must be writable.");
        outfile = args[1];
        
        try {
            primaryfail = Float.valueOf(args[2]);
            backupfail = Float.valueOf(args[3]);
        } catch (NumberFormatException e) {
            fail("Probabilities must be parsable as floating point.");
        }
        if (primaryfail < 0f || primaryfail > 1f)
            fail("Primary failure probability must be within [0,1].");
        if (backupfail < 0f || backupfail > 1f)
            fail("Primary failure probability must be within [0,1].");
        
        try {
            timer = Integer.valueOf(args[4]);
        } catch (NumberFormatException e) {
            fail("Timer interval must be parsable as integer.");
        }
        if (timer <= 0)
                fail("Timer interval must be greater than zero.");
    }
    
    private boolean canWrite(String filePath) {
        try {
            // Create, write to, and delete test file in desired directory
            File test;
            test = new File(FileSystems.getDefault()
                                       .getPath(filePath)
                                       .toAbsolutePath()
                                       .getParent()
                                       .toFile(), "deleteme");
            new FileOutputStream(test).write(42);
            test.delete();
            
            // Directory has write permissions
            return true;
        } catch (IOException e) {
            // Directory does not have write permissions
            return false;
        }
        
    }
    
    public String getInFile() {
        return infile;
    }
    public String getOutFile() {
        return outfile;
    }
    public float getPrimaryFail() {
        return primaryfail;
    }
    public float getBackupFail() {
        return backupfail;
    }
    public int getTimer() {
        return timer;
    }
}