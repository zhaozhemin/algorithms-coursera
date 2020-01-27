import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private String s;
    private CircularSuffix[] suffixes;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private String origStr;
        private int start;

        public CircularSuffix(String origStr, int start) {
            this.origStr = origStr;
            this.start = start;
        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < origStr.length(); i++) {
                int thisStart = i + this.start;

                if (thisStart >= origStr.length()) {
                    thisStart = thisStart - origStr.length();
                }

                int thatStart = i + that.start;

                if (thatStart >= origStr.length()) {
                    thatStart = thatStart - origStr.length();
                }

                if (this.origStr.charAt(thisStart) < that.origStr.charAt(thatStart)) {
                    return -1;
                }
                else if (this.origStr.charAt(thisStart) > that.origStr.charAt(thatStart)) {
                    return 1;
                }
            }

            return 0;
        }

        public void printString() {
            for (int i = start; i < s.length(); i++) {
                StdOut.print(s.charAt(i));
            }
            for (int i = 0; i < start; i++) {
                StdOut.print(s.charAt(i));
            }
            StdOut.println();
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        this.s = s;
        suffixes = new CircularSuffix[s.length()];

        for (int i = 0; i < suffixes.length; i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }

        Arrays.sort(suffixes);
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > suffixes.length - 1) {
            throw new IllegalArgumentException();
        }
        return suffixes[i].start;
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("abracadabra!");
        StdOut.println(circularSuffixArray.length());
        StdOut.println(circularSuffixArray.index(0));
    }
}
