/* COP 3503C Assignment 3
This program is written by: Joshua Samontanez */

package P3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws IOException {
        Main set = new Main();
        // Get the file ready to read the input
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P3/in2.txt"));

        // Reads the first three input on the first line of the file
        String firstLine = scanner.readLine();
        String[] arr1 = firstLine.split(" ", 3);

        int numComputers = Integer.parseInt(arr1[0]);
        int numConnections = Integer.parseInt(arr1[1]);
        int numDisconnections = Integer.parseInt(arr1[2]);

        // Store the results into an array
        int[] results = new int[numDisconnections + 1];
        // Initialize the edges array object
        Edges[] edges = new Edges[numConnections];

        // Loop through all the connections
        for (int i = 0; i < numConnections; i++) {
            String mLines = scanner.readLine();
            String[] arr2 = mLines.split(" ", 2);

            int u = Integer.parseInt(arr2[0]) - 1;
            int v = Integer.parseInt(arr2[1]) - 1;

            // Store all the edges in the edges array, and initialize the status to 1
            edges[i] = new Edges(u, v, 1);
        }

        // Calculate the first current connectivity before the deletion
        results[0] = set.connect(numComputers, edges);

        for (int i = 0; i < numDisconnections; i++){
            int dLines = Integer.parseInt(scanner.readLine()) - 1;

            // Once the program know what edge to remove, set the status of that edge to 0
            edges[dLines].setStatus(0);

            // Calculate the current connectivity after an edge was deleted
            results[i + 1] = set.connect(numComputers, edges);
        }
        scanner.close();
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

        // Store the number of nodes in each sets
        int[] numMembers = set.countMembers();
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
}


class DisjointSet {
    public static Pair[] parents;
    public static int countSets;
    public static ArrayList<Integer> roots = new ArrayList<>();

    DisjointSet(int m){
        parents = new Pair[m];
        // Remove the contents of the list every deletion of an edge
        roots.clear();
        // Initialize the countSets to the number of nodes we have
        countSets = m;

        // Initialize our disjoint sets
        for(int i = 0; i < m; i++){
            parents[i] = new Pair(i, 0, 1);
            roots.add(i);
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
        if(parents[root1].getHeight() > parents[root2].getHeight()){
            parents[root2].setID(root1);
            // Increase the number of members inside the root node
            parents[root1].incNumMembers();
            // Remove the attached tree from the root list
            removeFromList(root2);
        }


        else if(parents[root2].getHeight() > parents[root1].getHeight()){
            parents[root1].setID(root2);
            // Increase the number of members inside the root node
            parents[root2].incNumMembers();
            // Remove the attached tree from the root list
            removeFromList(root1);
        }

        // If they have the same size
        else{
            // Attach the tree with bigger index into the bigger one
            parents[root2].setID(root1);
            // Increase the number of members inside the root node
            parents[root1].incNumMembers();
            // Increase the height of the root
            parents[root1].increaseHeight();
            // Remove the attached tree from the root list
            removeFromList(root2);
        }
    }

    // Will remove an item from the roots list
    public void removeFromList(int parent){
        roots.remove(Integer.valueOf(parent));
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

        return result;
    }

    public int[] countMembers(){
        int[] numMembers = new int[countSets];

        // Sorts the list of roots in increasing order
        Collections.sort(roots);

        for(int i = 0; i < countSets; i++){
            // Initialize the member to 1 for each sets
            numMembers[i] = parents[roots.get(i)].getNumMembers();
        }

        // Used for testing the contents
        // System.out.println(countSets);
        // System.out.println(" Roots: " + roots);
        // System.out.println(Arrays.toString(numMembers));
        return numMembers;
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
    private int numMembers;

    Pair(int myID, int myHeight, int myNumMembers){
        height = myHeight;
        ID = myID;
        numMembers = myNumMembers;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void incNumMembers() {
        numMembers++;
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

// Stores the information for each edge
class Edges {
    // Two combinations for each edge
    private final int u;
    private final int v;
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