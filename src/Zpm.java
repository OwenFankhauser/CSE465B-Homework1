/*
 * Copyright Owen Fankhauser September 2024
 * 
 * CSE 465B
 * Professor Amjad
 * 
 * ● (45 pts) Write an interpreter in Java to execute Z+- programs
 * ○ (10 pts) Basic structure, integer variables only
 * ○ (10 pts) Basic structure, integer and string variables
 * ○ (20 pts) For loops
 * ○ (5  pts) Detection of runtime errors
 * 
 */

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Zpm {

    // Stores all Z+- integer variables and their names.
    public HashMap<String,Integer> numVari = new HashMap<String, Integer>();
    
    // Stores all Z+- string variables and their names.
    public HashMap<String, String> strVari = new HashMap<String, String>();
    
    /**
     * This method validates the Z+- file and handles the exception if a 
     *  non.zpm file is attempted to process. 
     * 
     * @param fileName The name of the file entered.
     * @return in The Scanner object produced if the file is successfully 
     *  validated.
     */
    public static Scanner readFile(String fileName) {
        try {
            File zpmFile = new File(fileName);
            Scanner in = new Scanner(zpmFile);
            return in;
        } catch (Exception e) { // Incorrect file
            System.out.println("Please use a Z+- file with the propper '.zpm' extension.");
            System.exit(0);
        }
        return null; // TODO clean this 
    }
    
    /**
     * This method does the majority of the interpreting. It will parse the
     *  
     *   lines of the file and distribute appropriately for processing.
     * @param in The Scanner object scanning the file
     */
    public static void interpret(Scanner in) {
        System.out.println("Success!");
    }
    
    public static void main(String[] args) {
        // Validate file
        if (!args[0].contains(".zpm")){
            System.out.println("Please use a Z+- file with the propper '.zpm' extension.");
            System.exit(0);
        }
        Scanner in = readFile(args[0]);
        // Interpret file
        interpret(in);
        // Close Scanner and end task
        in.close();
    }

}
