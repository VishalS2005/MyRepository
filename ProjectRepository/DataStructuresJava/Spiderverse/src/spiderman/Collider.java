package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public Profile[] profileVar;
    public DimensionNode[] dimensionList;
    public DimensionNode[] dimNodeAdjList;
    
    
    public Collider() {
        profileVar = null;
        dimensionList = null;
        dimNodeAdjList = null;
    }

    public Profile[] getProfileVar() { return profileVar; }

    public void createAdjacencyList(String dimensionFile) {
        Clusters tempCl = new Clusters();                               //create Cluster object
        tempCl.createCluster(dimensionFile);                            //create clusterVar and all the private variables so that they are not null
        dimNodeAdjList = new DimensionNode[tempCl.dimensionAmount];     //create Adjancency table
        DimensionNode[] clusterVar = tempCl.clusterVar;
        StdIn.setFile(dimensionFile);
        int size = StdIn.readInt();
        StdIn.readLine();
        int index = 0;
        while(size > 0) {   
            int dimensionNum = StdIn.readInt();     //dimension number
            int canonEvent = StdIn.readInt();       //dimension amount of canon events
            int dimensionWeight = StdIn.readInt();  //dimension weight
            DimensionNode tempNode = new DimensionNode(dimensionNum, canonEvent, dimensionWeight, null);
            dimNodeAdjList[index] = tempNode;

            index++;
            size--;
        }  
        dimensionList = dimNodeAdjList;
        profileVar = new Profile[dimNodeAdjList.length];   
        //in this same loop check clusterVar for the connections and add them here
        for (int i = 0; i < clusterVar.length; i++) {
            //create a node at the front representative of what 
            DimensionNode front = clusterVar[i];
            DimensionNode ptr = clusterVar[i].getNextDimensionNode();
            while (ptr != null) { 
                DimensionNode node1 = new DimensionNode(front.getDimensionNum(), front.getCanonEvent(), front.getDimensionWeight(), null);
                DimensionNode node2 = new DimensionNode(ptr.getDimensionNum(), ptr.getCanonEvent(), ptr.getDimensionWeight(), null);
                addEdge(node1, node2);
                ptr = ptr.getNextDimensionNode(); 
            }
        }
    }

    public void addEdge(DimensionNode front, DimensionNode add) {
        add(findFront(front),add);
        add(findFront(add),front);
    }

    public void add(int index, DimensionNode add) {
        DimensionNode ptr = dimNodeAdjList[index];
        while(ptr.getNextDimensionNode() != null) {
             ptr = ptr.getNextDimensionNode();
        }
        ptr.setNextDimensionNode(add);     
    }

    public int findFront(DimensionNode x) {
        int index = 0;
        for (int i = 0; i < dimNodeAdjList.length; i++) {
            if (dimNodeAdjList[index].getDimensionNum() == x.getDimensionNum()) return index;
            else index++; 
        }
        return -1;
    }



    public void addProfile(String spiderFile) {
        StdIn.setFile(spiderFile);
        int d = StdIn.readInt();
        StdIn.readLine();
        while (d > 0) {
            int dimCurr = StdIn.readInt(); 
            StdIn.readChar();
            String name = StdIn.readString();
            int dimSig = StdIn.readInt();
            StdIn.readLine();
            Profile tempProfile = new Profile(dimCurr, name, dimSig, null);
            for (int i = 0; i < dimensionList.length; i++) {
                if (tempProfile.getCurrentDimension() == dimensionList[i].getDimensionNum()) {
                    Profile newProfile = new Profile(tempProfile.getCurrentDimension(), tempProfile.getName(), tempProfile.getDimensionSignature(), null);
                    if (profileVar[i] == null) profileVar[i] = newProfile;
                    else {  
                        Profile ptr = profileVar[i];
                        while (ptr.getNextProfile() != null ) ptr = ptr.getNextProfile();
                        ptr.setNextProfile(newProfile);
                    }
                } 
            }
            d--;
        }
    }


    
    public void print() {
        for (int i = 0; i < dimNodeAdjList.length; i++) {
            DimensionNode ptr = dimNodeAdjList[i];
            while (ptr != null) { 
                StdOut.print(ptr.getDimensionNum() + " ");
                ptr = ptr.getNextDimensionNode(); 
            }
            StdOut.println();
        }
    }
    
    public void printProfiles() {
        StdOut.println();
        for (int j = 0; j < dimensionList.length; j++) {
            StdOut.print(" " + dimensionList[j].getDimensionNum() + " ");
            if (profileVar[j] != null) { 
                Profile ptr = profileVar[j];
                while (ptr != null) {
                    StdOut.print(" " + ptr.getName() + " "); 
                    ptr = ptr.getNextProfile();
                }
            }
            StdOut.println();
        }
    }
    public static void main(String[] args) {

        Collider col = new Collider();
        StdOut.setFile(args[2]);
        col.createAdjacencyList(args[0]);
        col.print();
        col.addProfile(args[1]);
        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }
        // WRITE YOUR CODE HERE   
    }
}