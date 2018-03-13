import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        validate(wordnet);
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        validate(nouns);
        int distance = 0;
        String result = "";
        for (int i = 0; i < nouns.length; i++) {
            int tmp = 0;
            boolean isOK = true;
            for (int j = 0; j < nouns.length; j++) {
                int d = wordNet.distance(nouns[i], nouns[j]);
                if (d < 0) {
                    isOK = false;
                    break;
                }
                tmp += d;
            }
            if (tmp > distance && isOK) {
                distance = tmp;
                result = nouns[i];
            }
        }
        return result;
    }

    private void validate(Object a) {
        if (a == null) {
            throw new IllegalArgumentException();
        }
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }


}
