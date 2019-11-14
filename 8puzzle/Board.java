import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private int[][] tiles;
    private int n;
    private int manhattanDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = copyTiles(tiles);
        manhattanDistance = -1;
    }

    // string representation of this board
    // copied from the faq page
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    private int positionToValue(int x, int y) {
        if (x == y && x == n - 1) {
            return 0;
        }
        else {
            return x * n + y + 1;
        }
    }

    private int[] valueToPosition(int val) {
        int row = (val - 1) / n;
        int col = (val - 1) % n;
        return new int[] { row, col };
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == 0) {
                    continue;
                }

                if (positionToValue(x, y) != tiles[x][y]) {
                    count += 1;
                }
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance != -1) {
            return manhattanDistance;
        }

        int distance = 0;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == 0) {
                    continue;
                }

                int[] position = valueToPosition(tiles[x][y]);
                distance += Math.abs(position[0] - x) + Math.abs(position[1] - y);
            }
        }

        manhattanDistance = distance;
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // If the board doesn't have zero and all the other numbers are
        // in right position, for example:
        //
        // 1 2 3
        // 4 5 6
        // 7 8 9
        //
        // this implementation of mahanttan() will return 0, which is wrong.
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    private int[][] copyTiles(int[][] src) {
        int[][] copy = new int[n][];

        for (int i = 0; i < n; i++) {
            copy[i] = Arrays.copyOf(src[i], src[i].length);
        }

        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        int row = -1;
        int col = -1;

        outer:
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == 0) {
                    row = x;
                    col = y;
                    break outer;
                }
            }
        }

        if (row == -1) {
            return stack;
        }

        if (col != 0) {
            int[][] copy = copyTiles(tiles);
            copy[row][col] = copy[row][col - 1];
            copy[row][col - 1] = 0;
            stack.push(new Board(copy));
        }

        if (col != n - 1) {
            int[][] copy = copyTiles(tiles);
            copy[row][col] = copy[row][col + 1];
            copy[row][col + 1] = 0;
            stack.push(new Board(copy));
        }

        if (row != 0) {
            int[][] copy = copyTiles(tiles);
            copy[row][col] = copy[row - 1][col];
            copy[row - 1][col] = 0;
            stack.push(new Board(copy));
        }

        if (row != n - 1) {
            int[][] copy = copyTiles(tiles);
            copy[row][col] = copy[row + 1][col];
            copy[row + 1][col] = 0;
            stack.push(new Board(copy));
        }

        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x0 = -1;
        int y0 = -1;
        int x1;
        int y1;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (tiles[x][y] == 0) {
                    x0 = x;
                    y0 = y;
                    break;
                }
            }
        }

        if (x0 == 0) {
            x1 = 1;
        }
        else {
            x1 = x0 - 1;
        }

        if (y0 == n - 1) {
            y1 = y0 - 1;
        }
        else {
            y1 = y0;
        }

        int[][] copy = copyTiles(tiles);
        int swap = copy[x1][y1];
        copy[x1][y1] = copy[x1][y1 + 1];
        copy[x1][y1 + 1] = swap;

        return new Board(copy);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
