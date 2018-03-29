import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolverOthers {
    //    private BoggleTrie<Integer> dict = new BoggleTrie<>();
    private BoggleTrieST<Integer> dict = new BoggleTrieST<>();

    private static class BoggleTrieST<Value> {
        private static final int R = 26; // A-Z letters
        private static final int OFFSET = 65; // Offset of letter A in ASCII table

        private Node root = new Node();

        private static class Node {
            private Object val;
            private Node[] next = new Node[R];
        }

        public enum NodeType {
            PREFIX, MATCH, NON_MATCH
        }

        /****************************************************
         * Is the key in the symbol table?
         ****************************************************/
        public boolean contains(String key) {
            return get(key) != null;
        }

        public Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null)
                return null;
            return (Value) x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d + 1);
        }

        /****************************************************
         * Insert key-value pair into the symbol table.
         ****************************************************/
        public void put(String key, Value val) {
            root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, val, d + 1);
            return x;
        }

        // find the key that is the longest prefix of s
        public String longestPrefixOf(String query) {
            int length = longestPrefixOf(root, query, 0, 0);
            return query.substring(0, length);
        }

        // find the key in the subtrie rooted at x that is the longest
        // prefix of the query string, starting at the dth character
        private int longestPrefixOf(Node x, String query, int d, int length) {
            if (x == null)
                return length;
            if (x.val != null)
                length = d;
            if (d == query.length())
                return length;
            char c = query.charAt(d);
            return longestPrefixOf(x.next[c - OFFSET], query, d + 1, length);
        }

        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> queue = new Queue<String>();
            Node x = get(root, prefix, 0);
            collect(x, prefix, queue);
            return queue;
        }

        public boolean isPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }

        public NodeType getNodeType(String key) {
            Node x = get(root, key, 0);
            if (x == null)
                return NodeType.NON_MATCH;
            else if (x.val == null)
                return NodeType.PREFIX;
            else
                return NodeType.MATCH;
        }

        private void collect(Node x, String key, Queue<String> queue) {
            if (x == null)
                return;
            if (x.val != null)
                queue.enqueue(key);
            for (int c = 0; c < R; c++)
                collect(x.next[c - OFFSET], key + (char) c, queue);
        }

        public Iterable<String> keysThatMatch(String pat) {
            Queue<String> q = new Queue<String>();
            collect(root, "", pat, q);
            return q;
        }

        public void collect(Node x, String prefix, String pat, Queue<String> q) {
            if (x == null)
                return;
            if (prefix.length() == pat.length() && x.val != null)
                q.enqueue(prefix);
            if (prefix.length() == pat.length())
                return;
            char next = pat.charAt(prefix.length());
            for (int c = 0; c < R; c++)
                if (next == '.' || next == c)
                    collect(x.next[c - OFFSET], prefix + (char) c, pat, q);
        }

        public void delete(String key) {
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                x.val = null;
            else {
                char c = key.charAt(d);
                x.next[c - OFFSET] = delete(x.next[c - OFFSET], key, d + 1);
            }
            if (x.val != null)
                return x;
            for (int c = 0; c < R; c++)
                if (x.next[c - OFFSET] != null)
                    return x;
            return null;
        }
    }

    /**
     * Initializes the data structure using the given array of strings as the dictionary. (You can assume each word in the dictionary contains only
     * the uppercase letters A through Z.)
     *
     * @param dictionary
     */
    public BoggleSolverOthers(String[] dictionary) {
        for (String s : dictionary) {
            dict.put(s, 1);
        }
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     *
     * @param board
     * @return The set of all valid words in the given Boggle board, as an Iterable.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> words = new TreeSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                searchWords(board, i, j, words);
            }
        }
        return words;
    }

    private void searchWords(BoggleBoard board, int i, int j, TreeSet<String> words) {
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        dfs(board, i, j, words, visited, "");
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise. (You can assume the word contains only the uppercase letters A
     * through Z.)
     *
     * @param word
     * @return The score of the given word if it is in the dictionary, zero otherwise. (You can assume the word contains only the uppercase letters A
     * through Z.)
     */
    public int scoreOf(String word) {
        if (dict.contains(word)) {
            if (word.length() < 3) {
                return 0;
            } else if (word.length() < 5) {
                return 1;
            } else if (word.length() < 6) {
                return 2;
            } else if (word.length() < 7) {
                return 3;
            } else if (word.length() < 8) {
                return 5;
            } else {
                return 11;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private void dfs(BoggleBoard board, int i, int j, Set<String> words, boolean[][] visited, String prefix) {
        if (visited[i][j]) {
            return;
        }

        char letter = board.getLetter(i, j);
        prefix = prefix + (letter == 'Q' ? "QU" : letter);

        if (prefix.length() > 2 && dict.contains(prefix)) {
            words.add(prefix);
        }
        if (!dict.isPrefix(prefix)) {
            return;
        }

        visited[i][j] = true;

        // do a DFS for all adjacent cells
        if (i > 0) {
            dfs(board, i - 1, j, words, visited, prefix);
            if (j > 0) {
                dfs(board, i - 1, j - 1, words, visited, prefix);
            }
            if (j < board.cols() - 1) {
                dfs(board, i - 1, j + 1, words, visited, prefix);
            }
        }
        if (j > 0) {
            dfs(board, i, j - 1, words, visited, prefix);
        }
        if (j < board.cols() - 1) {
            dfs(board, i, j + 1, words, visited, prefix);
        }
        if (i < board.rows() - 1) {
            if (j > 0) {
                dfs(board, i + 1, j - 1, words, visited, prefix);
            }
            if (j < board.cols() - 1) {
                dfs(board, i + 1, j + 1, words, visited, prefix);
            }
            dfs(board, i + 1, j, words, visited, prefix);
        }
        visited[i][j] = false;
    }
}