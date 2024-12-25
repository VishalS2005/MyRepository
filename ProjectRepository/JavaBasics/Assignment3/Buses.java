/*
 *
 * Write the Buses program inside the main method
 * according to the assignment description.
 * 
 * To compile:
 *        javac Buses.java
 * To execute:
 *        java Buses 7302
 * 
 * DO NOT change the class name
 * DO NOT use System.exit()
 * DO NOT change add import statements
 * DO NOT add project statement
 * 
 */

public class Buses {
    public static void main(String[] args) {

        // WRITE YOUR CODE HERE
        int busNum = Integer.parseInt(args[0]);

        if (busNum<0) {

            System.out.print ("ERROR");
            
        } 

        else {
            int num4 = (((busNum % 1000) % 100) % 10);
            int num3 = ((((busNum % 1000) % 100) - num4) / 10);
            int num2 = (((busNum % 1000) - (num4 + num3)) /100);
            int num1 = ((busNum - (num2 + num3 + num4)) /1000);
            int total = num1 + num2 + num3 + num4;
            if ((total % 2) == 0) {
                System.out.print("LX");
            }
            else {
                System.out.print("H");
            }
        }    
    }
}
