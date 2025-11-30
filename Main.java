/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: Main.java
 * Purpose: This is the entry point of the program. 
 * We utilize the _______________ class in order to demonstrate 
 * _________________________________.
 **************************************************************/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;



public class Main {
    /*********************************************************
     * Function: main; run this function in order to see the 
     * ________________________________ in action. 
     * @param args
     *********************************************************/
    public static void main(String[] args) {

        
        String filename = "inputA"; // class file 
        if (args.length != 0)
            filename = args[0];
        
        DijkstrasClass dijClass = new DijkstrasClass(filename);

        System.out.println("\n--------------------------------------------------------------------------");
    }
}