import java.util.*;
import java.util.*;

public class DijkstraRouting 
{
    static final int INF = Integer.MAX_VALUE;  // A constant to represent infinity
    static final int V = 7;                    // Number of vertices (nodes) in the graph
    static String[] vertexNames = {"A", "B", "C", "D", "E", "F", "G"}; // Names for display

    // Function to implement Dijkstra's algorithm
    public static void dijkstra(int[][] graph, int source) 
    {
        int[] dist = new int[V];             // Array to hold the shortest distance from source
        boolean[] visited = new boolean[V];  // Track visited nodes
        String[] path = new String[V];       // Track path taken to reach each node

        // Initialize distances to infinity and paths with source node
        Arrays.fill(dist, INF);
        Arrays.fill(visited, false);
        for (int i = 0; i < V; i++) 
        {
            path[i] = vertexNames[source];  // Start path from source
        }
        System.out.println("Initial Values");
        System.out.println("Visited: " + Arrays.toString(visited));
        System.out.println("Distances: " + Arrays.toString(dist));
        System.out.println("Path: " + Arrays.toString(path) + "\n");

        dist[source] = 0;  // Distance to itself is zero

        // Loop through all vertices to find shortest path
        for (int count = 0; count < V - 1; count++) 
        {
            // Pick the unvisited node with the smallest distance
            int u = minDistance(dist, visited);
            visited[u] = true;

            // Print current state (iteration-wise trace)
            System.out.println("Step " + (count + 1) + ":");
            System.out.println("Visited: " + Arrays.toString(visited));
            System.out.println("Distances: " + Arrays.toString(dist));

            // Update distance and path for neighboring vertices
            for (int v = 0; v < V; v++) 
            {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != INF &&
                        dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v]; // Update distance
                    path[v] = path[u] + " -> " + vertexNames[v]; // Update path
                }
            }
            System.out.println("Update distance and path for neighboring vertices");
            System.out.println("Distances: " + Arrays.toString(dist));
            System.out.println("Path: " + Arrays.toString(path) + "\n");
        }

        // Final output of shortest paths from source
        System.out.println("\nVertex\tDistance\tPath");
        for (int i = 0; i < V; i++) 
        {
            if (i != source) 
            {
                System.out.println(vertexNames[source] + " -> " + vertexNames[i] + "\t" +
                        dist[i] + "\t\t" + path[i]);
            }
        }
    }

    // Helper function to find the unvisited node with the smallest distance
    public static int minDistance(int[] dist, boolean[] visited) 
    {
        int min = INF, minIndex = -1;
        for (int v = 0; v < V; v++) 
        {
            if (!visited[v] && dist[v] <= min) 
            {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Main method to execute the program
    public static void main(String[] args) 
    {
        // Graph represented as adjacency matrix (7x7)
        int[][] graph = 
        {
            // A  B  C  D  E  F  G
            { 0, 2, 5, 0, 0, 0, 0 }, // A
            { 2, 0, 1, 2, 0, 0, 0 }, // B
            { 5, 1, 0, 3, 1, 4, 0 }, // C
            { 0, 2, 3, 0, 2, 0, 3 }, // D
            { 0, 0, 1, 2, 0, 0, 5 }, // E
            { 0, 0, 4, 0, 0, 0, 2 }, // F
            { 0, 0, 0, 3, 5, 2, 0 }  // G
        };

        dijkstra(graph, 0); // Call Dijkstra from source node A (index 0)
    }
}
