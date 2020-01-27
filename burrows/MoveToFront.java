import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static LinkedList<Character> initAsciiList() {
        LinkedList<Character> chars = new LinkedList<Character>();

        for (int i = 255; i >= 0; i--) {
            chars.addFirst((char) i);
        }

        return chars;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> chars = initAsciiList();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i = chars.indexOf(c);
            BinaryStdOut.write(i, 8);
            chars.remove(i);
            chars.addFirst(c);
        }

        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> chars = initAsciiList();

        while (!BinaryStdIn.isEmpty()) {
            int r = BinaryStdIn.readInt(8);
            char c = chars.get(r);
            BinaryStdOut.write(c);
            chars.remove(r);
            chars.addFirst(c);
        }

        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
