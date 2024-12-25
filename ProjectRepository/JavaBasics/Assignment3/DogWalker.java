/*
 *  
 * Write the DogWalker program inside the main method
 * according to the assignment description.
 * 
 * To compile:
 *        javac DogWalker.java
 * To execute:
 *        java DogWalker 5
 * 
 * DO NOT change the class name
 * DO NOT use System.exit()
 * DO NOT change add import statements
 * DO NOT add project statement
 * 
 *
 */
public class DogWalker {

    public static void main(String[] args) {

       // WRITE YOUR CODE HERE
    int n = Integer.parseInt(args[0]);
    int x = 0;
    int y = 0;
    double euclideanDistance;
    System.out.println("(" + x + "," + y + ")");
    
    for (int k = 0; k < n ; k++) {

        double i = Math.random();

        if (i < 0.25) {
            x = x + 1;
        } else if (i < 0.5) {
            x = x - 1;
        } else if (i < 0.75) {
            y = y + 1;
        } else {
            y = y - 1;
        }

        System.out.println("(" + x + "," + y + ")");

    }

    euclideanDistance = ((x*x)+(y*y));
    System.out.println(euclideanDistance);
    }
}
