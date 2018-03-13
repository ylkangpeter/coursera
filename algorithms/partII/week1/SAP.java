import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by ylkang on 3/5/18.
 */
public class SAP {

    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validate(G);
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] result = ancestorAndLength(v, w);
        return result[0] < 0 ? -1 : result[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] result = ancestorAndLength(v, w);
        return result[0];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        int[] result = ancestorAndLengthGroups(v, w);
        return result[0] > 0 ? result[1] : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        int[] result = ancestorAndLengthGroups(v, w);
        return result[0] > 0 ? result[0] : -1;
    }

    private int[] ancestorAndLengthGroups(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        int[] result = {-1, Integer.MAX_VALUE};

        for (int a : v) {
            for (int b : w) {
                int[] tmp = ancestorAndLength(a, b);

                if (tmp[0] >= 0 && result[1] > tmp[1]) {
                    result[1] = tmp[1];
                    result[0] = tmp[0];
                }
            }
        }
        return result;
    }

    private void validate(Object a) {
        if (a == null) {
            throw new IllegalArgumentException();
        }
    }

    private int[] ancestorAndLength(int v, int w) {
        int inx = -1;
        int distance = Integer.MAX_VALUE;

        BreadthFirstDirectedPaths bfdV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfdV.hasPathTo(i) && bfdW.hasPathTo(i)) {
                int tmp = bfdV.distTo(i) + bfdW.distTo(i);
                if (distance > tmp) {
                    distance = tmp;
                    inx = i;
                }
            }
        }
        return new int[]{inx, distance};
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}

