import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.List;

public class SP {

    private int width;
    private int height;
    private int[] edgeTo;
    private double[] distTo;
    private boolean[] marked;
    private Queue<Integer> postorder;
    private double[][] energyArray;

    public SP(double[][] energyArray) {
        width = energyArray[0].length;
        height = energyArray.length;
        edgeTo = new int[width * height];
        distTo = new double[width * height];
        marked = new boolean[width * height];
        postorder = new Queue<>();
        this.energyArray = energyArray;

        for (int i = 0; i < width * height; i++) {
            if (!marked[i]) {
                dfs(i);
            }
        }

        for (int i = 0; i < width * height; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < width; i++) {
            distTo[i] = 0.0;
        }


        for (int i : topological()) {
            int[] coord = indexToCoord(i);
            for (int[] pair : getAdjacentPixel(coord[0], coord[1])) {
                int to = coordToIndex(pair[0], pair[1]);
                relax(i, to);
            }
        }

        // Why is this implementation much slower?

        // Queue<Integer> q = new Queue<>();
        //
        // for (int i = 0; i < width; i++) {
        //     distTo[i] = 0.0;
        //     q.enqueue(i);
        // }
        //
        // while (!q.isEmpty()) {
        //     int from = q.dequeue();
        //     int[] coord = indexToCoord(from);
        //     for (int[] pair : getAdjacentPixel(coord[0], coord[1])) {
        //         // StdOut.println(from + "->" + pair[0] + "," + pair[1]);
        //         int to = coordToIndex(pair[0], pair[1]);
        //         relax(from, to);
        //         q.enqueue(to);
        //     }
        // }
    }

    private void relax(int v, int e) {
        int[] coord = indexToCoord(e);
        int row = coord[0];
        int col = coord[1];
        if (distTo[e] > distTo[v] + energyArray[row][col]) {
            distTo[e] = distTo[v] + energyArray[row][col];
            edgeTo[e] = v;
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);

        if (!hasPathTo(v)) {
            return null;
        }

        int w = v;
        Stack<Integer> path = new Stack<>();

        while (edgeTo[w] != 0) {
            path.push(w);
            w = edgeTo[w];
        }

        path.push(w);

        return path;
    }

    public Iterable<Integer> shortestPathToBottom() {
        int start = width * height - width;
        double distanceMin = Double.POSITIVE_INFINITY;
        int destination = -1;

        for (int i = start; i < width * height; i++) {
            if (distTo(i) < distanceMin) {
                distanceMin = distTo(i);
                destination = i;
            }
        }

        return pathTo(destination);
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void dfs(int v) {
        marked[v] = true;
        int[] coord = indexToCoord(v);
        for (int[] pair : getAdjacentPixel(coord[0], coord[1])) {
            int to = coordToIndex(pair[0], pair[1]);
            if (!marked[to]) {
                dfs(to);
            }
        }
        postorder.enqueue(v);
    }

    private Iterable<Integer> topological() {
        Stack<Integer> reverse = new Stack<>();
        for (int v : postorder) {
            reverse.push(v);
        }
        return reverse;
    }

    private int coordToIndex(int row, int col) {
        return row * width + col;
    }

    private int[] indexToCoord(int i) {
        int row = i / width;
        int col = i % width;
        return new int[] { row, col };
    }

    private List<int[]> getAdjacentPixel(int row, int col) {
        List<int[]> coordList = new ArrayList<>();

        for (int i : new int[] { col - 1, col, col + 1 }) {
            if (validCoordinateRow(row + 1) && validCoordinateCol(i)) {
                coordList.add(new int[] { row + 1, i });
            }
        }

        return coordList;
    }

    private boolean validCoordinateCol(int col) {
        return col >= 0 && col < width;
    }

    private boolean validCoordinateRow(int row) {
        return row >= 0 && row < height;
    }

    public static void main(String[] args) {
    }
}
