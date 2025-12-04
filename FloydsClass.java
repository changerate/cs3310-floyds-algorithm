/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: FloydsClass.java
 * Purpose: This class holds the logic needed to find the optimal 
 * paths from each node to every other node.
 **************************************************************/


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;




public class FloydsClass {
    private String filename; // filename of the original 2D array graph file
    private int numNodes; // total number of nodes in the original graph
    private int[][] graphMatrix; // the 2d array representing the actual graph
    private int[][] shortestPaths; // the 2d array representing the optimal paths 
    private int[][] parentsMatrix; // the 2d array representing the parents for path reconstruction



    /*********************************************************
     * Constructor FloydsClass; initializes the class by
     * loading the graph from the given filename and running
     * Floyd’s shortest–path algorithm on it.
     *
     * @param inputFilename the name of the file containing
     *        the graph’s adjacency information
     *********************************************************/
    public FloydsClass(String inputFilename) { 
        setFilename(inputFilename);
        readFile(); // initialize information needed 
        floydsAlgorithm();
    }





    /*********************************************************
     * Function floydsAlgorithm; executes the Floyd
     * algorithm to compute the shortest paths between all 
     * pairs of nodes in the graph.
     *
     * This method:
     *   - Initializes the parents matrix for path reconstruction
     *   - Iteratively improves path costs using intermediate nodes
     *
     * Results are stored in shortestPaths and parentsMatrix.
     *********************************************************/
    private void floydsAlgorithm() {
        shortestPaths = deepCopy(graphMatrix);
        int n = shortestPaths.length;
        
        // Initialize parentsMatrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (shortestPaths[i][j] != -1 && i != j) {
                    // There is a direct edge i -> j, so the next node from i to j is j
                    parentsMatrix[i][j] = j;
                } else {
                    // No known path yet
                    parentsMatrix[i][j] = -1;
                }
            }
        }

        floydRecursive(0, 0, 0);
    }



    /*********************************************************
     * Function floydRecursive; recursive helper 
     * @param k current in between index
     * @param i current source node index
     * @param j current destination node index
     *********************************************************/
    private void floydRecursive(int k, int i, int j) {
        // the base case is finished all intermediate verteces
        if (k == numNodes) return;

        // move to next i (row)
        if (j == numNodes) {
            floydRecursive(k, i + 1, 0);
            return;
        }

        // move to next k
        if (i == numNodes) {
            floydRecursive(k + 1, 0, 0);
            return;
        }

        // Skip if i can't reach k or k can't reach j
        if (shortestPaths[i][k] != -1 && shortestPaths[k][j] != -1) {
            int ikjDist = shortestPaths[i][k] + shortestPaths[k][j];
            
            if (shortestPaths[i][j] == -1 || ikjDist < shortestPaths[i][j]) {
                // take this path if there is even an intermediate path that exists,
                // but the original path does not
                shortestPaths[i][j] = ikjDist;
                parentsMatrix[i][j] = parentsMatrix[i][k];
            }
        }

        // move to next j
        floydRecursive(k, i, j + 1);
    }



        
    /*********************************************************
     * Function readFile; loads the graph data from the input
     * file specified by 'filename'. The first line contains the
     * number of nodes, and subsequent lines contain the upper
     * triangular distance entries of the adjacency matrix.
     * 
     * WARNING: 
     * This method only works on graphs that follow this structure: 
        10 50  5 12 27 
           23 16 38 44
              30 15 33
                 33 43
                    12
     * This method:
     *   - Initializes graphMatrix and parentsMatrix
     *   - Fills matrix entries with either a distance or -1
     *   - Handles file-reading exceptions gracefully
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
                    // collect the distances of the nodes
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



    //=====================================================================
    //=====================================================================
    // UTILITIES 
    //=====================================================================
    //=====================================================================

    /*********************************************************
     * Function displayResults; prints the completed shortest
     * path distance matrix and demonstrates a sample reconstructed
     * path between nodes 1 and 3.
     *********************************************************/
    public void displayResults() { 
        System.out.println("\nThe optimal cost matrix is as follows:");
        printMatrix(shortestPaths);
        
        displayAllPaths();
    }
    
    
    
    
    /*********************************************************
     * Function displayAllPaths; This function simply displays 
     * and formats all the paths in matrix.
     *********************************************************/
    public void displayAllPaths() { 
        System.out.println("\nThese are all the paths in this matrix:");

        for (int i = 0; i < numNodes; i++) { 
            for (int j = 0; j < numNodes; j++) { 
                if (graphMatrix[i][j] < 0) continue;
                
                displayPath(i, j);
            }
        }
    }



    /*********************************************************
     * Function displayPath; reconstructs and prints the path
     * from node u to node v using the parentsMatrix produced by
     * Floyd’s algorithm.
     *
     * If no path exists, a message is displayed.
     *
     * @param u the starting node
     * @param v the target node
     *********************************************************/
    public void displayPath(int u, int v) { 
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




    /*********************************************************
     * Function printMatrix; prints a formatted 2D matrix of
     * distances. The method auto-aligns columns based on the
     * widest numeric entry and prints blanks for -1 entries.
     *
     * @param arr the matrix to display
     *********************************************************/
    private void printMatrix(int[][] arr) {
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





    /*********************************************************
     * Function deepCopy; produces a deep copy of the 2D array
     * passed as input. Each row is cloned individually.
     *
     * @param original the matrix to be copied
     * @return a new 2D array with identical values
     *********************************************************/
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
}