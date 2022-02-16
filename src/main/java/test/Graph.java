package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Graph
{
    // Number of vertices in graph
    public int vertices;
    // Use to collect edges information
    public ArrayList < ArrayList <Integer>> adjacencylist;
    public Graph(int vertices)
    {
        this.vertices = vertices;
        this.adjacencylist = new ArrayList <ArrayList <Integer>> (vertices);
        for (int i = 0; i < this.vertices; ++i)
        {
            this.adjacencylist.add(new ArrayList <Integer > ());
        }
    }
    public void addEdge(int u, int v)
    {
        if (u < 0 || u >= this.vertices || v < 0 || v >= this.vertices)
        {
            return;
        }
        // Add node edge
        adjacencylist.get(u).add(v);
        adjacencylist.get(v).add(u);
    }
    // Display graph nodes and edges
    public void printGraph()
    {
        System.out.print("\n Graph Adjacency List ");
        for (int i = 0; i < this.vertices; ++i)
        {
            System.out.print(" \n [" + i + "] :");
            // iterate edges of i node
            for (int j = 0; j < this.adjacencylist.get(i).size(); j++)
            {
                System.out.print("  " + this.adjacencylist.get(i).get(j));
            }
        }
    }
    public void dfs(boolean[] visited, int start)
    {
        if (start < 0 || start >= this.vertices)
        {
            // In case given invalid node
            return;
        }
        // Mark a current visited node
        visited[start] = true;

        int i = 0;
        // Execute edges of given start vertices
        while (i < adjacencylist.get(start).size())
        {
            if (visited[adjacencylist.get(start).get(i)] == false)
            {
                // When edge node not visiting, then perform DFS operation
                dfs(visited, adjacencylist.get(start).get(i));
            }
            // Visit to next node
            i++;
        }
    }

    public int edgePosition(int u, int v)
    {

        int i = 0;
        while(i < this.adjacencylist.get(u).size())
        {
            if(this.adjacencylist.get(u).get(i)==v)
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    public boolean removeNodeEdge(int u, int v)
    {

        if(u < 0 || v < 0 || u > this.vertices
                || v > this.vertices )
        {
            return false;
        }
        int a = edgePosition(u,v);
        int b = edgePosition(v,u);

        if(a==-1 || b == -1)
        {
            // Given edge are not exist between given nodes
            return false;
        }
        // Remove edges
        this.adjacencylist.get(u).remove(a);
        this.adjacencylist.get(v).remove(b);

        return true;

    }
    public void unsetVisit(boolean[] visited)
    {
        for (int i = 0; i < this.vertices; ++i)
        {
            visited[i] = false;
        }
    }
    public boolean isAllVerticesVisit(boolean[] visited)
    {
        for (int i = 0; i < this.vertices; ++i)
        {
            if(visited[i]==false)
            {
                return false;
            }
        }
        return true;
    }
    public void disconnectByEdge(int u, int v)
    {


        boolean[] visited = new boolean[this.vertices];

        unsetVisit(visited);

        dfs(visited, 0);



        if(isAllVerticesVisit(visited)==true)
        {

            if(removeNodeEdge(u,v)==true)
            {

                unsetVisit(visited);

                dfs(visited, 0);

                // Add remove edge
                addEdge(u,v);

                if(isAllVerticesVisit(visited))
                {
                    // not a bridge edge
                    // graph are not disconnect

                    System.out.print("\n Remove edge ["+u+"-"+v+"] not disconnecting this graph ");
                }
                else
                {

                    System.out.print("\n Remove edge ["+u+"-"+v+"] disconnecting this graph ");
                }

            }
            else
            {
                // When edges are not exist
                System.out.print("\n No edge between vertices ["+u+"-"+v+"]");
            }
        }
        else
        {
            // When graph is already disconnected?
            System.out.print("\n Graph is already disconnected");
        }

    }


    public static void main(String[] args) throws FileNotFoundException {
        // Get the file ready to read the input
        Scanner scanner = new Scanner(new FileReader("docs/P3/in.txt"));

        // Reads the first three input on the first line of the file
        int numComputers = scanner.nextInt();
        int numConnections = scanner.nextInt();
        int nunDisconnections = scanner.nextInt();

        ArrayList<ArrayList<Integer>> combination = new ArrayList<>();
        Graph graph = new Graph(numComputers);
        // Read the edges

        for(int i = 0; i < numConnections; i++){
            combination.add(new ArrayList<>());
        }

        for(int i = 0; i < numConnections; i++){
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            graph.addEdge(u, v);

            combination.get(i).add(v);
            combination.get(i).add(u);
        }

        // Display graph element
        graph.printGraph();

        Integer[] removeList;
        int remove;
        for(int i = 0; i < nunDisconnections; i++){
            remove = scanner.nextInt() - 1;
            removeList = combination.get(remove).toArray(new Integer[0]);

            graph.disconnectByEdge(1, 5);
        }

        // Display graph element
        graph.printGraph();

    }
}
