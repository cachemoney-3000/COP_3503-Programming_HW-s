/* COP 3503C Assignment 5
This program is written by: Joshua Samontanez */

package P5A;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class P5 {
    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P5/in5.txt"));

        // Get the first line of the input
        String firstLine = scanner.readLine();
        String[] arr1 = firstLine.split(" ", 3);

        // Read information about the number of cities, roads, and the capital
        int cities = Integer.parseInt(arr1[0]);
        int roads = Integer.parseInt(arr1[1]);
        int capital = Integer.parseInt(arr1[2]) - 1;

        // Reads the information about how the cities are connected
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < roads; i++) {
            String line = scanner.readLine();
            String[] arr2 = line.split(" ", 3);

            // Reads the source -> destination and the distance between them
            int source = Integer.parseInt(arr2[0]) - 1;
            int destination = Integer.parseInt(arr2[1]) - 1;
            int weight = Integer.parseInt(arr2[2]);

            edges.add(new Edge(source, destination, weight));
            //edges.add(new Edge(destination, source, weight));
        }

        // Reads the distance of the treasure from the capital
        int treasure_distance = Integer.parseInt(scanner.readLine());

        scanner.close();

        // Count how many treasures we can find using the information provided
        int[] treasures_found = count_treasures(capital, cities, treasure_distance, edges);

        int treasures_inCity = treasures_found[0];
        int treasures_onRoad = treasures_found[1];

        // Prints out the results
        System.out.println("In city: " + treasures_inCity);
        System.out.println("On the road: " + treasures_onRoad);
    }

    private static int[] count_treasures(int source, int cities, int t_distance, ArrayList<Edge> roads) {
        // Construct a graph
        Graph graph = new Graph(roads, cities);

        // Set the initial distance from source to the last node as infinity
        ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(cities, Integer.MAX_VALUE));
        dist.set(source, 0);    // Set the starting node distance to itself = zero

        // Create a min heap which makes the shortest distance as the first item
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Node(source, 0));   // Add the starting node to the minheap

        // Tracks how many treasures are found in city and on road
        int treasures_inCity = 0;
        int treasures_onRoad;

        // Tracks which cities already have treasure found so we don't double count
        ArrayList<Integer> cities_with_treasures = new ArrayList<>();

        // Used for finding treasures on road
        int reachableRoad = 0;
        int a;
        int b;

        // Run till min-heap is empty
        while (!minHeap.isEmpty()) {
            // Get the vertex with the shortest distance
            Node node = minHeap.poll();
            int u = node.vertex;    // Get the city number of that vertex
            int d = node.weight;    // Get the distance of that vertex

            // If the distance of the vertex is already bigger than
            // the distance of the treasure from the capital, skip it
            if (d > t_distance) break;

            // Visit all the neighbors of that vertex
            List<Edge> edges = graph.adjacencyList.get(u);
            for (Edge edge : edges) {
                int v = edge.dest;
                int weight = edge.weight;

                // Get the new distance
                int newDistance = d + weight;

                // Relaxation step, we travel the city that have less distance first
                if (newDistance < dist.get(v)) {
                    dist.set(v, newDistance);
                    minHeap.add(new Node(v, dist.get(v)));

                    boolean treasure_already_found = cities_with_treasures.contains(v);

                    // If the distance between a city from the capital matched
                    // the distance of the treasure from the capital, then we found the treasure
                    if (!treasure_already_found && newDistance == t_distance) {
                        treasures_inCity++;   // Counts how many treasures we found
                        cities_with_treasures.add(v);   // Add that city to our list
                        System.out.println(u + "  " + v);
                    }
                }

                /*
                // Takes care of one of the bug which wrongfully count the distance of a road
                if (weight % 2 == 0)
                    weight = weight - 1;

                // Check if we can find a treasure by going to that road
                a = dist.get(u) > t_distance ? 0 : Math.min(t_distance - dist.get(u), weight);
                b = dist.get(v) > t_distance ? 0 : Math.min(t_distance - dist.get(v), weight);

                // Gets the total of how many distance of roads we can go to find the treasure
                reachableRoad += Math.min(a + b, weight);

                 */
            }

        }




        // Keeps track of how far we can travel on the road
        reachableRoad = 0;
        // Loop through all the edges
        for (Edge edge : roads) {
            // Get the destination, source, and distance of each edge
            int u = edge.dest;
            int v = edge.source;
            int w = edge.weight;

            // Takes care of one of the bug which wrongfully count the distance of a road
            if (w % 2 == 0)
                w = w - 1;

            // Check if we can find a treasure by going to that road
            a = dist.get(u) > t_distance ? 0 : Math.min(t_distance - dist.get(u), w);
            b = dist.get(v) > t_distance ? 0 : Math.min(t_distance - dist.get(v), w);

            // Gets the total of how many distance of roads we can go to find the treasure
            reachableRoad += Math.min(a + b, w);
        }

        System.out.println("Roads Traveled: " + reachableRoad);

        // Calculates how many treasures we can find on the road
        treasures_onRoad = (reachableRoad / t_distance) - treasures_inCity;

        return new int[]{treasures_inCity, treasures_onRoad};
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
