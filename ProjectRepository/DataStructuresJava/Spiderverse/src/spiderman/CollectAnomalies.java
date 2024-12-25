package spiderman;
import java.util.*;

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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {

    public boolean[] visited;
    public DimensionNode[] dimNodeAdjList;
    public DimensionNode[] dimensionList;
    public Profile[] profileVar;
    public DimensionNode[] edgeTo;




    public CollectAnomalies() { 
        visited = null; 
        dimNodeAdjList = null;
        dimensionList = null;
        profileVar = null;
        edgeTo = null;
    }


    public void collect(String dimensionFile, String spiderverseFile, String hubFile) {
        
        //create cluster table to create an adjacency list
        Clusters clu = new Clusters();
        clu.createCluster(dimensionFile);
        //create the adjacency list dimNodeAdjList, profileVar, visited boolean array, and dimensionList
        Collider col = new Collider();
        col.createAdjacencyList(dimensionFile);
        col.addProfile(spiderverseFile);
        dimNodeAdjList = col.dimNodeAdjList;
        dimensionList = col.dimensionList;
        profileVar = col.profileVar;
        visited = new boolean[col.dimensionList.length];
        edgeTo = new DimensionNode[col.dimensionList.length];
        //initialize the queue
        //take argument to find what dimension the hub is
        StdIn.setFile(hubFile);
        int hub = StdIn.readInt();
        //traverse through profileVar
        for (int j = 0; j < dimensionList.length; j++) {
            //go through each dimension and check if it's either null or the hub node
            if (profileVar[j] != null && dimensionList[j].getDimensionNum() != hub) { 
                Profile ptr = profileVar[j];
                while (ptr != null) {
                    //check if they are an anomaly before creating path to get them
                    if(!checkSpider(ptr.getName(), profileVar)) {
                        //traverse to check if there is a spider in the same dimension
                        //print anomaly
                        StdOut.print(ptr.getName() + " ");
                        //start at the beginning of the people at a dimension
                        Profile tempPtr = profileVar[j];
                        boolean spiderTookCareOfTheAnomaly = false;
                        while (tempPtr != null) {
                            //if spider is found, check if a spider already took care of the anomaly, print spider, change boolean so that we don't do breadthFirstSearch again, and then take anomaly back to hub
                            if(!spiderTookCareOfTheAnomaly) {
                                if (checkSpider(tempPtr.getName(), profileVar)) {
                                    StdOut.print(tempPtr.getName() + " ");
                                    spiderTookCareOfTheAnomaly = true;
                                    StdOut.print(ptr.getCurrentDimension() + " ");
                                    visited = new boolean[col.dimensionList.length];
                                    edgeTo = new DimensionNode[col.dimensionList.length];
                                    breadthFirstSearch(hub, ptr.getCurrentDimension(), false, true);
                                    StdOut.println();
                                }
                            }
                            tempPtr = tempPtr.getNextProfile();
                        }
                        //if there is not spider in the same dimension as the anomaly, we go from the hub to the spider and back
                        if(!spiderTookCareOfTheAnomaly) {
                            
                            StdOut.print(hub + " ");
                            visited = new boolean[col.dimensionList.length];
                            edgeTo = new DimensionNode[col.dimensionList.length];
                            breadthFirstSearch(hub, ptr.getCurrentDimension(), true, true);
                            StdOut.println();
                        }
                    } 
                    ptr = ptr.getNextProfile();
                }
            }
        }
        updateProfile(hub);
    }
    //check spider DOES NOT check if we are at the hub because we need to return a boolean here - dimensionList[i].getDimensionNum() != 928
    public void updateProfile(int hub) {
        for (int i = 0; i < profileVar.length; i++) {
            if (profileVar[i] != null) {
                Profile ptr = profileVar[i];
                while(ptr != null) {
                    ptr.setCurrentDimension(hub);
                    ptr = ptr.getNextProfile();
                }
            }
        }
    }

    public boolean checkSpider(String name, Profile[] profileVar) {
        for (int i = 0; i < profileVar.length; i++) {
            //check if there is a profile at this index
            if (profileVar[i] != null) {
                //traverse the linked list of profiles
                Profile ptr = profileVar[i];
                while (ptr != null) {
                    //check for the profile we are looking for
                    if (ptr.getName().equals(name)) {
                        //once we find the profile, we check if they are an anomaly
                        if (ptr.getDimensionSignature() == ptr.getCurrentDimension()) return true;
                        else return false;
                    }
                    //go to the next profile
                    ptr = ptr.getNextProfile();
                }
            }
        }
        return false;
    }

    private void breadthFirstSearch(int start, int finish, boolean forward, boolean reverse) {
        //get the index of the starting dimension
        int index = findFront(start, dimNodeAdjList);
        visited[index] = true;
        DimensionNode node = dimensionList[index];
        DimensionNode tempNode = new DimensionNode(node.getDimensionNum(), node.getCanonEvent(), node.getDimensionWeight(), null);
        Queue<DimensionNode> queue = new LinkedList<DimensionNode>();
        queue.add(tempNode);

        while (!queue.isEmpty()) {
            //previous node is removed and will be added to the edgeTo array later
            DimensionNode x = queue.remove();
            //set the pointed at the second node of the adjancecy list in preparation for traversal because the starting node was already printed
            int currentIndex = findFront(x.getDimensionNum(), dimensionList);
            DimensionNode ptr = dimNodeAdjList[currentIndex];
            while (ptr != null) {
                //find the index of node 
                int tempIndex = findFront(ptr.getDimensionNum(), dimensionList);
                if(!visited[tempIndex]) {
                    //if the node in the adjacency list has not been visited, add to queue, mark true in visited, and then add previous node to index of current node in adjancency list
                    queue.add(ptr);
                    visited[tempIndex] = true;
                    edgeTo[tempIndex] = x;
                }
                //traverse the Adjacency List
                ptr = ptr.getNextDimensionNode();
            }
        }
        print(start,finish, forward, reverse);
    }

    private void print(int start, int finish, boolean forward, boolean reverse) {
        Stack<DimensionNode> stack = new Stack<DimensionNode>();
        Queue<DimensionNode> q = new LinkedList<DimensionNode>();
        
        for (DimensionNode node = dimensionList[findFront(finish, dimensionList)]; node != null; node = edgeTo[findFront(node.getDimensionNum(), dimensionList)]) { 
            stack.push(node);
            q.add(node);
        }

        if (forward) {
            stack.pop();
            while (!stack.isEmpty()) {
                StdOut.print(stack.pop().getDimensionNum() + " ");
            }
        }
        if (reverse) {
            q.remove();
            while(!q.isEmpty()) {
                StdOut.print(q.remove().getDimensionNum() + " ");
            }
        }
    }
    public int findFront(int num, DimensionNode[] list) {
        for (int i = 0; i < list.length; i++) { 
            if (list[i].getDimensionNum() == num) { 
                return i; 
            }
        }
        return -1;
    }
    

    public static void main(String[] args) {
        StdOut.setFile(args[3]);
        CollectAnomalies anomaliesIWill = new CollectAnomalies();
        anomaliesIWill.collect(args[0], args[1], args[2]);
        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }
    }
}
