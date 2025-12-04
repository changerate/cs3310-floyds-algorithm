/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: Main.java
 * Purpose: This is the entry point of the program. 
 * I utilize the DijkstrasClass class in order to demonstrate 
 * computing optimal post-to-post costs on a river graph using 
 * Dijkstra's shortest-path algorithm and to allow interactive 
 * testing of routes between posts. 
 **************************************************************/


import java.util.Scanner;



public class Main {
    /*********************************************************
     * Function main; run this function to execute the river
     * routing program. The method reads an optional input
     * filename from the command line, constructs a 
     * DijkstrasClass instance using that file, prints the 
     * optimal cost matrix, and then enters an interactive 
     * loop where the user can test post-to-post routes.
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
        // riverInstance.printShortestPaths();
        
        // Now let the user test routes between posts 
        // testRoutes(riverInstance);
        
        System.out.println("\n-------------------------------------------------");
    }


    /*********************************************************
     * Function testRoutes; repeatedly prompts the user to 
     * test optimal routes between pairs of posts. When the 
     * user chooses to continue, they enter a pair of post 
     * indices (u and v), and this function calls 
     * printOptimalRouteInfo on the provided DijkstrasClass 
     * instance to display the optimal distance and path.
     * Input is validated, and invalid entries are reported.
     * 
     * @param riverInstance a DijkstrasClass object that has 
     *        already computed all shortest-path information
     *********************************************************/
    // static void testRoutes(FloydsClass riverInstance) { 
    //     int u = -1, v = -1;
    //     boolean contin = true; 
    //     Scanner sc = new Scanner(System.in);

    //     while (contin) { 
    //         System.out.print("\nTest another post-to-post cost (y/n)? ");
    //         String line = sc.nextLine(); 

    //         if ("n".equals(line.toLowerCase())) {
    //             contin = false;
    //         }

    //         else if ("y".equals(line.toLowerCase())) {
    //             try { 
    //                 System.out.print("Choose the from and to posts, seperated by a space: ");
    //                 line = sc.nextLine(); 
    //                 String[] uv = line.trim().split("\\s+");
    //                 u = Integer.parseInt(uv[0]);
    //                 v = Integer.parseInt(uv[1]);
                    
    //                 // riverInstance.printOptimalRouteInfo(u, v);
    //             } catch (Exception e) { 
    //                 System.out.println("Invalid post input: " + e);
    //             }
    //         }
    //     }
    // }
}