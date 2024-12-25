public class StaircaseBuilder {
    public static void main(String[] args) {
        int d = Integer.parseInt(args[0]);
        int bricks = Integer.parseInt(args[1]);
        char[][] staircase = new char[d][d];

        for (int i = 0; i < d; i++) {
            for (int f = 0; f < d; f++) {

                staircase[i][f] = ' ';

            }
        }

        for (int column = 0; d>column && bricks>0; column++) { //leftmost column iterating to the right while bricks is 0
            for (int row = (d - 1); row >= 0 && bricks > 0; row--) {    //bottom row interating up
 
                if (row >= (d - column - 1)) {
                    staircase[row][column] = 'X';
                    bricks--;
                }
            }
        }

        for (int n = 0; n < d; n++) {
            for (int k = 0; k < d; k++) {

                System.out.print(staircase[n][k]);

            }

            System.out.println();

        }

        System.out.println("Bricks remaining: " + bricks);

    }
}
