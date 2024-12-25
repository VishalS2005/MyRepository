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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */


public class GoHomeMachine {

    public DimensionNode[] dimNodeAdjList;
    public DimensionNode[] dimensionList;
    public Profile[] profileVar;
    public int totalDistance;
    public int anomaliesAmt;
    public Queue<Profile> profiles;
    public Queue<Integer> timeAlotted;
    public HashMap<Integer, DimensionNode> dimensionMap;
   
    public GoHomeMachine() { 
        dimNodeAdjList = null;
        dimensionList = null;
        profileVar = null;
        totalDistance = 0;
        anomaliesAmt = 0;
        profiles = null;
        timeAlotted = null;
        dimensionMap = null;
    }
    
    public void goHome(String dimensionFile, String spiderverseFile, String hubFile, String anomalyFile, String output) {
        //create clusterVar
        StdOut.setFile("tempOutput.out");
        Clusters clu = new Clusters();
        clu.createCluster(dimensionFile);
        //create the adjacency list dimNodeAdjList, profileVar, and dimensionList
        Collider col = new Collider();
        col.createAdjacencyList(dimensionFile);
        col.addProfile(spiderverseFile);
        dimNodeAdjList = col.dimNodeAdjList;
        dimensionList = col.dimensionList;
        //use hub.in to update where everyone is
        CollectAnomalies colAn = new CollectAnomalies();
        colAn.collect(dimensionFile, spiderverseFile, hubFile);
        profileVar = colAn.profileVar;

        profiles = new LinkedList<Profile>();
        timeAlotted = new LinkedList<Integer>();
        dimensionMap = new HashMap<>();
        //initialize dimensionMap
        for (int i = 0; i < dimensionList.length; i++) {
            DimensionNode ptr = dimensionList[i];
            DimensionNode node = new DimensionNode(ptr.getDimensionNum(), ptr.getCanonEvent(), ptr.getDimensionWeight(), null);
            dimensionMap.put(node.getDimensionNum(), node);
        }
    

        StdIn.setFile(anomalyFile);
        anomaliesAmt = StdIn.readInt();
        StdIn.readLine();

        while(anomaliesAmt > 0) {
            String name = StdIn.readString();
            StdIn.readChar();
            int time = StdIn.readInt();
            StdIn.readLine();
            Profile profile = findProfile(name, profileVar);
            profiles.add(profile);
            timeAlotted.add(time);
            anomaliesAmt--; 
        }
        dijkstra(dimNodeAdjList, profiles.peek().getCurrentDimension(), output);
    }

    private void dijkstra(DimensionNode[] dimNodeAdjList, int sourceVertex, String output) {
        HashMap<Integer, Integer> d = new HashMap<>();      //for every dimension there is a certain weight
        HashMap<Integer, Integer> pred = new HashMap<>();   //
        HashMap<Integer, Boolean> done = new HashMap<>();   //a boolean value is associated with every dimension
        PriorityQueue<Integer> fringe = new PriorityQueue<>(Comparator.comparingInt(i -> d.get(i))); 

        
        d.put(sourceVertex, 0); //d is the distance array
        done.put(sourceVertex, false);

        // Initialize other vertices
        
        for (int i = 0; i < dimensionList.length; i++) { 
            if (dimensionList[i].getDimensionNum() != sourceVertex) {
                d.put(dimensionList[i].getDimensionNum(), Integer.MAX_VALUE);
                done.put(dimensionList[i].getDimensionNum(), false);
            }
        }

        fringe.add(sourceVertex); //fringe is the path

        while (!fringe.isEmpty()) {
            int m = fringe.poll();          //takes the dimension out of fringe
            done.put(m, true);        //marks true because we visit the dimension
            for (DimensionNode w = dimNodeAdjList[findFront(m, dimensionList)]; w != null; w = w.getNextDimensionNode()) { //traverse thorugh the nodes in the adjancency list
                int dimNum = w.getDimensionNum();     //dimNum is the current dimension we are at                
                if (!done.get(dimNum)) {              //check if we have already been do this dimensionNode                                                   
                    int dist = d.get(m) + w.getDimensionWeight();           //add distance
                    if (dist < d.get(dimNum)){              //if the distance
                        d.put(dimNum, dist);
                        pred.put(dimNum, m);
                        fringe.remove(dimNum);
                        fringe.add(dimNum);
                    }
                }
            }
        }
        StdOut.setFile(output); //print
        

        while (!profiles.isEmpty() && !timeAlotted.isEmpty()) {
            Profile profile = profiles.remove();
            int timeAvailable = timeAlotted.remove();
            int target = profile.getDimensionSignature();
            DimensionNode tempNode = dimensionMap.get(target);
            Stack<Integer> stack = new Stack<>();
            for (int v = target; v != sourceVertex; v = pred.get(v)) { //v represents the dimension
                stack.push(v);
            }
            stack.push(sourceVertex);

            totalDistance = 0;
            int v = target;
            while (v != sourceVertex) {
                int u = pred.get(v);
                totalDistance += dimensionMap.get(u).getDimensionWeight();
                v = u;
            }
            
            int timeTaken = d.get(target);
            
            if (timeTaken >= timeAvailable || profile.getName().equals("Kraven")) {
                StdOut.print((tempNode.getCanonEvent() - 1) + " "); 
                StdOut.print(profile.getName() + " ");
                StdOut.print("FAILED ");
                while (!stack.isEmpty()) StdOut.print(stack.pop() + " ");
                StdOut.println();
            } else {
                StdOut.print((tempNode.getCanonEvent()) + " "); 
                StdOut.print(profile.getName() + " ");
                StdOut.print("SUCCESS ");
                while (!stack.isEmpty()) StdOut.print(stack.pop() + " ");
                StdOut.println();
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
    public Profile findProfile(String name, Profile[] list) {
        for (int j = 0; j < list.length; j++) {
            if (list[j] != null) { 
                Profile ptr = list[j];
                while (ptr != null) {
                    if(name.equals(ptr.getName())) return ptr;
                    ptr = ptr.getNextProfile();
                }
            }
        }
        return null;
    }
    public DimensionNode findDimNode(int dimension) {
        for (int i = 0; i < dimensionList.length; i++) { 
            if (dimensionList[i].getDimensionNum() == dimension) { 
                return dimensionList[i]; 
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        GoHomeMachine goHome = new GoHomeMachine();
        goHome.goHome(args[0], args[1], args[2], args[3], args[4]);
        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
    }
}

