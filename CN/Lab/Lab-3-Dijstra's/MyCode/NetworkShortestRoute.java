import java.util.Arrays;

public class NetworkShortestRoute {

    // Number of routers in the network
    static final int TOTAL_ROUTERS = 7;
    // Representation of infinity (no direct connection)
    static final int NO_PATH = Integer.MAX_VALUE;

    // Router labels for display
    static String[] routerNames = {"X1", "X2", "X3", "X4", "X5", "X6", "X7"};

    public static void main(String[] args) {

        // Adjacency matrix of network costs
        int[][] linkCost = {
            {0, 2, 5, 0, 0, 0, 0},
            {2, 0, 1, 2, 0, 0, 0},
            {5, 1, 0, 3, 1, 4, 0},
            {0, 2, 3, 0, 2, 0, 3},
            {0, 0, 1, 2, 0, 0, 5},
            {0, 0, 4, 0, 0, 0, 2},
            {0, 0, 0, 3, 5, 2, 0}
        };

        computeRoutes(linkCost, 0); // Start from router X1
    }

    static void computeRoutes(int[][] graph, int sourceIndex) {
        int[] minDistance = new int[TOTAL_ROUTERS];       // Holds shortest distance from source
        boolean[] processed = new boolean[TOTAL_ROUTERS]; // Tracks processed routers
        String[] journey = new String[TOTAL_ROUTERS];     // Tracks path taken

        Arrays.fill(minDistance, NO_PATH);
        Arrays.fill(processed, false);

        for (int r = 0; r < TOTAL_ROUTERS; r++) {
            journey[r] = routerNames[sourceIndex];
        }

        minDistance[sourceIndex] = 0;

        // Step-by-step progression
        for (int phase = 1; phase <= TOTAL_ROUTERS - 1; phase++) {
            int current = chooseNearest(minDistance, processed);
            processed[current] = true;

            // Custom progress output
            System.out.println("=== Phase " + phase + " ===");
            System.out.println("Router chosen: " + routerNames[current]);
            System.out.println("Current distances: " + Arrays.toString(minDistance));
            System.out.println("Processed flags:  " + Arrays.toString(processed));

            for (int neighbor = 0; neighbor < TOTAL_ROUTERS; neighbor++) {
                if (!processed[neighbor] && graph[current][neighbor] != 0 &&
                        minDistance[current] != NO_PATH) {
                    int tentative = minDistance[current] + graph[current][neighbor];
                    if (tentative < minDistance[neighbor]) {
                        minDistance[neighbor] = tentative;
                        journey[neighbor] = journey[current] + " -> " + routerNames[neighbor];
                    }
                }
            }

            System.out.println("Updated paths: " + Arrays.toString(journey));
            System.out.println();
        }

        // Final shortest routes display
        System.out.println("From\tTo\tTotalCost\tRouteTaken");
        for (int dest = 0; dest < TOTAL_ROUTERS; dest++) {
            if (dest != sourceIndex) {
                System.out.println(routerNames[sourceIndex] + "\t" + routerNames[dest] + "\t" +
                                   minDistance[dest] + "\t\t" + journey[dest]);
            }
        }
    }

    // Picks the unprocessed router with smallest distance
    static int chooseNearest(int[] distances, boolean[] doneSet) {
        int smallest = NO_PATH;
        int pick = -1;
        for (int idx = 0; idx < TOTAL_ROUTERS; idx++) {
            if (!doneSet[idx] && distances[idx] < smallest) {
                smallest = distances[idx];
                pick = idx;
            }
        }
        return pick;
    }
}
