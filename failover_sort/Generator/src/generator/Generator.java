package generator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

/**
 *
 * @author kdbanman
 */
public class Generator {

    /**
     * Constructs, populates, and returns an array filled with integers.
     * Integer members are chosen randomly by java.util.Random#nextInt.
     * 
     * @param size length of array to be generated
     * @return 
     */
    public static int[] generateArray(int size) {
        if (size < 1) {
            System.err.println("Can only generate 1 or more numbers.");
            System.exit(1);
        }
        
        int[] arr = new int[size];
        
        Random rand = new Random();
        
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt();
        }
        
        return arr;
    }
    
    /**
     * Attempts to write the passed integer array to a newline sepaarated file.
     * Exceptions are not thrown, the program exits on error.
     * 
     * @param arr Integer array to write to file
     * @param filename path of file to write
     */
    public static void writeArray(int[] arr, String filename) {
        Writer writer = null;
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "utf-8"));
            
            for (int i = 0; i < arr.length; i++) {
                writer.write(String.valueOf(arr[i]) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Write to file " + filename + " failed.");
            System.err.println("Ensure valid name and writable permissions.");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                System.err.println("Could not close file " + filename);
                System.err.println("Corruption possible.");
            }
        }
    }
    
    /**
     * Prints CLI usage to stderr.
     */
    public static void usage() {
        String name = new java.io.File(Generator.class.getProtectionDomain()
                                        .getCodeSource()
                                        .getLocation()
                                        .getPath())
                                    .getName();
        System.err.println("Usage: " + name + " <filename> <number>");
        System.err.println("       - filename: output filename");
        System.err.println("       - number: number of integers to generate");
    }
    
    /**
     * Generates an integer array and writes the integers to a newline
     * separated file.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
            System.exit(1);
        }
        
        try {
            int size = Integer.valueOf(args[1]);
            int[] arr = generateArray(size);
            writeArray(arr, args[0]);
        } catch (NumberFormatException e) {
            usage();
            System.exit(1);
        }
    }
}
