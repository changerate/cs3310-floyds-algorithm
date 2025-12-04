/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: FloydsClass.java
 * TODO: FILL OUT THE COMMENTS TO FIT FLOYDS
 * Purpose: this class loads a weighted directed 
 * graph from an input file, constructs a 2D distance matrix, 
 * and applies Floyd's algorithm to compute the optimal 
 * shortest-path matrix.
 **************************************************************/


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;




public class FloydsClass {
    private String filename; // filename of the original 2D array graph file
    private int numNodes; // total number of nodes in the original graph
    private int[][] graphMatrix; // the 2d array representing the actual graph
    private int[][] shortestPaths; // the 2d array representing the optimal paths 
    private int[][] parentsMatrix; // the 2d array representing the parents for path reconstruction




    public FloydsClass(String inputFilename) { 
        System.out.println(); //TODO: DELETE

        setFilename(inputFilename);
        readFile(); // initialize information needed 

        // System.out.println("The initialized parent matrix");
        // printMatrix(parentsMatrix);

        floydsAlgorithm();
        
        System.out.println("The parent matrix after floyd's");
        printMatrix(parentsMatrix);
        // System.out.println("The graphMatrix after floyd's");
        // printMatrix(graphMatrix);
        System.out.println("The shortestPaths after floyd's");
        printMatrix(shortestPaths);

        System.out.println("The path");
        for (int i = 0; i < parentsMatrix.length; i++) { 
            for (int j = 0; j < parentsMatrix.length; j++) { 
                displayPath(i, j);
            }
        }
        
    }



    

    private void floydsAlgorithm() {
        int[][] shortestPaths = deepCopy(graphMatrix);
        
        for (int k = 0; k < graphMatrix.length; k++) { 
            for (int i = 0; i < graphMatrix.length; i++) { 
                for (int j = 0; j < graphMatrix.length; j++) {
                    int ijDist = shortestPaths[i][j];
                    int ikDist = shortestPaths[i][k];
                    int kjDist = shortestPaths[k][j];

                    if (ikDist >= 0 || kjDist >= 0) { 

                        if (ijDist < ikDist + kjDist) {
                            shortestPaths[i][j] = ijDist;
                            parentsMatrix[i][j] = j;
                        } else { 
                            shortestPaths[i][j] = ikDist + kjDist;

                            parentsMatrix[i][j] = k;
                            parentsMatrix[k][j] = j;
                        }
                    }
                }
            }
        }
        
        setShortestPaths(shortestPaths);
    }




