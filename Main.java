/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: Main.java
 * Purpose: This is the entry point of the program. 
 * I utilize the FloydsClass in order to demonstrate 
 * computing optimal post-to-post costs on a river graph using 
 **************************************************************/


public class Main {

    
    /*********************************************************
     * Function main; run this function to execute the river
     * routing program. Essentially turning a graph class 
     * into a class appropriate for this problem.
     * 
     * @param args optional command-line arguments; if args[0]
     *             is present, it is used as the input filename
     *********************************************************/
    public static void main(String[] args) {
        String filename = "inputA"; // class file 
        if (args.length != 0)
            // in the case where the user inputs just the filename
            filename = args[0];


        // DijkstrasClass riverInstance = new DijkstrasClass(filename);
        FloydsClass riverInstance = new FloydsClass(filename);
        riverInstance.displayResults();
        
        System.out.println("\n-------------------------------------------------");
    }
}