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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    
    public boolean[] visited;

    public TrackSpot() { visited = null; }

    private boolean found = false;

    public void findSpot(String dimensionFile, String spiderverseFile, String spotFile) {
        //initialize everything needed before the depth-first search traversal
        Clusters clu = new Clusters();
        clu.createCluster(dimensionFile);
        Collider col  = new Collider();
        col.createAdjacencyList(dimensionFile);
        DimensionNode[] dimNodeAdjList = col.dimNodeAdjList;
        DimensionNode[] dimList = col.dimensionList;
        visited = new boolean[clu.dimensionAmount];
        StdIn.setFile(spotFile);
        int start = StdIn.readInt();
        StdOut.print(start + " ");
        StdIn.readLine();
        int finish = StdIn.readInt();
        //once everything has been initialized, move on to the depth-first search recursive as done in recitation
        dfs(start, finish, dimNodeAdjList, dimList);

    }

    private void dfs(int start, int finish, DimensionNode[] dimNodeAdjList, DimensionNode[] dimList) {
        if (found) return;
        int index = findFront(start, dimList);
        visited[index] = true;
        if (start == finish) { 
            found = true;
            return; 
        } 
        for (DimensionNode ptr = dimNodeAdjList[index]; ptr != null; ptr = ptr.getNextDimensionNode()) {
            int temp = findFront(ptr.getDimensionNum(), dimList);
            if (!visited[temp] && !found) {
                visited[temp] = true;
                StdOut.print(ptr.getDimensionNum() + " ");
                dfs(ptr.getDimensionNum(), finish, dimNodeAdjList, dimList);
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
        TrackSpot ts = new TrackSpot();
        StdOut.setFile(args[3]);
        ts.findSpot(args[0], args[1], args[2]);
        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
    }
}
