/*
 * Write your program inside the main method to find the order
 * which the bus the student needs to take will arrive
 * according to the assignemnt description. 
 *
 * To compile:
 *        javac BusStop.java
 * 
 * DO NOT change the class name
 * DO NOT use System.exit()
 * DO NOT change add import statements
 * DO NOT add project statement
 * 
 */
public class BusStop {

    public static void main(String[] args) {

        // WRITE YOUR CODE HERE

int[] array = new int[args.length];

for (int i = 0; i <= (args.length - 1); i++) {
    array[i] = Integer.parseInt(args[i]);
}

for (int k = 0; k <= (args.length - 1); k++) {

    if (k == args.length - 1) {
        if (array [k] == array [args.length - 1]) {
        k += 1;
        System.out.println (k);
        } else {
            System.out.println ("1000");
        }
     
    }

    else if (array [k] == array [args.length - 1]) {
        k += 1;
        System.out.println (k);
        break;
    } 
    
        }
    }
}
