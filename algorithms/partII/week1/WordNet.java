import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ylkang on 3/5/18.
 */
public class WordNet {

    private Map<String, ArrayList<Integer>> nouns = new HashMap<String, ArrayList<Integer>>();

    private List<String> synset = new ArrayList<>();

    private Digraph digraph;

    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        parseSynsets(synsets);
        digraph = new Digraph(synset.size());
        parseHypernyms(hypernyms);
        //circle
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }

        // has root
        int rooted = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (!this.digraph.adj(i).iterator().hasNext()) {
                if (++rooted > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }

        sap = new SAP(digraph);
    }

    private void parseSynsets(String synsets) {
        validate(synsets);
        In in = new In(synsets);
        String line = "";
        while ((line = in.readLine()) != null) {
            if (line.trim().equals("")) {
                continue;
            } else {
                String[] tmps = line.split(",");
                synset.add(tmps[1]);
                String[] nounArr = tmps[1].split(" ");
                for (String noun : nounArr) {
                    ArrayList<Integer> l = nouns.get(noun);
                    if (l == null) {
                        l = new ArrayList<Integer>();
                    }
                    l.add(Integer.parseInt(tmps[0]));
                    nouns.put(noun, l);
                }
            }
        }

    }

    private void validate(String synsets) {
        if (synsets == null) {
            throw new IllegalArgumentException();
        }
    }

    private void parseHypernyms(String hypernyms) {
        validate(hypernyms);
        In in = new In(hypernyms);
        String line = "";
        while ((line = in.readLine()) != null) {
            String[] tmps = line.split(",");
            for (int i = 1; i < tmps.length; i++) {
                digraph.addEdge(Integer.parseInt(tmps[0]), Integer.parseInt(tmps[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return Collections.unmodifiableCollection(nouns.keySet());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validate(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    private void validateNoun(String noun) {
        if (!nouns.containsKey(noun)) {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return synset.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        System.out.println(wordNet.digraph.toString());
    }
}

