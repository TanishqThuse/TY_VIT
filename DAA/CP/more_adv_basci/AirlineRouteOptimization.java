import java.util.*;

public class AirlineRouteOptimization {
    static class Flight {
        String airline, flightNumber;
        int src, dest, cost, duration; // duration in minutes
        String depTime, arrTime;
        boolean isTwoWay;

        Flight(String airline, String flightNumber, int src, int dest, int cost, int duration, String depTime, String arrTime, boolean isTwoWay) {
            this.airline = airline;
            this.flightNumber = flightNumber;
            this.src = src;
            this.dest = dest;
            this.cost = cost;
            this.duration = duration;
            this.depTime = depTime;
            this.arrTime = arrTime;
            this.isTwoWay = isTwoWay;
        }
    }

    static class Node implements Comparable<Node> {
        int city, cost, duration;
        List<Flight> path;

        Node(int city, int cost, int duration, List<Flight> path) {
            this.city = city;
            this.cost = cost;
            this.duration = duration;
            this.path = path;
        }

        public int compareTo(Node o) {
            return Integer.compare(this.cost, o.cost); // Change to duration for time optimization
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of cities: ");
        int n = Integer.parseInt(sc.nextLine());
        String[] cityNames = new String[n];
        Map<String, Integer> cityIndex = new HashMap<>();
        System.out.println("Enter city names:");
        for (int i = 0; i < n; i++) {
            cityNames[i] = sc.nextLine();
            cityIndex.put(cityNames[i], i);
        }

        System.out.print("Enter number of flights: ");
        int m = Integer.parseInt(sc.nextLine());
        List<List<Flight>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        System.out.println("Enter flight details (airline flightNumber srcCity destCity cost duration depTime arrTime twoWay[Y/N]):");
        for (int i = 0; i < m; i++) {
            String[] parts = sc.nextLine().split(" ");
            String airline = parts[0], flightNumber = parts[1];
            int src = cityIndex.get(parts[2]);
            int dest = cityIndex.get(parts[3]);
            int cost = Integer.parseInt(parts[4]);
            int duration = Integer.parseInt(parts[5]);
            String depTime = parts[6], arrTime = parts[7];
            boolean isTwoWay = parts[8].equalsIgnoreCase("Y");
            Flight f = new Flight(airline, flightNumber, src, dest, cost, duration, depTime, arrTime, isTwoWay);
            adj.get(src).add(f);
            if (isTwoWay) {
                // Add reverse flight
                Flight rev = new Flight(airline, flightNumber, dest, src, cost, duration, arrTime, depTime, isTwoWay);
                adj.get(dest).add(rev);
            }
        }

        System.out.print("Enter source city: ");
        int srcCity = cityIndex.get(sc.nextLine());
        System.out.print("Enter destination city: ");
        int destCity = cityIndex.get(sc.nextLine());

        // Dijkstra's algorithm (by cost)
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[srcCity] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(srcCity, 0, 0, new ArrayList<>()));
        boolean[] visited = new boolean[n];
        Node result = null;

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (visited[curr.city]) continue;
            visited[curr.city] = true;
            if (curr.city == destCity) {
                result = curr;
                break;
            }
            for (Flight f : adj.get(curr.city)) {
                if (!visited[f.dest] && dist[curr.city] + f.cost < dist[f.dest]) {
                    dist[f.dest] = dist[curr.city] + f.cost;
                    List<Flight> newPath = new ArrayList<>(curr.path);
                    newPath.add(f);
                    pq.add(new Node(f.dest, dist[f.dest], curr.duration + f.duration, newPath));
                }
            }
        }

        if (result == null) {
            System.out.println("No path exists from " + cityNames[srcCity] + " to " + cityNames[destCity]);
        } else {
            System.out.println("Minimum cost from " + cityNames[srcCity] + " to " + cityNames[destCity] + ": " + result.cost);
            System.out.println("Total duration: " + result.duration + " minutes");
            System.out.println("Flight path:");
            for (Flight f : result.path) {
                System.out.println("  " + cityNames[f.src] + " -> " + cityNames[f.dest] +
                        " | " + f.airline + " " + f.flightNumber +
                        " | Cost: " + f.cost +
                        " | Duration: " + f.duration + " min" +
                        " | Dep: " + f.depTime + " Arr: " + f.arrTime);
            }
        }
    }
}


/**Ex-1 : Delhi to Kolkata
 * 
 * 4
Delhi
Mumbai
Chennai
Kolkata
5
AirIndia AI101 Delhi Mumbai 5000 120 08:00 10:00 Y
IndiGo 6E202 Mumbai Chennai 4000 150 11:00 13:30 N
SpiceJet SG303 Chennai Kolkata 3500 180 14:00 17:00 Y
AirAsia I505 Kolkata Delhi 3000 150 18:00 20:30 N
Vistara UK404 Delhi Chennai 6000 180 09:00 12:00 N
Delhi
Kolkata

Example 2 : Germany to Japan

5
Berlin
Frankfurt
Dubai
Tokyo
Osaka
6
Lufthansa LH123 Berlin Frankfurt 200 90 08:00 09:30 Y
Emirates EK204 Frankfurt Dubai 500 360 11:00 17:00 Y
ANA NH210 Dubai Tokyo 700 540 19:00 04:00 Y
JAL JL300 Tokyo Osaka 100 60 06:00 07:00 Y
Lufthansa LH456 Frankfurt Tokyo 900 720 10:00 22:00 N
ANA NH220 Dubai Osaka 800 600 20:00 06:00 N
Berlin
Osaka

Example 3 : Pune to New York

5
Pune
Mumbai
London
NewYork
Dubai
6
AirIndia AI850 Pune Mumbai 100 60 06:00 07:00 Y
BritishAirways BA198 Mumbai London 600 540 09:00 18:00 Y
Delta DL401 London NewYork 700 480 20:00 04:00 Y
Emirates EK501 Mumbai Dubai 400 180 08:00 11:00 Y
Emirates EK201 Dubai NewYork 900 840 13:00 03:00 N
AirIndia AI101 Mumbai NewYork 1200 900 10:00 01:00 N
Pune
NewYork

Example 4 : Nashik to London

4
Nashik
Mumbai
Dubai
London
5
IndiGo 6E123 Nashik Mumbai 80 60 07:00 08:00 Y
Emirates EK501 Mumbai Dubai 400 180 10:00 13:00 Y
BritishAirways BA108 Dubai London 700 480 15:00 23:00 N
AirIndia AI131 Mumbai London 900 600 09:00 19:00 N
Emirates EK502 London Dubai 700 480 01:00 09:00 N
Nashik
London


 */