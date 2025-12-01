/**************************************************************
 * Carlos Vargas
 * Cal Poly Pomona -- CS 3310
 * Fall 2025
 * Programming Assignment 3
 * ------------------------------------------------------------
 * File: DijkstrasClass.java
 * Purpose: TODO: _____________________________
 **************************************************************/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;



public class DijkstrasClass {
    private int[][] graph2DArray; // the 2d array to hold the distances between each node 
    private String filename;
    private int numNodes;
    private List<List<Edge>> graph = new ArrayList<>();
    private List<List<int[]>> distAndParents = new ArrayList<>();




    public DijkstrasClass(String inputFilename) { 
        setFilename(inputFilename);
        readFile(); // initialize information needed 
        // printGraph();
        applyDijkstras();
        // printDistAndParents();
    }
    



    private void applyDijkstras() { 
        // for each row (source node), use the Dijkstras Algorithm to find the optimal cost to the next node 
        for (int srcNode = 0; srcNode < numNodes-1; srcNode++) { 
            List<int[]> distAndParentsOneRow = dijkstrasAlgorithm(srcNode);
            distAndParents.add(distAndParentsOneRow);
        }
    }

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





    private void readFile() {
        int row = 0;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (row == 0) { 
                    // get the number of nodes (posts) in this graph (river)
                    // initialize a 2D array of distances 
                    setNumNodes(Integer.parseInt(line)); 
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





    public void printOptimalRouteInfo(int u, int v) { 
        int[] parents = distAndParents.get(u).get(1); // get the list of parents TO node u
        int parent = v;

        int i = 0;
        Deque<Integer> stack = new ArrayDeque<>();

        for (; i < parents.length; i++) {
            stack.push(parent);    
            if (parents[parent] != -1) { 
                parent = parents[parent];
            } else {
                i = parents.length;
            }
        }

        while (!stack.isEmpty()) { 
            System.out.print(stack.pop());
            if (!stack.isEmpty())
                System.out.print(" -> ");
        }
        System.out.println();
    }



    //***************************************************************************/
    // UTILITIES 
    //***************************************************************************/
    public void printGraph() { 
        for (int src = 0; src < numNodes; src++) {
            System.out.println("The list for source " + Integer.toString(src) + ": ");
            for (Edge e : graph.get(src)) {
                System.out.println("to: " + e.to + ", weight: " + e.weight);
            }
        }
    }


    public void printOptimalGraph() { 
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



    public void printIntArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }


    
    public void printBoolArray(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }



    public void printDistAndParents() {
        System.out.println("----------------------------");

        for (int i = 0; i < distAndParents.size(); i++) {
            System.out.println("Source node " + i + ":");
            List<int[]> innerList = distAndParents.get(i);
            for (int j = 0; j < innerList.size(); j++) {
                int[] arr = innerList.get(j);
                
                if (j == 0)
                    System.out.println("\tOptimal dist:   " + Arrays.toString(arr));
                else
                    System.out.println("\tParents source: " + Arrays.toString(arr));
            }
            System.out.println();
        }

        System.out.println("----------------------------");
    }





    //***************************************************************************/
    // SETTERS 
    //***************************************************************************/
    private void setFilename(String inputFilename) { 
        filename = inputFilename;
    }

    private void setNumNodes(int inputNum) { 
        numNodes = inputNum;
    }
}