package forensic;

import org.w3c.dom.Node;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        // WRITE YOUR CODE HERE    
        int s = StdIn.readInt();
        STR[] str = new STR[s];
        for (int i = 0; i < s; i++) {
            String text = StdIn.readString();
            int occ = StdIn.readInt();
            STR object = new STR(text, occ);
            str[i] = object;
        }
        Profile profile = new Profile(str);
        return profile; // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {
        if (treeRoot == null) {
            treeRoot = new TreeNode(name, newProfile, null, null); 
            return;
        }
        TreeNode ptr = treeRoot;
        TreeNode prev = treeRoot;
        while (ptr != null) {
            prev = ptr;
            if (ptr.getName().compareTo(name) < 0) ptr = ptr.getRight();
            else ptr = ptr.getLeft();
        }
        TreeNode object = new TreeNode(name, newProfile, null, null);
        if (prev.getName().compareTo(name) < 0) prev.setRight(object);
        else prev.setLeft(object);
        // WRITE YOUR CODE HERE
    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    private int traverse(TreeNode x, boolean interest, int sum) {
        if(x != null) {
            if (x.getProfile().getMarkedStatus() == interest) sum++;
            sum = traverse(x.getLeft(), interest, sum);
            sum = traverse(x.getRight(), interest, sum);
        }
        return sum;
    }
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        // WRITE YOUR CODE HERE
        return traverse(treeRoot, isOfInterest, 0); // update this line
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    private void preOrder(TreeNode x) {
        if (x == null) return;
        int count = 0;
        STR[] a = x.getProfile().getStrs();
        for (int i = 0; i < a.length; i++) {
            if ((numberOfOccurrences(firstUnknownSequence, a[i].getStrString()) + numberOfOccurrences(secondUnknownSequence, a[i].getStrString())) == a[i].getOccurrences()) count++;
        }
        if (count >= Math.ceil((double)a.length/2)) x.getProfile().setInterestStatus(true); 
        
        preOrder(x.getLeft());
        preOrder(x.getRight());
    }
    public void flagProfilesOfInterest() {
        preOrder(treeRoot);
        //preorder: 
        //use treeRoot to go  inside traverse method with parameter as node, 
        //call recursively node.left(goes all the way down lefrt)
        //call recursilvely node.right(goes back up the tree and looks to the right)
        //base case: no left or right node
        // WRITE YOUR CODE HERE
    }

    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {
        Queue<TreeNode> queue = new Queue<>();
        String[] s = new String[getMatchingProfileCount(false)];
        queue.enqueue(treeRoot);
        int i = 0;
        while(!queue.isEmpty()) {
            TreeNode x = queue.dequeue();
            if(x.getProfile().getMarkedStatus() == false) {
                s[i] = x.getName();
                i++;
            }
            if (x.getLeft()!=null) queue.enqueue(x.getLeft());
            if (x.getRight()!=null) queue.enqueue(x.getRight());
        }
        // WRITE YOUR CODE HERE
        return s; // update this line
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    private TreeNode min(TreeNode y) {
        if (y.getLeft() == null) return y; 
        else return min(y.getLeft());
    }

    private TreeNode deleteMin(TreeNode z) {
        if (z.getLeft() == null) return z.getRight();
        z.setLeft(deleteMin(z.getLeft()));
        return z;
    }

    private TreeNode inOrder(TreeNode x, String inputName) {
        if (x == null) return null;
        if (x.getName().compareTo(inputName) > 0) x.setLeft(inOrder(x.getLeft(), inputName));
        else if (x.getName().compareTo(inputName) < 0) x.setRight(inOrder(x.getRight(), inputName));
        else {
            
            if(x.getRight() == null) return x.getLeft();
            if(x.getLeft() == null) return x.getRight();
            TreeNode t = x;
            x = min(t.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());
        }
        return x;
    }
    public void removePerson(String fullName) {
        // WRITE YOUR CODE HERE
        treeRoot = inOrder(treeRoot, fullName);
    }

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        String[] a = getUnmarkedPeople();
        for (int i = 0; i < a.length; i++) removePerson(a[i]);
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
