/* COP 3503C Assignment 3
This program is written by: Joshua Samontanez */

package P3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Main set = new Main();
        // Get the file ready to read the input
        Scanner scanner = new Scanner(new FileReader("docs/P3/in.txt"));

        // Reads the first three input on the first line of the file
        int numComputers = scanner.nextInt();
        int numConnections = scanner.nextInt();
        int numDisconnections = scanner.nextInt();

        // Store the results into an array
        int[] results = new int[numDisconnections + 1];
        // Initialize the edges array object
        Edges[] edges = new Edges[numConnections];

        // Loop through all the connections
        for (int i = 0; i < numConnections; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            // Store all the edges in the edges array, and initialize the status to 1
            edges[i] = new Edges(u, v, 1);
        }

        // Calculate the first current connectivity before the deletion
        results[0] = set.connect(numComputers, edges);

        // Initialize removeList to store which edge to delete
        int[] removeList = new int[numDisconnections];
        for (int i = 0; i < numDisconnections; i++){
            removeList[i] = scanner.nextInt() - 1;

            // Once the program know what edge to remove, set the status of that edge to 0
            edges[removeList[i]].setStatus(0);

            // Calculate the current connectivity after an edge was deleted
            results[i + 1] = set.connect(numComputers, edges);
        }

        // Print the final results
        set.printResults(results);
    }

    public void printResults(int[] results){
        // Print the values inside the results array
        for (int result : results) {
            System.out.println(result);
        }
    }

    public int connect(int numComputers, Edges[] edges){
        DisjointSet set = new DisjointSet(numComputers);

        // Loop through all the edges
        for (Edges edge : edges) {
            if (edge.getStatus() != 0) {
                int u = edge.getU();
                int v = edge.getV();

                // Perform a union using the 2 combinations (u and v)
                set.union(u, v);
            }
        }

        // Counts how many sets exists
        int numSets = DisjointSet.countSets;
        // Store the number of nodes in each sets
        int[] numMembers = countMembers(numSets, set, numComputers);
        // Return the calculation of the current connectivity using the info that the program had from above
        return calculate(numMembers);
    }

    public int calculate(int[] numMembers){
        int total = 0;

        // By knowing the number of members a set has, we can find the current connectivity
        // Loop through the array and calculate the current connectivity
        for (int numMember : numMembers) {
            int memberSquared = numMember * numMember;
            total = total + memberSquared;
        }

        return total;
    }

    public int[] countMembers(int numSets, DisjointSet set, int numComputers){
        int[] numMembers = new int[numSets];
        for(int i = 0; i < numSets; i++){
            // Initialize the member to 1 for each sets
            numMembers[i] = 1;
        }
        // Stores which node is the root
        ArrayList<Integer> roots = new ArrayList<>();

        // Loop through all the nodes
        for(int i = 0; i < numComputers; i++){
            // Find the parent of each node
            int parent = set.find(i);

            // If the root is not in the array list yet, add them
            if(!roots.contains(parent)){
                roots.add(parent);
            }

            // If the root already exists, increase the number of members for that parent
            else {
                numMembers[roots.indexOf(parent)]++;
            }
        }
        return numMembers;
    }

}


class DisjointSet {
    public static Pair[] parents;
    public static int countSets;

    DisjointSet(int m){
        parents = new Pair[m];
        // Initialize the countSets to the number of nodes we have
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

        // The 2 sets have the same root
        if(root1 == root2){
            return;
        }

        // If we can combine the 2 sets, decrease the count of the sets
        countSets--;

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
        int itemID = 0;

        // Keep looping until we find the root/parent of an item
        while(itemID != parents[ID].getID()){
            itemID = parents[ID].getID();
        }

        return itemID;
    }

    // Used for testing
    @Override
    public String toString(){
        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < parents.length; i++){
            ans.append("(").append(i).append(", ").append(parents[i].getID()).append(") ");
        }
        return ans.toString();
    }
}

// Stores the information for each node
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
}

// Stores the information for each edge
class Edges {
    // Two combinations for each edge
    private int u;
    private int v;
    // Status for each edge (1 = Don't destroy, 0 = Destroy edge)
    private int status;

    Edges(int myU, int myV, int myStatus){
        u = myU;
        v = myV;
        status = myStatus;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Used for testing
    @Override
    public String toString() {
        return "Edges{" +
                "u=" + u +
                ", v=" + v +
                ", status=" + status +
                '}';
    }
}