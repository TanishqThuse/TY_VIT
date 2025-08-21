
import java.util.*;

public class AirlineRouteOptimization {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of cities: ");
        int n = sc.nextInt();
        System.out.print("Enter number of routes: ");
        int m = sc.nextInt();
        List<Edge> edges = new ArrayList<>();
        // Build adjacency list for Dijkstra
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        System.out.println("Enter routes (src dest cost):");
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), c = sc.nextInt();
            edges.add(new Edge(u, v, c));
            adj.get(u).add(new Edge(u, v, c));
            adj.get(v).add(new Edge(v, u, c)); // undirected
        }

        System.out.print("Enter source city: ");
        int srcCity = sc.nextInt();
        System.out.print("Enter destination city: ");
        int destCity = sc.nextInt();

        // Dijkstra's algorithm
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[srcCity] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));
        pq.add(new Edge(-1, srcCity, 0));
        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            int u = curr.dest;
            if (curr.cost > dist[u]) continue;
            for (Edge e : adj.get(u)) {
                int v = e.dest, cost = e.cost;
                if (dist[u] + cost < dist[v]) {
                    dist[v] = dist[u] + cost;
                    prev[v] = u;
                    pq.add(new Edge(u, v, dist[v]));
                }
            }
        }
        if (dist[destCity] == Integer.MAX_VALUE) {
            System.out.println("No path exists from city " + srcCity + " to city " + destCity);
        } else {
            System.out.println("Minimum cost from city " + srcCity + " to city " + destCity + ": " + dist[destCity]);
            // Reconstruct path
            List<Integer> path = new ArrayList<>();
            for (int at = destCity; at != -1; at = prev[at]) path.add(at);
            Collections.reverse(path);
            System.out.print("Path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i != path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }
}

class Edge implements Comparable<Edge> {
    int src, dest, cost;
    Edge(int s, int d, int c) {
        src = s; dest = d; cost = c;
    }
    public int compareTo(Edge other) {
        return this.cost - other.cost;
    }
}

class UnionFind {
    int[] parent, rank;
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    boolean union(int x, int y) {
        int xr = find(x), yr = find(y);
        if (xr == yr) return false;
        if (rank[xr] < rank[yr]) parent[xr] = yr;
        else if (rank[xr] > rank[yr]) parent[yr] = xr;
        else { parent[yr] = xr; rank[xr]++; }
        return true;
    }
}


/**Enter number of cities: 4
Enter number of routes: 5
Enter routes (src dest cost):
0 1 10
0 2 6
0 3 5
1 3 15
2 3 4 */