package sorter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author kdbanman
 */
class FileUtil {
    public static int[] readInputArray(String filepath) {
        // Read the file into an arraylist for flexible size first, then
        // convert it to required array format.
        ArrayList<Integer> results = new ArrayList<Integer>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while(scanner.hasNextInt()){
               results.add(scanner.nextInt());
            }
        } catch (FileNotFoundException nofile) {
            System.err.println(nofile.getMessage());
        }
        
        int[] resultArr = new int[results.size()];
        for (int i = 0; i < results.size(); i++) {
            resultArr[i] = results.get(i);
        }
        return resultArr;
    }
    
    public static void writeResults(int[] results, String filepath) {
        Writer writer = null;
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filepath), "utf-8"));
            
            for (int i = 0; i < results.length; i++) {
                writer.write(String.valueOf(results[i]) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Write to file " + filepath + " failed.");
            System.err.println("Ensure valid name and writable permissions.");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                System.err.println("Could not close file " + filepath);
                System.err.println("Corruption possible.");
            }
        }
    }
}
