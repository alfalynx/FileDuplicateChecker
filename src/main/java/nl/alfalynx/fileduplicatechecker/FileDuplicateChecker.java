package nl.alfalynx.fileduplicatechecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


/**
 * 
 *
 * @author Luke
 */
public class FileDuplicateChecker {
    
    static File selectedDirectory;
    
    static ArrayList<File> fetchedFiles = new ArrayList<>();
    static ArrayList<File> duplicateFiles = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("=====File Duplicate Checker v0.01=====");
        
        do {    
            System.out.print(
                "Enter the directory you'd like to scan for duplicate files: ");
            selectedDirectory = new File(in.nextLine());
            if (!selectedDirectory.isDirectory()) {
                System.err.println("Entered directory path is NOT valid!");
            }
        } while (!selectedDirectory.isDirectory());

        System.out.println("Fetching files...");
        scrape(selectedDirectory);
        Collections.sort(fetchedFiles, Collections.reverseOrder());
        System.out.println("Completed\n\nScanning files for duplicates...");
        compareFiles();
        System.out.println("Found " + duplicateFiles.size() + " duplicate files");
        System.out.println("\nFinished");
        
    }
    
    // Function to check for duplicates
    private static void compareFiles() throws IOException {
        for (File controlFile : fetchedFiles) {
            for (File testFile : fetchedFiles) {
                try {
                    int check = Arrays.compare(
                            Files.readAllBytes(controlFile.toPath()), 
                            Files.readAllBytes(testFile.toPath())
                        );
                    // Ensures the program doesn't remove the original file!!
                    if (check == 0 && !controlFile.equals(testFile)) {
                        duplicateFiles.add(testFile);
                        // System.out.println(
                        //        "\tDeleting duplicate file '"
                        //                +testFile.getName()
                        //                +"'");
                        // testFile.delete();
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
                    fetchedFiles.add(f);
                } else if (f.isDirectory()) {
                    scrape(f);
                }
            }
        } else {
            System.err.println("Entered value is not a folder!");
        }
    }
}