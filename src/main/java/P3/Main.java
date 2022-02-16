package P3;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Get the file ready to read the input
        Scanner scanner = new Scanner(new FileReader("docs/P3/in.txt"));

        // Reads the first three input on the first line of the file
        int numComputers = scanner.nextInt();
        int numConnections = scanner.nextInt();
        int numDisconnections = scanner.nextInt();

        int[] results = new int[numDisconnections + 1];

        Edges[] edges = new Edges[numConnections];


        for (int i = 0; i < numConnections; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;

            edges[i] = new Edges(u, v, 1);
        }

        results[0] = connect(numComputers, edges);
        //System.out.println(Arrays.toString(edges));

        int[] removeList = new int[numDisconnections];
        for (int i = 0; i < numDisconnections; i++){
            removeList[i] = scanner.nextInt() - 1;

            edges[removeList[i]].setStatus(0);

            results[i + 1] = connect(numComputers, edges);
            //System.out.println(Arrays.toString(edges));
        }

        printResults(results);
    }

    public static void printResults(int[] results){
        for(int i = 0; i < results.length; i++){
            System.out.println(results[i]);
        }
    }

    public static int connect(int numComputers, Edges[] edges){
        DisjointSet set = new DisjointSet(numComputers);

        for(int i = 0; i < edges.length; i++){
            if(edges[i].getStatus() != 0){
                int u = edges[i].getU();
                int v = edges[i].getV();

                set.union(u, v);
            }
        }

        int numSets = DisjointSet.countSets;
        //System.out.println(set);
        //System.out.println(numSets);

        int[] numMembers = countMembers(numSets, set, numComputers);
        return calculate(numMembers);
    }

    public static int calculate(int[] numMembers){
        int total = 0;

        for(int i = 0; i < numMembers.length; i++){
            int memberSquared = numMembers[i] * numMembers[i];
            total = total + memberSquared;
        }
        return total;
    }

    public static int[] countMembers(int numSets, DisjointSet set, int numComputers){
        int[] numMembers = new int[numSets];
        for(int i = 0; i < numSets; i++){
            numMembers[i] = 1;
        }

        //int[] roots = new int[numSets];
        ArrayList<Integer> roots = new ArrayList<>();

        for(int i = 0; i < numComputers; i++){
            int parent = set.find(i);

            if(!roots.contains(parent)){
                roots.add(parent);
            }

            else {
                numMembers[roots.indexOf(parent)]++;
            }
        }
        //System.out.println(roots);
        //System.out.println(Arrays.toString(numMembers));

        return numMembers;
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


        if(root1 == root2){
            return;
        }

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

    @Override
    public String toString(){
        String ans = "";
        for(int i = 0; i < parents.length; i++){
            ans = ans + "(" + i + ", " + parents[i].getID() + ") ";
        }
        return ans;
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
}

class Edges {
    private int u;
    private int v;
    private int status;

    Edges(int myU, int myV, int myStatus){
        u = myU;
        v = myV;
        status = myStatus;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Edges{" +
                "u=" + u +
                ", v=" + v +
                ", status=" + status +
                '}';
    }
}