    /*********************************************************
     * Function readFile; reads the graph data from the input 
     * file specified by filename. The first line contains the 
     * number of nodes, and each subsequent line contains the 
     * upper-triangular portion of the adjacency matrix. The 
     * method initializes both a 2D distance matrix and an 
     * adjacency list representation of the graph.
     *
     * File format:
     *   Line 1: numNodes
     *   Line 2+: distances from node i to all nodes j ≥ i
     *
     * @throws IOException if the input file cannot be read
     *********************************************************/
    private void readFile() {
        int row = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (row == 0) { 
                    // get the number of nodes (posts) in this graph (river)
                    setNumNodes(Integer.parseInt(line.trim())); 
                    // initialize a 2D array of distances 
                    graphMatrix = new int[numNodes][numNodes]; 
                    parentsMatrix = new int[numNodes][numNodes]; 
                    // now initialize all the values to negative value:
                    for (int i = 0; i < graphMatrix.length; i++) {
                        Arrays.fill(graphMatrix[i], -1);   
                    }
                    for (int i = 0; i < parentsMatrix.length; i++) {
                        Arrays.fill(parentsMatrix[i], -1);   
                    }
                } else { 
                    // collect the distances of the nodes in the adjacency list graph
                    String[] dists = line.trim().split("\\s+");   // split on one or more spaces
                    int distIndex = 0;
                    for (int i = row; i < numNodes; i++) {
                        // fill out the distance matrix with either empty distances, or the dists from the file
                        graphMatrix[row - 1][i] = Integer.parseInt(dists[distIndex]);
                        distIndex += 1;
                    }
                }
                row += 1; 
            }
        } catch (IOException e) {
            System.out.println("\nError reading file: " + e);
            // return;
        }
    }



    private void displayPath(int u, int v) { 
        Deque<Integer> recontructedPath = new ArrayDeque<>();
        recontructedPath.add(u);
        int k = parentsMatrix[u][v];
        
        while (k != v) {
            if (k == -1) { 
                System.out.println("No path exists from " + u + " to " + v);
                return;
            }
            recontructedPath.add(k);
            k = parentsMatrix[k][v];
        }
        recontructedPath.add(v);

        while (!recontructedPath.isEmpty()) { 
            System.out.print(recontructedPath.pop());
            if (!recontructedPath.isEmpty())
                System.out.print(" -> ");
        }
        System.out.println();
    }







    //=====================================================================
    //=====================================================================
    // UTILITIES 
    //=====================================================================
    //=====================================================================


    /*********************************************************
     * Function printIntArray; prints the contents of an integer 
     * array in comma-separated format.
     *
     * @param arr the array to print
     *********************************************************/
    public void printIntArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }


    public void printShortestPaths() {
        // First find the widest number (in characters)
        int maxWidth = 0;
        for (int[] row : shortestPaths) {
            for (int val : row) {
                if (val != -1) {                       // ignore "no edge" entries
                    int width = String.valueOf(val).length();
                    maxWidth = Math.max(maxWidth, width);
                }
            }
        }

        // Print each row using padding
        for (int[] row : shortestPaths) {
            for (int val : row) {
                if (val == -1) {
                    // print blank representing no path
                    System.out.printf("%" + maxWidth + "s ", " ");
                } else {
                    System.out.printf("%" + maxWidth + "d ", val);
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    public void printMatrix(int[][] arr) {
        // First find the widest number (in characters)
        int maxWidth = 0;
        for (int[] row : arr) {
            for (int val : row) {
                if (val != -1) {                       // ignore "no edge" entries
                    int width = String.valueOf(val).length();
                    maxWidth = Math.max(maxWidth, width);
                }
            }
        }

        // Ensure maxWidth is at least 1 (important when all entries are -1)
        if (maxWidth == 0) {
            maxWidth = 1;
        }

        // Print the vertex numbers
        System.out.print("  ");
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("%" + maxWidth + "d ", i);
        }
        System.out.println();
        
        // Print each row using padding
        int rowIndex = 0;
        for (int[] row : arr) {
            System.out.printf("%" + 1 + "d ", rowIndex); // prints the row numbers
            for (int val : row) {
                if (val == -1) {
                    // print blank representing no path
                    System.out.printf("%" + maxWidth + "s ", " ");
                } else {
                    System.out.printf("%" + maxWidth + "d ", val);
                }
            }
            System.out.println();
            rowIndex++;
        }
        System.out.println();
    }



    public static int[][] deepCopy(int[][] original) {
        if (original == null) 
            return null;

        int[][] copy = new int[original.length][];

        for (int i = 0; i < original.length; i++) {
            // Copy each row (creates a new array, not a reference!)
            copy[i] = original[i].clone();
        }
        return copy;
    }


    /*********************************************************
     * Function printBoolArray; prints the contents of a boolean 
     * array in comma-separated format.
     *
     * @param arr the boolean array to print
     *********************************************************/
    public void printBoolArray(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }


    //=====================================================================
    //=====================================================================
    // SETTERS 
    //=====================================================================
    //=====================================================================
    /*********************************************************
     * Function setFilename; stores the input filename used to
     * read the graph data.
     *
     * @param inputFilename the name of the file to load
     *********************************************************/
    private void setFilename(String inputFilename) { 
        filename = inputFilename;
    }

    /*********************************************************
     * Function setNumNodes; records the number of nodes in the 
     * graph as extracted from the input file.
     *
     * @param inputNum number of nodes in the graph
     *********************************************************/
    private void setNumNodes(int inputNum) { 
        numNodes = inputNum;
    }

    private void setShortestPaths(int[][] inputShortestPaths) { 
        shortestPaths = inputShortestPaths;
    }
}