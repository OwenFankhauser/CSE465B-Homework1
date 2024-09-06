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
    
    // Stores all Z+- string variables and their names.
    public static HashMap<String, String> vari = new HashMap<String, String>();
    
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
        return null; // Unreachable
    }
    
    /**
     * This method does the majority of the interpreting. It will parse the
     *   lines of the file and distribute appropriately for processing.
     *   
     * @param in The Scanner object scanning the file
     */
    public static void interpret(Scanner in) {
        String line;
        while (in.hasNextLine()) {
            line = in.nextLine();
            assign(line);
        }
    }
    
    /**
     * This method assigns what function to execute based on the line's 
     *  keyword.
     * @param line The line to be read.
     */
    public static void assign(String line) {
        if (line.contains("FOR")) {
            loop(line);
        } else if (line.contains("=")) {
            define(line);
        } else if (line.contains("PRINT")) {
            display(line);
        }
    }
    
    /**
     * This method prints a variable's value.
     * 
     * @param line The line being processed.
     */
    private static void display(String line) {
        Scanner in = new Scanner(line);
        in.next();
        String key = in.next();
        if (!vari.containsKey(key)) {
            variableDoesNotExistException(key);
        }
        System.out.println(vari.get(key));
        in.close();
    }

    /**
     * This method acts as an Exception when a line references a variable that
     *  has no value or does not exist.
     *  
     * @param line The offending line
     */
    private static void variableDoesNotExistException(String key) {
        System.out.println("Variable: " + key + " has no value.");
        System.exit(0);
    }
    
    /**
     * This method parses a definition statement and directs it to the proper 
     *  process.
     *  
     * @param line The line being parse. 
     */
    private static boolean define(String line) {
        Scanner in = new Scanner(line);
        String variName = in.next();
        if (variName.length() > 1) {
            variableNameLengthException(line);
        }
        String operation = in.next();
        String added = in.nextLine();
        added = added.replaceAll(";", "");
        added = added.trim();
        in.close();
        switch (operation) {
        case "+=": addition(variName, added);
            break;
        case "-=": subtraction(variName, added);
            break;
        case "*=": multiplication(variName, added);
            break;
        case "=": vari.put(variName, added);
            break;
        default: operationDoesNotExistException(line, operation);
        in.close();
        return false;
        }
        in.close();
        return true;
    }

    /**
     * This method acts as an Exception when an operator is inputted 
     *  incorrectly.
     *  
     * @param line The offending line
     * @param operation The operator misinputted. 
     */
    private static void operationDoesNotExistException(String line,
            String operation) {
        System.out.println("The operation " + operation + " does not exist in line: ");
        System.out.println(line + ".");
        System.exit(0);
        
    }

    /**
     * This helper method defines variables with the multiplication operator.
     * 
     * @param variName The variable being added to or overridden.
     * @param added The String to be multiplied by the variable value. 
     */
    private static void multiplication(String variName, String added) {
     // Integer Definitions
        if (isNumeric(added)) {
            if (isNumeric(vari.get(variName))) {
                int num1 = Integer.valueOf(vari.get(variName));
                vari.put(variName, String.valueOf(num1 * Integer.valueOf(added)));
            } else {
                vari.put(variName, added);
            }
     // Variable Definitions
        } else if (added.length() == 1) {
            if (!vari.containsKey(added)) {
                variableDoesNotExistException(added);
            }
            vari.put(variName, 
                    String.valueOf(Integer.valueOf(vari.get(variName)) *
                    Integer.valueOf(vari.get(added))));
    // String Definitions N/A
        } else {
            cannotOperateOnStringException(variName);
        }
        
    }

    /**
     * This helper method defines variables with the subtraction operator.
     * 
     * @param variName The variable being added to or overridden.
     * @param added The String to be subtracted from the variable value. 
     */
    private static void subtraction(String variName, String added) {
     // Integer Definitions
        if (isNumeric(added)) {
            if (isNumeric(vari.get(variName))) {
                int num1 = Integer.valueOf(vari.get(variName));
                vari.put(variName, String.valueOf(num1 - Integer.valueOf(added)));
            } else {
                vari.put(variName, added);
            }
     // Variable Definitions
        } else if (added.length() == 1) {
            if (!vari.containsKey(added)) {
                variableDoesNotExistException(added);
            }
            vari.put(variName, 
                    String.valueOf(Integer.valueOf(vari.get(variName)) -
                    Integer.valueOf(vari.get(added))));
    // String Definitions N/A
        } else {
            cannotOperateOnStringException(variName);
        }
    }
    
    /**
     * This method acts as an Exception if a user tries to subtract, multiply,
     *  or divide a String variable.
     * 
     * @param variName
     */
    private static void cannotOperateOnStringException(String variName) {
        System.out.println("Cannot perform non-additive operations on String "
                + "variable " + variName + ".");
        System.exit(0);
    }

    /**
     * This helper method defines variables with the addition operator.
     * 
     * @param variName The variable being added to or overridden.
     * @param added The String to be added to the variable value. 
     */
    private static void addition(String variName, String added) {
     // Integer Definitions
        if (isNumeric(added)) {
            if (isNumeric(vari.get(variName))) {
                int num1 = Integer.valueOf(vari.get(variName));
                vari.put(variName, String.valueOf(num1 + 
                        Integer.valueOf(added)));
            } else {
                vari.put(variName, added);
            }
    // Variable Definitions
        } else if (added.length() == 1) {
            if (!vari.containsKey(added)) {
                variableDoesNotExistException(added);
            }
            vari.put(variName, 
                    String.valueOf(Integer.valueOf(vari.get(variName)) +
                    Integer.valueOf(vari.get(added))));
    // String Definitions
        } else {
            if (isNumeric(vari.get(variName))) {
                vari.put(variName, added);
            } else {
                vari.put(variName, (vari.get(variName) + added));
            }
        }
    }

    /**
     * This method acts as an exception thrown when a variable name is more 
     *  than one character in length. 
     * 
     * @param line The line that caused the runtime error.
     */
    private static void variableNameLengthException(String line) {
        System.out.println("Error at line: " + line);
        System.out.println("Variable names must be one character in length");
        System.exit(0);
    }

    /**
     * This method handles loops. The loop is parsed and executed as if it were
     *  assigned by the interpret method.
     *  
     * @param line The like containing the for loop.
     */
    private static void loop(String line) {
        String[]elements = line.split(" ");
        int count = Integer.parseInt(elements[1]);
        String operation = line.substring(line.indexOf(elements[2]));
        operation.trim();
        String[] lines = operation.split(";");
        for (int i = 0; i < count; i++) {
            for (String execute : lines) {
                assign(execute.replace("ENDFOR", "").trim());
            }
            
        }
    }
    
    /**
     * This method checks if a string is numeric.
     * 
     * @param line The string to be checked.
     * @return true/false
     */
    public static boolean isNumeric(String line) {
        if (line == null) {
            return false;
        }
        try {
            double check = Double.parseDouble(line);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * This main method calls manages validation and then calls for 
     *  interpretation of the Z+- file.
     *  
     * @param args The arguments provided in console.
     */
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
