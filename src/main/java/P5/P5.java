package P5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class P5 {
    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P5/in6.txt"));
        // Get the first line of the input
        String firstLine = scanner.readLine();
        String[] arr1 = firstLine.split(" ", 3);

        // Read the number of rows and columns
        int cities = Integer.parseInt(arr1[0]);
        int roads = Integer.parseInt(arr1[1]);
        int capital = Integer.parseInt(arr1[2]) - 1;

        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < roads; i++) {
            String line = scanner.readLine();
            String[] arr2 = line.split(" ", 3);

            int source = Integer.parseInt(arr2[0]) - 1;
            int destination = Integer.parseInt(arr2[1]) - 1;
            int weight = Integer.parseInt(arr2[2]);

            edges.add(new Edge(source, destination, weight));
            //edges.add(new Edge(destination, source, weight));
        }

        int distance = Integer.parseInt(scanner.readLine());

        // Construct a graph
        Graph graph = new Graph(edges, cities);

        //edges.sort(Comparator.comparingInt(edge -> edge.weight));


        /*
        for (int i = 0; i < cities; i++) {
            for (Edge edge: graph.adjacencyList.get(i)) {
                int v = edge.dest + 1;
                int u = i + 1;
                int weight = edge.weight;

                System.out.println(u + " " + v + " " + weight);
            }
        }

         */

        findShortestPath(graph, capital, cities, distance, edges);

    }

    private static void findShortestPath(Graph graph, int source, int cities, int t_distance, ArrayList<Edge> e) {
        // Set the initial distance from source to 'v' (last node) as infinity
        ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(cities, Integer.MAX_VALUE));
        dist.set(source, 0);    // Set the starting node distance to itself to zero

        // Create a min heap which makes the shortest distance as the first item
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Node(source, 0));   // Add the starting node to the minheap

        // Tracks if a node is visited or not
        boolean[] visited = new boolean[cities];
        visited[source] = true; // Set the starting node as visited

        // Stores the predecessors for each node
        int[] prev = new int[cities];
        prev[source] = -1;      // Starting node doesn't have a predecessor

        int treasures_inCity = 0;
        int treasures_onRoad = 0;

        ArrayList<Integer> cities_with_treasures = new ArrayList<>();

        // Run till min-heap is empty
        while (!minHeap.isEmpty()) {
            // Get the vertex with the shortest distance
            Node node = minHeap.poll();
            int u = node.vertex;    // Get the number of that vertex
            int d = node.weight;
            visited[u] = true;      // Mark that vertex as visited

            //System.out.println("while: " + u + " " + d);

            if (d > t_distance) break;

            // Visit all the neighbors of that vertex
            List<Edge> edges = graph.adjacencyList.get(u);
            for (Edge edge : edges) {
                int v = edge.dest;
                int weight = edge.weight;



                int newDistance = d + weight;

                if (newDistance <= dist.get(v)) {
                    dist.set(v, newDistance);
                    //prev[v] = u;
                    minHeap.add(new Node(v, dist.get(v)));

                    //System.out.println(u + " - " + v);

                    boolean treasure_already_found = cities_with_treasures.contains(v);

                    // If the distance between a city from the capital
                    // matched the distance of the treasure from the capital, we found the treasure
                    if (!treasure_already_found && newDistance == t_distance) {
                        System.out.println(u + " " + v);
                        treasures_inCity++;   // Counts how many treasures we found
                        cities_with_treasures.add(v);
                    }
                }
            }
        }

        // NODES REACHED
        Integer[] arr = dist.toArray(new Integer[0]);
        int reachable = (int) Arrays.stream(arr).filter(d -> d <= t_distance).count();
        int reachableRoad = 0;

        for (Edge edge : e) {
            int u = edge.dest;
            int v = edge.source;
            int w = edge.weight;

            if (w % 2 == 0) {
                //System.out.println(v + " " + w);
                w = w - 1;
                //System.out.println("AFTER " + v + " " + w);
            }

            int a = dist.get(u) > t_distance ? 0 : Math.min(t_distance - dist.get(u), w);
            int b = dist.get(v) > t_distance ? 0 : Math.min(t_distance - dist.get(v), w);

            reachableRoad += Math.min(a + b, w);
        }

        treasures_onRoad = (reachableRoad / t_distance) - treasures_inCity;

        System.out.println("Reachable Nodes: " + reachable + " Roads Traveled: " + reachableRoad);

        System.out.println("In city: " + treasures_inCity);
        System.out.println("On road: " + treasures_onRoad);

        // Printing
        for (int i = 0; i < cities; i++) {
            if (i != source && dist.get(i) != Integer.MAX_VALUE) {
                System.out.printf("Path (%d --> %d): Minimum cost = %d\n",
                        source, i, dist.get(i));

            }
        }
    }
}

// Object for storing the edge (roads) information
class Edge {
    int source;
    int dest;
    int weight;

    public Edge (int source, int dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }
}

// Object for storing the node (city) information
class Node {
    int vertex;
    int weight;

    public Node (int vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }
}

// Object for storing how the nodes (cities) and edge (roads) are connected
class Graph {
    List<List<Edge>> adjacencyList;

    Graph (List<Edge> edges, int n) {
        adjacencyList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (Edge edge: edges) {
            adjacencyList.get(edge.source).add(edge);
        }
    }
}
