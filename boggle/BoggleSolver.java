import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.List;

public class BoggleSolver {

    private TST<Integer> tst;
    private BoggleBoard boggleBoard;

    public BoggleSolver(String[] dictionary) {
        int count = 0;
        tst = new TST<>();
        for (String i : dictionary) {
            tst.put(i, count);
            count += 1;
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boggleBoard = board;
        Stack<List<int[]>> stack = new Stack<>();
        TST<Integer> seen = new TST<>();
        int count = 0;

        for (int x = 0; x < board.rows(); x++) {
            for (int y = 0; y < board.cols(); y++) {
                List<int[]> coord = new ArrayList<>();
                coord.add(new int[] { x, y });
                stack.push(coord);
            }
        }

        while (!stack.isEmpty()) {
            List<int[]> coord = stack.pop();
            String word = getWord(coord);

            if (word.length() < 3) {
                for (List<int[]> x : nextCoords(coord)) {
                    stack.push(x);
                }
                continue;
            }

            for (String i : tst.keysWithPrefix(word)) {
                if (tst.contains(word) && !seen.contains(word)) {
                    seen.put(word, count);
                    count += 1;
                }

                for (List<int[]> x : nextCoords(coord)) {
                    stack.push(x);
                }

                break;
            }
        }

        return seen.keys();
    }

    private String getWord(List<int[]> coords) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int[] i : coords) {
            int x = i[0];
            int y = i[1];
            char letter = boggleBoard.getLetter(x, y);
            if (letter == 'Q') {
                stringBuilder.append("QU");
            }
            else {
                stringBuilder.append(letter);
            }
        }

        return stringBuilder.toString();
    }

    public int scoreOf(String word) {
        if (!tst.contains(word)) {
            return 0;
        }

        int len = word.length();

        if (len == 3 || len == 4) {
            return 1;
        }
        else if (len == 5) {
            return 2;
        }
        else if (len == 6) {
            return 3;
        }
        else if (len == 7) {
            return 5;
        }
        else if (len >= 8) {
            return 11;
        }
        else {
            return 0;
        }
    }

    private Iterable<List<int[]>> nextCoords(List<int[]> coords) {
        int[] tail = coords.get(coords.size() - 1);
        int x = tail[0];
        int y = tail[1];
        Stack<List<int[]>> stack = new Stack<>();

        for (int[] i : new int[][] {
                { x, y + 1 }, { x, y - 1 }, { x + 1, y }, { x - 1, y }, { x - 1, y - 1 },
                { x + 1, y + 1 }, { x - 1, y + 1 }, { x + 1, y - 1 }
        }) {
            if (coordWithinBound(i) && coordNotDuplicate(coords, i)) {
                List<int[]> copy = new ArrayList<>(coords);
                copy.add(i);
                stack.push(copy);
            }
        }

        return stack;
    }

    private boolean coordWithinBound(int[] coord) {
        int x = coord[0];
        int y = coord[1];
        return x >= 0 && x < boggleBoard.rows() && y >= 0 && y < boggleBoard.cols();
    }

    private boolean coordNotDuplicate(List<int[]> coords, int[] coord) {
        for (int[] i : coords) {
            if (i[0] == coord[0] && i[1] == coord[1]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            // StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
