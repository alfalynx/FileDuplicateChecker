package nl.alfalynx.fileduplicatechecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


/**
 * 
 *
 * @author Luke
 */
public class FileDuplicateChecker {
    
    static File selectedDirectory;
    static int duplicateCounter = 0;
    
    static ArrayList<File> fetchedFiles = new ArrayList<>();
    
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
        System.out.println("Completed\nScanning files for duplicates...");
        compareFiles();
        System.out.printf("Removed %d duplicate files\n", duplicateCounter);
        moveToMain();
        System.out.println("Cleaning up left over sub-directories");
        removeSubDirectories(selectedDirectory);
        System.out.println("\nFinished");
    }
    
    
    // Ensures every file is placed into selectedDirectory
    private static void moveToMain() {
        fetchedFiles.clear();
        scrape(selectedDirectory);
        for (File f : fetchedFiles) {
            f.renameTo(new File(String.format("%s/%s", 
                            selectedDirectory.getAbsolutePath(), f.getName())
            ));
        }
    }
    

    private static void removeDuplicate(File file) {
        try {
            System.out.printf("\tDeleting duplicate file '%s'\n",
                    file.getName());
            file.delete();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    // Removes any left over sub-directories after moveToMain() is called
    private static void removeSubDirectories(File folder) {
        if (folder.isDirectory()) {
            for (File d : folder.listFiles()) {
                if (d.isDirectory()) {
                    d.delete();
                }
            }
        } else {
            System.err.println("Entered value is not a folder!");
        }
    }
    
    
    // Compares files to eachother to scan for duplicates
    private static void compareFiles() throws IOException {
        for (File controlFile : fetchedFiles) {
            for (File testFile : fetchedFiles) {
                try {
                    long check = Files.mismatch(controlFile.toPath(), testFile.toPath());
                    
                    // Ensures the program does NOT remove the original file!!
                    if (check == -1L && !controlFile.equals(testFile)) {
                        removeDuplicate(testFile);
                        duplicateCounter++;
                    }
                } catch (java.nio.file.NoSuchFileException e) {
                } catch(IOException e) {
                    System.err.println(e);
                }
            }
        } 
    }
    
    // Obtains files from directory and all sub-directories, is recursive
    private static void scrape(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
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