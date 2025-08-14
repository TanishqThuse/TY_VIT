import java.util.Arrays;

public class ShortestPathOSPF {

    static final int NODES = 7;
    static final int BIG = Integer.MAX_VALUE;

    static String[] routerLabels = {"R1", "R2", "R3", "R4", "R5", "R6", "R7"};

    public static void main(String[] args) {

        int[][] costMatrix = {
            {0, 2, 5, 0, 0, 0, 0},
            {2, 0, 1, 2, 0, 0, 0},
            {5, 1, 0, 3, 1, 4, 0},
            {0, 2, 3, 0, 2, 0, 3},
            {0, 0, 1, 2, 0, 0, 5},
            {0, 0, 4, 0, 0, 0, 2},
            {0, 0, 0, 3, 5, 2, 0}
        };

        findPaths(costMatrix, 0);
    }

    static void findPaths(int[][] network, int start) {
        int[] minCost = new int[NODES];
        boolean[] done = new boolean[NODES];
        String[] route = new String[NODES];

        Arrays.fill(minCost, BIG);
        Arrays.fill(done, false);

        for (int i = 0; i < NODES; i++) {
            route[i] = routerLabels[start];
        }

        minCost[start] = 0;

        for (int step = 0; step < NODES - 1; step++) {
            int nearest = pickMin(minCost, done);
            done[nearest] = true;

            for (int j = 0; j < NODES; j++) {
                if (!done[j] && network[nearest][j] != 0 && minCost[nearest] != BIG) {
                    int newCost = minCost[nearest] + network[nearest][j];
                    if (newCost < minCost[j]) {
                        minCost[j] = newCost;
                        route[j] = route[nearest] + " -> " + routerLabels[j];
                    }
                }
            }
        }

        System.out.println("From\tTo\tCost\tPath");
        for (int k = 0; k < NODES; k++) {
            if (k != start) {
                System.out.println(routerLabels[start] + "\t" + routerLabels[k] + "\t" +
                                   minCost[k] + "\t" + route[k]);
            }
        }
    }

    static int pickMin(int[] cost, boolean[] visited) {
        int minValue = BIG;
        int minIndex = -1;
        for (int i = 0; i < NODES; i++) {
            if (!visited[i] && cost[i] < minValue) {
                minValue = cost[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
