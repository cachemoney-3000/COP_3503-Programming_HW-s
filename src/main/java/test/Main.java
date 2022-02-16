package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Get the file ready to read the input
        Scanner scanner = new Scanner(new FileReader("docs/P3/in.txt"));

        // Reads the first three input on the first line of the file
        int numComputers = scanner.nextInt();
        int numConnections = scanner.nextInt();
        int nunDisconnections = scanner.nextInt();

        DisjointSet ds = new DisjointSet(numComputers);

        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        ArrayList<ArrayList<Integer>> combination = new ArrayList<>();

        for(int i = 0; i < numComputers; i++){
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < numConnections; i++){
            combination.add(new ArrayList<>());
        }

        // Read the edges
        for(int i = 0; i < numConnections; i++){
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            graph.get(u).add(v);
            graph.get(v).add(u);

            combination.get(i).add(v);
            combination.get(i).add(u);
        }

        printGraph(graph);

        Integer[] removeList;
        int remove;
        for(int i = 0; i < nunDisconnections; i++){
            remove = scanner.nextInt() - 1;
            removeList = combination.get(remove).toArray(new Integer[0]);


            graph = connect(numConnections, combination, remove, numComputers);
            printGraph(graph);
        }




    }

    public static ArrayList<ArrayList<Integer>> connect(int numConnections, ArrayList<ArrayList<Integer>> combination,
                                                        int notInclude, int numComputers){
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();;
        for(int i = 0; i < numComputers; i++){
            graph.add(new ArrayList<>());
        }

        // Read the edges
        for(int i = 0; i < numConnections; i++){

            if(i != notInclude){
                ArrayList<Integer> vertices = combination.get(i);
                int v = vertices.get(0);
                int u = vertices.get(1);

                graph.get(u).add(v);
                graph.get(v).add(u);
            }
        }

        return graph;
    }



    public static void printGraph(ArrayList<ArrayList<Integer>> graph)
    {
        System.out.print("\n Graph Adjacency List ");
        for (int i = 0; i < 9; ++i)
        {
            System.out.print(" \n [" + i + "] :");
            // iterate edges of i node
            for (int j = 0; j < graph.get(i).size(); j++)
            {
                System.out.print("  " + graph.get(i).get(j));
            }
        }
    }

    public static void results(int numComputers, int[] removeList, ArrayList<ArrayList<Integer>> graph){
        DisjointSet ds = new DisjointSet(numComputers);
        boolean[] open = new boolean[numComputers];

        ArrayList<String> answers = new ArrayList<>();

        // Go backwards to open the farms
        for (int i = numComputers - 1; i >= 0; i--){
            open[removeList[i]] = true;

            // Connect the opening farm with its neighbor. Only if its neighboring farm is open
            for(int neighbor : graph.get(removeList[i])){
                if(open[neighbor]){
                    ds.union(removeList[i], neighbor);
                }
            }

            answers.add(String.valueOf(DisjointSet.countSets));
            /*
            // Prints "YES" if the number of connected components is equal to the unopened farms + 1
            if(DisjointSet.countSets == i + 1){
                answers.add("YES");
            }
            // Print "NO" if it doesn't satisfy the condition above
            else{
                answers.add("NO");
            }

             */

        }

        // Prints out the answers
        Collections.reverse(answers);
        for (String answer : answers) {
            System.out.println(answer);
        }
        System.out.println();
    }

}




class DisjointSet {
    public static Pair[] parents;
    public static int countSets;

    DisjointSet(int m){
        parents = new Pair[m];
        countSets = m;

        // Initialize our disjoint sets
        for(int i = 0; i < m; i++){
            parents[i] = new Pair(i, 0);
        }
    }

    public void union(int item1, int item2) {
        // Find the roots of the 2 items
        int root1 = find(item1);
        int root2 = find(item2);

        countSets--;
        if(root1 == root2){
            return;
        }

        // Attach the smaller tree into the bigger tree
        if(parents[root1].getHeight() > parents[root2].getHeight())
            parents[root2].setID(root1);

        else if(parents[root2].getHeight() > parents[root1].getHeight())
            parents[root1].setID(root2);
            // If they have the same size
        else{
            // Attach the tree with bigger index into the bigger one
            parents[root2].setID(root1);
            // Increase the height of the root
            parents[root1].increaseHeight();
        }

    }

    public int find(int ID){
        // The item is already the root
        if(ID == parents[ID].getID())
            return ID;

        // Find the item parents root
        int result = find(parents[ID].getID());

        // If the result is not the existing parent, make it the parent
        if(result != parents[ID].getID()){
            // Attach that item directly into the root
            parents[ID].setID(result);
            // Decrease the height of the root
            parents[result].decreaseHeight();
        }
        return ID;
    }
}

class Pair{
    private int height;
    private int ID;

    Pair(int myID, int myHeight){
        height = myHeight;
        ID = myID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHeight() {
        return height;
    }

    public void increaseHeight() {
        height++;
    }

    public void decreaseHeight() {
        height--;
    }
}


