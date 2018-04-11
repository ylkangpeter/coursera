import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static final int R = 256;

    /**
     * move-to-front    in   out
     * -------------    ---  ---
     * A B C D E F      C    2
     * C A B D E F      A    1
     * A C B D E F      A    0
     * A C B D E F      A    0
     * A C B D E F      B    2
     * B A C D E F      C    2
     * C B A D E F      C    0
     * C B A D E F      C    0
     * C B A D E F      A    2
     * A C B D E F      C    1
     * C A B D E F      C    0
     * C A B D E F      F    5
     * F C A B D E
     * <p>
     * ABRACADABRA!
     * 41 42 52 02 44 01 45 01 04 04 02 26
     */
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] input = BinaryStdIn.readString().toCharArray();
        LinkedList<Character> list = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            list.add((char) i);
        }

        for (char c : input) {
            int inx = list.indexOf(c);
            BinaryStdOut.write((char) inx, 8);
            list.remove(inx);
            list.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] input = BinaryStdIn.readString().toCharArray();
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            list.add(i);
        }

        for (char c : input) {
            int inx = list.remove(c);
            BinaryStdOut.write((char) inx, 8);
            list.add(0, inx);
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}