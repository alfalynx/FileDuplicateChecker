package nl.alfalynx.fileduplicatechecker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * 
 *
 * @author Luke
 */
public class Main {
    
    static ArrayList<File> results = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        
        File selectedDirectory = new File("C:\\Users\\Luke\\Pictures\\temp");
        System.out.println("Gathering files...");
        scrape(selectedDirectory);
        Collections.sort(results, Collections.reverseOrder());
        System.out.println("Completed\n\nScanning files for duplicates...");
        compareFiles();
        System.out.println("\nFinished");
        
    }
    
    // Function to check for duplicates
    private static void compareFiles() throws IOException {
        for (File controlFile : results) {
            for (File testFile : results) {
                try {
                    int check = Arrays.compare(
                            Files.readAllBytes(controlFile.toPath()), 
                            Files.readAllBytes(testFile.toPath())
                        );
                    // Ensures the program doesn't remove the original file!!
                    if (check == 0 && !controlFile.equals(testFile)) {
                        System.out.println(
                                "\tDeleting duplicate file '"
                                        +testFile.getName()
                                        +"'");
                        testFile.delete();
                    }
                } catch (IOException e) {
                    //System.err.println(e);
                }
            }   
        } 
    }
    
    // Function to obtain files from folder and all sub-folders, is recursive
    private static void scrape(File folder) {
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    results.add(f);
                } else if (f.isDirectory()) {
                    scrape(f);
                }
            }
        } else {
            System.err.println("Entered value is not a folder!");
        }
    }
}
