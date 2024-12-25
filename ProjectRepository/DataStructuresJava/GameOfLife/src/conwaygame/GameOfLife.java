package conwaygame;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        
        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j< grid[i].length; j++) {
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        int totalAliveCells = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j< grid[i].length; j++) {
                if (grid[i][j] == ALIVE) totalAliveCells++;
            }
        }
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        boolean cellState = DEAD;
        if (grid[row][col]) cellState = ALIVE;
        return cellState; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j< grid[i].length; j++) {
                if (grid[i][j]) return ALIVE;    
            }
        }
        // WRITE YOUR CODE HERE
        return DEAD; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE(
        //8 neighbors means 8 checks + past array bounds adjustments 
        int numAlive = 0;
        int tempRow = row;
        int tempCol = col;
        //top left
        tempRow--;
        tempCol--;
        if((row - 1) < 0) tempRow = grid.length - 1;
        if((col - 1) < 0) tempCol = grid[row].length - 1;
        if (grid[tempRow][tempCol]) numAlive++;
        //top (row stays same)
        tempCol = col;
        if (grid[tempRow][tempCol]) numAlive++;
        //top right (row stays same)
        tempCol = col + 1;
        if((col + 1) > (grid[row].length - 1)) tempCol = 0;
        if (grid[tempRow][tempCol]) numAlive++;
        //left
        tempRow = row;
        tempCol = col - 1;
        if((col - 1) < 0) tempCol = grid[row].length - 1;
        if (grid[tempRow][tempCol]) numAlive++;
        //right
        tempCol = col + 1;
        if((col + 1) > (grid[row].length - 1)) tempCol = 0;
        if (grid[tempRow][tempCol]) numAlive++;
        //bottom left 
        tempRow = row + 1;
        tempCol = col - 1;
        if((row + 1) > (grid.length - 1)) tempRow = 0;
        if((col - 1) < 0) tempCol = grid[row].length - 1;
        if (grid[tempRow][tempCol]) numAlive++;
        //bottom (row stays same)
        tempCol = col;
        if (grid[tempRow][tempCol]) numAlive++;
        //bottom right (row stays same)
        tempCol = col + 1;
        if((col + 1) > (grid[row].length - 1)) tempCol = 0;
        if (grid[tempRow][tempCol]) numAlive++;

        return numAlive; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        //make sure that this grid is checking the original grid, but the original grid isn't being changed
        boolean[][] newGrid= new boolean[grid.length][grid[0].length];
        for (int i = 0; i < newGrid.length; i++) {
            for (int j = 0; j < newGrid[i].length; j++) {
                if ((numOfAliveNeighbors(i, j) <= 1) && (grid[i][j] == ALIVE)) {
                    newGrid[i][j] = DEAD;
                }
                else if (numOfAliveNeighbors(i, j) == 3 && (grid[i][j]==DEAD)) {
                    newGrid[i][j] = ALIVE;
                }
                else if (numOfAliveNeighbors(i, j) == 2 && (grid[i][j]==ALIVE)) {
                    newGrid[i][j] = ALIVE;
                }
                else if (numOfAliveNeighbors(i, j) == 3 && (grid[i][j]==ALIVE)) {
                    newGrid[i][j] = ALIVE;
                }
                else if ((numOfAliveNeighbors(i, j) >= 4)  && (grid[i][j]==ALIVE)) {
                    newGrid[i][j] = DEAD;
                }
            }
        }
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        // WRITE YOUR CODE HERE
        grid = computeNewGrid();
        totalAliveCells = getTotalAliveCells();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < n; i++) nextGeneration();
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        // WRITE YOUR CODE HERE
        WeightedQuickUnionUF a = new WeightedQuickUnionUF(grid.length, grid[0].length);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                    int tempRow = i;
                    int tempCol = j;
                    //top left
                    tempRow--;
                    tempCol--;
                    if((i - 1) < 0) tempRow = grid.length - 1;
                    if((j - 1) < 0) tempCol = grid[i].length - 1;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol); 
                    //top (i stays same)
                    tempCol = j;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //top right (i stays same)
                    tempCol = j + 1;
                    if((j + 1) > (grid[i].length - 1)) tempCol = 0;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //left
                    tempRow = i;
                    tempCol = j - 1;
                    if((j - 1) < 0) tempCol = grid[i].length - 1;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //right
                    tempCol = j + 1;
                    if((j + 1) > (grid[i].length - 1)) tempCol = 0;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //bottom left 
                    tempRow = i + 1;
                    tempCol = j - 1;
                    if((i + 1) > (grid.length - 1)) tempRow = 0;
                    if((j - 1) < 0) tempCol = grid[i].length - 1;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //bottom (i stays same)
                    tempCol = j;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);
                    //bottom right (i stays same)
                    tempCol = j + 1;
                    if((j + 1) > (grid[i].length - 1)) tempCol = 0;
                    if (grid[tempRow][tempCol]) a.union(i,j,tempRow,tempCol);           
            }
        }
        ArrayList<Integer> names = new ArrayList<Integer>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j< grid[i].length; j++) {
                if (grid[i][j]) {
                    if(names.size() == 0) {
                        names.add(a.find(i,j));
                    } 
                    for (int k = 0; k < names.size(); k++) { 
                        if(!(names.get(k).equals(a.find(i,j)))) {
                            names.add(a.find(i,j));
                        } 
                    }
                } 
            }
        }
        for (int l = 0; l < names.size(); l++) {
            for(int n = 0; n < names.size(); n++) {
                if (n!=l) {
                    if (names.get(l) == names.get(n)) names.remove(n);
                }
            }
        }
        
        return names.size(); // update this line, provided so that code compiles
    }
}

