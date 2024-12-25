/*************************************************************************
 *  Compilation:  javac RecursiveAppend.java
 *  Execution:    java RecursiveAppend
 *
 *************************************************************************/

public class RecursiveAppend {

    /*
     * Returns the orginal string appended n times 
     *
     * This method does not print ANYTHING
     */
    public static String appendNTimes (String original, int n) {

	// WRITE YOUR CODE HERE
        if (n==0) {
            return original;
        } else {
            return appendNTimes(original, n-1) + appendNTimes(original, 0);
        }
    }
    
    /*
     * Test client
     */
    public static void main (String[] args) {

	System.out.println(appendNTimes("cat", 0));
	System.out.println(appendNTimes("cat", 1));
	System.out.println(appendNTimes("cat", 2));
	
    }
}