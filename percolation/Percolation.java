import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private boolean[] sites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufForIsFull;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        sites = new boolean[n * n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufForIsFull = new WeightedQuickUnionUF(n * n + 2);
        unionTopVirtualSite(uf);
        unionBottomVirtualSite(uf);
        unionTopVirtualSite(ufForIsFull);
    }

    private void unionTopVirtualSite(WeightedQuickUnionUF wquf) {
        for (int i = 1; i <= size; i++) {
            wquf.union(0, i);
        }
    }

    private void unionBottomVirtualSite(WeightedQuickUnionUF wquf) {
        for (int i = size * (size - 1) + 1; i <= size * size; i++) {
            wquf.union(size * size + 1, i);
        }
    }

    private boolean valid(int n) {
        return 1 <= n && n <= size;
    }

    private int coordToIndex(int row, int col) {
        return (row - 1) * size + col;
    }

    private int[][] getAdjacentSites(int row, int col) {
        return new int[][] {
                { row, col - 1 }, { row, col + 1 }, { row - 1, col }, { row + 1, col }
        };
    }

    public void open(int row, int col) {
        if (!valid(row) || !valid(col)) {
            throw new IllegalArgumentException();
        }

        int index = coordToIndex(row, col);
        sites[index] = true;

        for (int[] site : getAdjacentSites(row, col)) {
            if (!valid(site[0]) || !valid(site[1])) {
                continue;
            }

            try {
                if (isOpen(site[0], site[1])) {
                    uf.union(index, coordToIndex(site[0], site[1]));
                    ufForIsFull.union(index, coordToIndex(site[0], site[1]));
                }
            }
            catch (IllegalArgumentException e) {
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!valid(row) || !valid(col)) {
            throw new IllegalArgumentException();
        }

        return sites[coordToIndex(row, col)];
    }

    public int numberOfOpenSites() {
        int number = 0;

        for (int i = 0; i < sites.length; i++) {
            if (sites[i]) {
                number += 1;
            }
        }

        return number;
    }

    public boolean percolates() {
        if (sites.length == 3) {
            return sites[1];
        }

        return uf.connected(0, size * size + 1);
    }

    public boolean isFull(int row, int col) {
        if (!valid(row) || !valid(col)) {
            throw new IllegalArgumentException();
        }

        return ufForIsFull.connected(0, coordToIndex(row, col)) && isOpen(row, col);
    }

    public static void main(String[] args) {

    }
}
