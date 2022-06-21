package D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dijkstra {
    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P5/in5.txt"));

        // Get the first line of the input
        String firstLine = scanner.readLine();
        String[] arr1 = firstLine.split(" ", 3);

        // Read the number of rows and columns
        int cities = Integer.parseInt(arr1[0]);
        int roads = Integer.parseInt(arr1[1]);
        int capital = Integer.parseInt(arr1[2]) - 1;

        ArrayList<E> edges = new ArrayList<>();
        for (int i = 0; i < roads; i++) {
            String line = scanner.readLine();
            String[] arr2 = line.split(" ", 3);

            int source = Integer.parseInt(arr2[0]) - 1;
            int destination = Integer.parseInt(arr2[1]) - 1;
            int weight = Integer.parseInt(arr2[2]);

            edges.add(new E(source, destination, weight));
            //edges.add(new E(destination, source, weight));
        }

        int distance = Integer.parseInt(scanner.readLine());

        // Construct a graph
        G graph = new G(edges, cities);
        // Initialize the edges for the graph


        // Total number of nodes in the graph (0 - 4)
        //int n = 10;
        // Construct a graph
        //G graph = new G(edges, n);

        // Run the Dijkstra algo for every node to find the shortest path from
        // the source to all the vertices
        for (int source = 0; source < cities; source++) {
            //findShortestPath(graph, source, cities);
        }
        findShortestPath(graph, 0, cities);
        System.out.println();
        findShortestPath(graph, 1, cities);
        System.out.println();
        findShortestPath(graph, 2, cities);
        //findShortestPath(graph, 0, cities);
    }

    private static void findShortestPath(G graph, int source, int n) {
        // Create a min heap which makes the shortest distance as the first item
        PriorityQueue<N> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        // Start adding the first/starting node
        minHeap.add(new N(source, 0));
        // Set the initial distance from source to 'v' (last node) as infinity
        List<Integer> dist = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
        dist.set(source, 0);

        // Tracks if a node is visited or not
        boolean[] visited = new boolean[n];
        visited[source] = true;

        // Stores the predecessors for each node
        int[] prev = new int[n];
        // Starting node doesn't have a predecessor
        prev[source] = -1;

        // Run till min-heap is empty
        while (!minHeap.isEmpty()) {
            // Get the vertex with the shortest distance
            N node = minHeap.poll();
            // Get the vertex number
            int u = node.vertex;



            // Visit all the neighbors of that vertex
            for (E edge: graph.adjacencyList.get(u)){
                int v = edge.dest;
                int weight = edge.weight;

                //System.out.println(u + " " + v + " ");

                // Relaxation step
                if (!visited[v] && (dist.get(u) + weight) < dist.get(v)){
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new N(v, dist.get(v)));
                }
            }
            //System.out.println();
            visited[u] = true;
        }

        // Printing the path
        List<Integer> route = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i != source && dist.get(i) != Integer.MAX_VALUE) {
                getRoute(prev, i, route);
                System.out.printf("Path (%d --> %d): Minimum cost = %d, Route = %s\n",
                        source, i, dist.get(i), route);

                route.clear();
            }
        }
    }

    private static void getRoute(int[] prev, int i, List<Integer> route) {
        if (i >= 0){
            getRoute(prev, prev[i], route);
            route.add(i);
        }
    }
}

class E {
    int source;
    int dest;
    int weight;

    public E (int source, int dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }
}

class N {
    int vertex;
    int weight;

    public N (int vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }
}

class G {
    List<List<E>> adjacencyList = null;

    G (List<E> edges, int n) {
        adjacencyList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (E edge: edges) {
            adjacencyList.get(edge.source).add(edge);
        }
    }
}

