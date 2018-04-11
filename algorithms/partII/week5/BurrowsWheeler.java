import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        // output rest data
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(s.charAt((s.length() - 1 + csa.index(i)) % s.length()));
        }
        BinaryStdOut.close();
    }

    /**
     * <pre>
     *      M                     P        N
     * 0    ! ? ? ? ? ? ? ? ? ? ? A        3
     * 1    A ? ? ? ? ? ? ? ? ? ? R        0
     * 2    A ? ? ? ? ? ? ? ? ? ? D
     * 3    A ? ? ? ? ? ? ? ? ? ? !
     * 4    A ? ? ? ? ? ? ? ? ? ? R
     * 5    A ? ? ? ? ? ? ? ? ? ? C
     * 6    B ? ? ? ? ? ? ? ? ? ? A
     * 7    B ? ? ? ? ? ? ? ? ? ? A
     * 8    C ? ? ? ? ? ? ? ? ? ? A
     * 9    D ? ? ? ? ? ? ? ? ? ? A
     * 10   R ? ? ? ? ? ? ? ? ? ? B
     * 11   R ? ? ? ? ? ? ? ? ? ? B
     *
     * for ele in col_M:
     *      find index of ele in P begin at 'offset'
     *      e.g.
     *          row_1_A == row_0_P
     *          row_2_A == row_6_P
     *          so
     *              N_1=6 and N_2=7
     *          and so on
     *
     * </pre>
     */
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int nextInx = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        char[] sorted = s.toCharArray();
        Arrays.sort(sorted);

        int[] next = new int[input.length];
        int[] counter = new int[R];
        for (int i = 0; i < counter.length; i++) {
            counter[i] = -1;
        }

        for (int i = 0; i < input.length; i++) {
            char c = sorted[i];
            int inx = getInx(c, counter, input);
            next[i] = inx;
        }

        for (int i = 0; i < input.length; i++) {
            BinaryStdOut.write(sorted[nextInx]);
            nextInx = next[nextInx];
        }
        BinaryStdOut.close();
    }

    private static int getInx(char c, int[] counter, char[] input) {
        int start = counter[c];
        for (int i = start + 1; i < input.length; i++) {
            if (input[i] == c) {
                counter[c] = i;
                return i;
            }
        }
        return -1;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}