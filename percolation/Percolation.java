import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private final WeightedQuickUnionUF gridUF;
    private final int height;
    private final int width;
    private final int topNodeId;
    private final int btmNodeId;
    private int numOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be a positive integer!");

        // grid tracks open/blocked status
        grid = new int[n][n];
        height = n;
        width = n;
        numOpen = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = 0;
            }
        }
        // gridUF tracks dynamic connectivity
        gridUF = new WeightedQuickUnionUF(n * n + 2);
        topNodeId = n * n;
        btmNodeId = n * n + 1;
    }

    // Helper function to get id based on row and col
    private int getId(int row, int col) {
        return width * (row - 1) + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > height || col > width)
            throw new IllegalArgumentException("row/col input is out of bound!");

        if (isOpen(row, col)) return;
        grid[row - 1][col - 1] = 1;
        numOpen++;

        // connect top, left, btm, right neighbours
        if (row - 1 >= 1 && isOpen(row - 1, col)) gridUF.union(getId(row, col), getId(row - 1, col));
        if (col - 1 >= 1 && isOpen(row, col - 1)) gridUF.union(getId(row, col), getId(row, col - 1));
        if (row + 1 <= height && isOpen(row + 1, col)) gridUF.union(getId(row, col), getId(row + 1, col));
        if (col + 1 <= width && isOpen(row, col + 1)) gridUF.union(getId(row, col), getId(row, col + 1));

        // connect to virtual top node & virtual btm node
        if (row == 1) gridUF.union(getId(row, col), topNodeId);
        if (row == height) gridUF.union(getId(row, col), btmNodeId);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > height || col > width)
            throw new IllegalArgumentException("row/col input is out of bound!");
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > height || col > width)
            throw new IllegalArgumentException("row/col input is out of bound!");
        return gridUF.find(getId(row, col)) == gridUF.find(topNodeId);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return gridUF.find(topNodeId) == gridUF.find(btmNodeId);
    }

    // test client (optional)
//    public static void main(String[] args) {
//    }
}
