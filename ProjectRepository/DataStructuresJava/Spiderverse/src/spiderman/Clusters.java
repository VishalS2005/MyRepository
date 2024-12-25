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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    public DimensionNode[] clusterVar;
    public int dimensionAmount;


    public Clusters() {
        clusterVar = null;
        dimensionAmount = 0;
    }

    public DimensionNode[] getClusterVar() { return clusterVar; }
    public int getDimensionAmount() { return dimensionAmount; }


    //create a hash table with clusters from the input file


    public void createCluster(String fileName) {
        StdIn.setFile(fileName);
        int a = StdIn.readInt(); //lines in input file (dimensions)
        int b = StdIn.readInt(); //initial size of the array clusterVar
        double c = StdIn.readDouble(); //double variable that represents the weight
        clusterVar = new DimensionNode[b]; //initialize instance variable
        
        while (a > 0) {
            int dimensionNum = StdIn.readInt();     //dimension number
            int canonEvent = StdIn.readInt();       //dimension amount of canon events
            int dimensionWeight = StdIn.readInt();  //dimension weight
            DimensionNode tempNode = new DimensionNode(dimensionNum, canonEvent, dimensionWeight, null);
            int hashIndex = ((dimensionNum) % (clusterVar.length));

            dimensionAmount++;
            if (((dimensionAmount) / (clusterVar.length)) >= c) { 
                if (clusterVar[hashIndex] == null) clusterVar[hashIndex] = tempNode;            
                else {
                    tempNode.setNextDimensionNode(clusterVar[hashIndex]);      // 2 3 7 5      //n nodes and weight 4(c)             
                    clusterVar[hashIndex] = tempNode;   
                } 
                rehash();    
            } else { //no rehash
                //check if there are any nodes and then adds to front of linked list
                if (clusterVar[hashIndex] == null) clusterVar[hashIndex] = tempNode;            
                else {
                    //adds the node form the queue into front of linked list
                    tempNode.setNextDimensionNode(clusterVar[hashIndex]);                       
                    clusterVar[hashIndex] = tempNode;   
                }
            }
            a--;
        }


        //create the connections between connections
        //create pointer and previous and loop through each one for the connections
        //start with index 0 and 1 and create a separate loop for those if there are that many in the clusterVar

        DimensionNode tempPtr = clusterVar[1];                            //at index 1 implement the wrap around case
        while(tempPtr.getNextDimensionNode() != null)  tempPtr = tempPtr.getNextDimensionNode();     //go to the end of the linked list
        
        DimensionNode superNode1 = new DimensionNode(clusterVar[0].getDimensionNum(),      //create a copy of the dimensionNode front at 0
                                                    clusterVar[0].getCanonEvent(), 
                                                    clusterVar[0].getDimensionWeight(), 
                                                    null);
        tempPtr.setNextDimensionNode(superNode1);                     //add to the end of the linked list from the previous cluster
        tempPtr = tempPtr.getNextDimensionNode();                     //go to the end of the linked list so that another node can be added
        DimensionNode superNode2 = new DimensionNode(clusterVar[clusterVar.length - 1].getDimensionNum(),     //create a copy of the dimensionNode front at last
                                                    clusterVar[clusterVar.length - 1].getCanonEvent(), 
                                                    clusterVar[clusterVar.length - 1].getDimensionWeight(), 
                                                    null);
        tempPtr.setNextDimensionNode(superNode2);                      //add to the end of the linked list from the last cluster



        tempPtr = clusterVar[0];                            //at index 0 implement the wrap around case
        while(tempPtr.getNextDimensionNode() != null) tempPtr = tempPtr.getNextDimensionNode();     //go to the end of the linked list
        
        DimensionNode superNode3 = new DimensionNode(clusterVar[clusterVar.length - 1].getDimensionNum(),     //create a copy of the dimensionNode front at last
                                                    clusterVar[clusterVar.length - 1].getCanonEvent(), 
                                                    clusterVar[clusterVar.length - 1].getDimensionWeight(), 
                                                    null);
        tempPtr.setNextDimensionNode(superNode3);                     //add to the end of the linked list from the previous cluster
        tempPtr = tempPtr.getNextDimensionNode();                     //go to the end of the linked list so that another node can be added
        DimensionNode superNode4 = new DimensionNode(clusterVar[clusterVar.length - 2].getDimensionNum(),     //create a copy of the dimensionNode front at second to last
                                                    clusterVar[clusterVar.length - 2].getCanonEvent(), 
                                                    clusterVar[clusterVar.length - 2].getDimensionWeight(), 
                                                    null);
        tempPtr.setNextDimensionNode(superNode4);                      //add to the end of the linked list from the last cluster
        

        for (int i = 2; i < clusterVar.length; i++) {
            tempPtr = clusterVar[i];                            //at index i implement the wrap around case
            while(tempPtr.getNextDimensionNode() != null) tempPtr = tempPtr.getNextDimensionNode();     //go to the end of the linked list
            
            DimensionNode superTemp1 = new DimensionNode(clusterVar[i - 1].getDimensionNum(),     //create a copy of the dimensionNode front at i - 1
                                                        clusterVar[i - 1].getCanonEvent(), 
                                                        clusterVar[i - 1].getDimensionWeight(), 
                                                        null);
            tempPtr.setNextDimensionNode(superTemp1);                     //add to the end of the linked list from the previous cluster
            tempPtr = tempPtr.getNextDimensionNode();                     //go to the end of the linked list so that another node can be added
            DimensionNode superTemp2 = new DimensionNode(clusterVar[i - 2].getDimensionNum(),     //create a copy of the dimensionNode front at i - 2
                                                        clusterVar[i - 2].getCanonEvent(), 
                                                        clusterVar[i - 2].getDimensionWeight(), 
                                                        null);
            tempPtr.setNextDimensionNode(superTemp2);                      //add to the end of the linked list from the two clusters back
        }
    }
    
    public void print() {
        for (int i = 0; i < clusterVar.length; i++) {
            DimensionNode ptr = clusterVar[i];
            while (ptr != null) { 
                StdOut.print(ptr.getDimensionNum() + " "); 
                ptr = ptr.getNextDimensionNode(); 
            }
            StdOut.println();
        }
        StdOut.println();
    }
    
    public void rehash() {
        //rehash
        //check if there are any nodes and then adds to front of linked list
        DimensionNode[] clusterVarCopy = new DimensionNode[clusterVar.length * 2];
        //go through each index of clusterVar and traverse the linked list
        for (int i = 0; i < clusterVar.length; i++) { 
            DimensionNode ptr = clusterVar[i];
            while(ptr != null) {                      //add each dimension to a queue
                DimensionNode rehashedNode = new DimensionNode(ptr.getDimensionNum(), ptr.getCanonEvent(), ptr.getDimensionWeight(), null);
                int tempIndex = (rehashedNode.getDimensionNum() % clusterVarCopy.length);
                //check if there are any nodes and then adds to front of linked list
                if (clusterVarCopy[tempIndex] == null) clusterVarCopy[tempIndex] = rehashedNode;            
                else {
                    //adds the node from the queue into front of linked list
                    rehashedNode.setNextDimensionNode(clusterVarCopy[tempIndex]);                       
                    clusterVarCopy[tempIndex] = rehashedNode;   
                }
                ptr = ptr.getNextDimensionNode();
            }   
        }
        clusterVar = clusterVarCopy;
    }
   public static void main(String[] args) {
        Clusters cl = new Clusters();
        cl.createCluster(args[0]);
        StdOut.setFile(args[1]);
        cl.print();
        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }
        
        // WRITE YOUR CODE HERE

    }
}
