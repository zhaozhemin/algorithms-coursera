import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class Node implements Comparable<Node> {
        private Board initial;
        private int moves;
        private Node prev;
        private int priority;

        public Node(Board initial, int moves, Node prev) {
            this.initial = initial;
            this.prev = prev;
            this.moves = moves;
            priority = initial.manhattan() + moves;
        }

        public int compareTo(Node that) {
            if (priority < that.priority) {
                return -1;
            }
            else if (priority == that.priority) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    private Node goalNode;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        twinPQ.insert(new Node(initial.twin(), 0, null));
        solvable = false;

        while (true) {
            Node min = pq.delMin();
            Node twinMin = twinPQ.delMin();

            if (min.initial.isGoal()) {
                goalNode = min;
                solvable = true;
                break;
            }

            if (twinMin.initial.isGoal()) {
                break;
            }

            for (Board i : min.initial.neighbors()) {
                if (min.prev == null || !i.equals(min.prev.initial)) {
                    pq.insert(new Node(i, min.moves + 1, min));
                }
            }

            for (Board i : twinMin.initial.neighbors()) {
                if (twinMin.prev == null || !i.equals(twinMin.prev.initial)) {
                    twinPQ.insert(new Node(i, twinMin.moves + 1, twinMin));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (goalNode == null) {
            return -1;
        }
        return goalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Node curr = goalNode;
        Stack<Board> stack = new Stack<>();

        while (curr != null) {
            stack.push(curr.initial);

            if (curr.prev == null) {
                break;
            }

            curr = curr.prev;
        }

        return stack.isEmpty() ? null : stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
