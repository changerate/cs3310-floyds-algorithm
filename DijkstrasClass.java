/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: DijkstrasClass.java
 * Purpose: this class loads a weighted directed 
 * graph from an input file, constructs an adjacency list and 
 * 2D distance matrix, and applies Dijkstra’s algorithm from 
 * each source node to compute optimal shortest-path costs.
 *
 * The class stores both the optimal distances and parent 
 * pointers for each run of Dijkstra’s algorithm, allowing 
 * reconstruction and printing of optimal routes between 
 * arbitrary node pairs.
 **************************************************************/




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;




public class DijkstrasClass {
    private String filename; // holds the filename where the original 2D array graph comes from
    private int numNodes; // holds the total number of nodes in the original 2D array graph
    private int[][] graph2DArray; // the 2d array to hold the distances between each node 
    private List<List<Edge>> graph = new ArrayList<>(); // holds the adjacency list version of the graph
    private List<List<int[]>> distAndParents = new ArrayList<>(); // holds the distances and parent arrays for each source to v pairing




    public DijkstrasClass(String inputFilename) { 
        setFilename(inputFilename);
        readFile(); // initialize information needed 
        // printGraph();
        applyDijkstras();
    }




    /*********************************************************
     * Function applyDijkstras; applies Dijkstra’s algorithm 
     * once for each node in the graph, treating that node as 
     * the source. For every source node, the resulting shortest 
     * distances and parent pointer arrays are stored in the 
     * distAndParents list.
     *********************************************************/
    private void applyDijkstras() { 
        // for each row (source node), use the Dijkstras Algorithm to find the optimal cost to the next node 
        for (int srcNode = 0; srcNode < numNodes-1; srcNode++) { 
            List<int[]> distAndParentsOneRow = dijkstrasAlgorithm(srcNode);
            distAndParents.add(distAndParentsOneRow);
        }
    }


    
    
    /*********************************************************
     * Function dijkstrasAlgorithm; runs Dijkstra’s shortest-
     * path algorithm from a given source node using an adjacency
     * list representation of the graph. It returns a list 
     * containing two arrays: the optimal distance array and 
     * the parent pointer array, where parent[v] represents the
     * previous node on the shortest path to v.
     *
     * @param srcNode the starting node from which shortest paths
     *        are computed
     * @return a List<int[]> containing:
     *         index 0 → dist[]   optimal distances
     *         index 1 → parent[] predecessor nodes on paths
     *********************************************************/
    private List<int[]> dijkstrasAlgorithm(int srcNode) {
        Node src = new Node(srcNode, 0);        // make a new Node object with the srcNode integer
        int[] dist = new int[numNodes];             // shortest distances found so far
        boolean[] visited = new boolean[numNodes];  // processed or not
        int[] parent = new int[numNodes];           // to reconstruct path (optional)
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // initialization
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[src.id] = 0;
        pq.add(src);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int u = cur.id;

            // Skip if we already processed this node
            if (visited[u]) continue;
            visited[u] = true;

            // Get each neighbor of the current node
            for (Edge e : graph.get(u)) {
                int v = e.to;
                int w = e.weight;

                // If going through u gives a shorter path to v
                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;  // for path reconstruction
                    pq.add(new Node(v, dist[v]));
                }
            }
        }

        List<int[]> ret = new ArrayList<>();
        ret.add(dist);
        ret.add(parent);

        return ret;
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
                    // initialize a 2D array of distances 
                    setNumNodes(Integer.parseInt(line.trim())); 
                    graph2DArray = new int[numNodes][numNodes]; 
                    for (int i = 0; i < graph2DArray.length; i++) {
                        // now initialize all the values to -1:
                        Arrays.fill(graph2DArray[i], -1);   
                    }
                    // initialize the list graph 
                    for (int i = 0; i < numNodes; i++)
                        graph.add(new ArrayList<>());
                } else { 
                    // collect the distances of the nodes in the adjacency list graph
                    String[] dists = line.trim().split("\\s+");   // split on one or more spaces
                    int distIndex = 0;
                    for (int i = row; i < numNodes; i++) {
                        // fill out the distance matrix with either empty distances, or the dists from the file
                        graph.get(row-1).add(new Edge(i, Integer.parseInt(dists[distIndex])));
                        graph2DArray[row-1][i] = Integer.parseInt(dists[distIndex]);
                        distIndex += 1;
                    }
                }
                row += 1; 
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e);
            // return;
        }
    }




    /*********************************************************
     * Function printOptimalRouteInfo; prints both the optimal 
     * distance as an integer and the full shortest path from 
     * source node u to destination node v. This method 
     * reconstructs the path using the stored parent pointers
     * and prints the path in order from source to destination.
     *
     * @param u the source node
     * @param v the destination node
     *********************************************************/
    public void printOptimalRouteInfo(int u, int v) { 
        if (u > numNodes || v > numNodes) {
            System.out.println("u or v is not a node.");
            return;
        }

        int optimalDistance = distAndParents.get(u).get(0)[v];
        int[] parents = distAndParents.get(u).get(1); // get the list of parents TO node u
        int parent = v;

        Deque<Integer> stack = new ArrayDeque<>();

        int i = 0;
        for (; i < parents.length; i++) {
            stack.push(parent);
            if (parents[parent] != -1) { 
                parent = parents[parent];
            } else {
                i = parents.length;
            }
        }

        System.out.println("Optimal distance: " + optimalDistance);
        System.out.print("Optimal path:     ");

        while (!stack.isEmpty()) { 
            System.out.print(stack.pop());
            if (!stack.isEmpty())
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
     * Function printGraph; prints the adjacency list 
     * representation of the graph, showing each source node and 
     * its outgoing edges with corresponding weights.
     *********************************************************/
    public void printGraph() { 
        for (int src = 0; src < numNodes; src++) {
            System.out.println("The list for source " + Integer.toString(src) + ": ");
            for (Edge e : graph.get(src)) {
                System.out.println("to: " + e.to + ", weight: " + e.weight);
            }
        }
    }


    /*********************************************************
     * Function printOptimalGraph; prints a formatted matrix of 
     * optimal distances produced by Dijkstra’s algorithm. Only 
     * the upper-triangular portion is displayed (excluding 0 
     * and infinity entries) to reflect the structure of the 
     * input graph.
     *********************************************************/
    public void printOptimalGraph() { 
        System.out.println("\nOptimal Cost Matrix");

        System.out.print(" ");

        for (int i = 0; i < numNodes; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();

        for (int srcNode = 0; srcNode < distAndParents.size(); srcNode++) {
            int[] optimalArray = distAndParents.get(srcNode).get(0);

            System.out.print(srcNode);
            for (int i = 0; i < srcNode+1; i++)
                System.out.print("    ");

            for (int weight: optimalArray) {
                if (weight == Integer.MAX_VALUE || weight == 0) { continue; }
                if (weight >= 100) { 
                    System.out.print(weight + " ");
                } else if (weight < 100 && weight >= 10) { 
                    System.out.print(weight + "  ");
                } else if (weight < 10) { 
                    System.out.print(weight + "   ");
                }
            }
            System.out.println();
        }
    }


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
}