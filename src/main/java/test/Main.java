/* COP 3503C Assignment 3
This program is written by: Joshua Samontanez */

package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P3/in3.txt"));

        // Reads the first three inputs on the first line of the file
        String firstLine = scanner.readLine();
        String[] threeInputs = firstLine.split(" ", 3);
        int numComputers = Integer.parseInt(threeInputs[0]);
        int numConnections = Integer.parseInt(threeInputs[1]);
        int numDisconnections = Integer.parseInt(threeInputs[2]);

        // Store the results into an array
        long[] results = new long[numDisconnections + 1];
        // Initialize the edges array object
        Edges[] edges = new Edges[numConnections];

        // Read all the edges
        for (int i = 0; i < numConnections; i++){
            String mLines = scanner.readLine();
            String[] arr2 = mLines.split(" ", 2);

            int u = Integer.parseInt(arr2[0]) - 1;
            int v = Integer.parseInt(arr2[1]) - 1;

            // Store all the edges in the edges array, and initialize the status to 1
            edges[i] = new Edges(u, v, 1);

        }

        ArrayList<Long> remove = new ArrayList<>();
        int removeOrder;

        // Store the edges to remove into the array list
        for (int i = 0; i < numDisconnections; i++){
            removeOrder = Integer.parseInt(scanner.readLine()) - 1;

            remove.add(i, (long) removeOrder);
            edges[removeOrder].setStatus(0);
        }

        DisjointSet set = new DisjointSet(numComputers);
        int counter = 0;

        // Start from the last disconnection
        for(int i = numDisconnections; i >= 0; i--){
            if (counter == 0){
                main.connectEdges(numConnections, edges, set);
                counter++;
            }

            else {
                // Get the edges from the remove list
                int u = edges[Math.toIntExact(remove.get(i))].getU();
                int v = edges[Math.toIntExact(remove.get(i))].getV();


                // Union the 2 numbers on that edge
                set.union(u, v);
            }


            // If we reached 0, that means all edges was already set to 1
            if(i != 0){
                edges[Math.toIntExact(remove.get(i - 1))].setStatus(1);
            }

            // Calculate the answer by knowing the number of members of each set
            results[i] = set.calculateResult(numComputers);

        }

        scanner.close();
        // Print the final results
        main.printResults(results);
    }

    public void connectEdges(int numConnections, Edges[] edges, DisjointSet set){
        // Loop through all the edges
        for(int j = 0; j < numConnections; j++){
            if(edges[j].getStatus() == 1){
                int u = edges[j].getU();
                int v = edges[j].getV();

                // Union the 2 numbers on that edge
                set.union(u, v);
            }
        }
    }

    public void printResults(long[] results){
        // Print the values inside the results array
        for (long result : results) {
            System.out.println(result);
        }
    }
}


class DisjointSet {
    public static ArrayList<Pair> parents;
    //public static Pair[] parents;
    public static int countSets;
    public static ArrayList<Integer> roots = new ArrayList<>();

    DisjointSet(int m){
        // Initialize the size of the parents arraylist
        parents = new ArrayList<>(m);
        // Initialize the countSets to the number of nodes we have
        countSets = m;

        // Initialize our disjoint sets
        for(int i = 0; i < m; i++){
            parents.add(i, new Pair(i, 0, 1L, true));
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
        if(parents.get(root1).getHeight() > parents.get(root2).getHeight()){
            parents.get(root2).setID(root1);
            // Increase the number of members inside the root node
            parents.get(root1).incNumMembers();
            // Remove the attached tree from the root list
            parents.get(root2).setRoot(false);
            removeFromList(root2);
        }



        else if(parents.get(root2).getHeight() > parents.get(root1).getHeight()){
            parents.get(root1).setID(root2);
            // Increase the number of members inside the root node
            parents.get(root2).incNumMembers();
            // Remove the attached tree from the root list
            parents.get(root1).setRoot(false);
            removeFromList(root1);
        }

        // If they have the same size
        else{
            // Attach the tree with bigger index into the bigger one
            parents.get(root2).setID(root1);

            // Calculate the new number of members
            long newNumMembers = parents.get(root1).getNumMembers() + parents.get(root2).getNumMembers();

            // Update the number of member of a set
            parents.get(root1).setNumMembers(newNumMembers);

            // Increase the height of the root
            parents.get(root1).increaseHeight();
            // Remove the attached tree from the root list
            parents.get(root2).setRoot(false);
            removeFromList(root2);
        }
    }

    public void removeFromList(int root){
        roots.remove(Integer.valueOf(root));
    }

    public int find(int ID){
        // The item is already the root
        if(ID == parents.get(ID).getID())
            return ID;

        // Find the item parents root
        int result = find(parents.get(ID).getID());

        // If the result is not the existing parent, make it the parent
        if(result != parents.get(ID).getID()){
            // Attach that item directly into the root
            parents.get(ID).setID(result);
            // Decrease the height of the root
            parents.get(result).decreaseHeight();
        }

        return result;

    }

    public long calculateResult(int numComputers){
        long sum = 0;
        
        // Sorts the list of roots in increasing order
        Collections.sort(roots);

        long[] numMembers = new long[countSets];
        int numMembersCounter = 0;

        // Compute the connectivity of the computers
        for(int i = 0; i < numComputers; i++){
            if(parents.get(i).isRoot()){
                numMembers[numMembersCounter] = parents.get(i).getNumMembers();
                long membersSquared =  numMembers[numMembersCounter] *  numMembers[numMembersCounter];
                sum = sum + membersSquared;

                numMembersCounter++;
            }
        }

        // Used for testing the contents
        // System.out.println("Sets: " + countSets);
        // System.out.println(" Roots: " + roots);
        //System.out.println("ROOT SIZE: " + roots.size());
         //System.out.println(Arrays.toString(numMembers));

        return sum;
    }


}

// Stores the information for each node
class Pair{
    private int height;
    private int ID;
    private long numMembers;
    private boolean root;

    Pair(int myID, int myHeight, long myNumMembers, boolean myRoot){
        height = myHeight;
        ID = myID;
        numMembers = myNumMembers;
        root = myRoot;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isRoot() {
        return root;
    }

    public long getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(long numMembers) {
        this.numMembers = numMembers;
    }

    public void incNumMembers() {
        numMembers = numMembers + 1L;
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

    @Override
    public String toString() {
        return "Pair{" +
                "height=" + height +
                ", ID=" + ID +
                ", numMembers=" + numMembers +
                ", root=" + root +
                '}';
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