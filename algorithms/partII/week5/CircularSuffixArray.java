import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircularSuffixArray {

    private int[] indices;
    private int length = 0;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        length = s.length();

        List<Strip> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            list.add(new Strip(i, s));
        }
        Collections.sort(list);

        indices = new int[list.size()];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = list.get(i).inx;
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) {
            throw new IllegalArgumentException();
        }
        return indices[i];
    }

    private static class Strip implements Comparable<Strip> {
        private int inx;
        private String str;

        public Strip(int inx, String str) {
            this.inx = inx;
            this.str = str;
        }

        @Override
        public int compareTo(Strip strip) {
            for (int i = 0; i < str.length(); i++) {
                int inx1 = (inx + i) % str.length();
                int inx2 = (strip.inx + i) % str.length();
                if (str.charAt(inx1) < strip.str.charAt(inx2)) {
                    return -1;
                } else if (str.charAt(inx1) > strip.str.charAt(inx2)) {
                    return 1;
                }
            }
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray array = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(array.indices);
    }
}