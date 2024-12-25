package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Menu {
    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        
        String inputFile = args[0];
        String outputFile = args[1];

        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();

	// 3. Call the menu() method to read the menu

        rh.menu("menu.in");
        rh.createStockHashTable("stock.in");
        rh.updatePriceAndProfit();

        StdIn.setFile("order1.in");
        int temp = StdIn.readInt();
        StdIn.readLine();
        while(temp > 0) {
                int amount = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();
                rh.order(item, amount);
                temp--;
        }
        StdIn.setFile(inputFile);
        int size = StdIn.readInt();
        StdIn.readLine();
        while (size > 0) {
                int amount = StdIn.readInt();
                StdIn.readChar();
                String item = StdIn.readLine();
                rh.restock(item, amount);
                size--;
        }


	// 4. Set output file
	// Option to remove this line if you want to print directly to the screen
        StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();
    }
}
