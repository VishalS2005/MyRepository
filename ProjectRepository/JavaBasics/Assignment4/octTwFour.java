public class octTwFour{
    public static void main(String[] args){
        
        int x = Integer.parseInt(args [0]);
       
        multiplicationTable(x);
    }
    public static void multiplicationTable (int x){
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= x; j++) {
                int product = i * j;
                System.out.print(product + " ");
            }
            System.out.println();
        }
    }
    public static boolean isPrime(int n){
        
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
            
        }
        return true;
    }
}