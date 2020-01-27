import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < s.length(); i++) {
            int index = circularSuffixArray.index(i);
            if (index == 0) {
                BinaryStdOut.write(s.charAt(s.length() - 1));
            }
            else {
                BinaryStdOut.write(s.charAt(index - 1));
            }
        }
        BinaryStdOut.flush();
    }

    private void printArr(int[] arr) {
        for (int i : arr) {
            StdOut.println(i);
        }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        List<Integer> chars = new ArrayList<>();
        Map<Integer, Queue<Integer>> charPosition = new HashMap<>();
        int currentIndex = 0;

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(8);
            chars.add(i);
            Queue<Integer> position = charPosition.get(i);

            if (position == null) {
                position = new Queue<>();
                charPosition.put(i, position);
            }

            position.enqueue(currentIndex);
            currentIndex += 1;
        }

        int N = chars.size();
        int R = 256;
        int[] count = new int[R + 1];

        for (int i = 0; i < N; i++) {
            count[chars.get(i) + 1]++;
        }

        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        int[] h = new int[N];

        for (int i = 0; i < N; i++) {
            h[count[chars.get(i)]++] = chars.get(i);
        }

        int[] next = new int[N];

        for (int i = 0; i < N; i++) {
            int index = charPosition.get(h[i]).dequeue();
            next[i] = index;
        }

        int current = first;

        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(h[current], 8);
            current = next[current];
        }

        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
