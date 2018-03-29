import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {

    private MyTrieST dictionary = new MyTrieST();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null || dictionary.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String str : dictionary) {
            this.dictionary.add(str);
        }

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        Set<String> result = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] marks = new boolean[board.rows()][board.cols()];
                traverse(board, i, j, marks, result, "");
            }
        }
        return result;
    }

    private void traverse(BoggleBoard board, int i, int j, boolean[][] marks, Set<String> result, String prefix) {
        if (marks[i][j]) {
            return;
        } else {
            char cur = board.getLetter(i, j);
            prefix += cur;
            if (cur == 'Q') {
                prefix += 'U';
            }
            if (!dictionary.startsWith(prefix)) {
                return;
            } else {
                if (prefix.length() > 2 && dictionary.contains(prefix)) {
                    result.add(prefix);
                }
                marks[i][j] = true;
                for (int row : new int[]{-1, 0, 1}) {
                    for (int col : new int[]{-1, 0, 1}) {
                        int nextI = i + row;
                        int nextJ = j + col;
                        if (row == 0 && col == 0) {
                            continue;
                        }
                        if (nextI >= 0 && nextI < board.rows() && nextJ >= 0 && nextJ < board.cols()) {
                            traverse(board, nextI, nextJ, marks, result, prefix);
                        }
                    }
                }
                marks[i][j] = false;
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        } else {
            if (!dictionary.contains(word)) {
                return 0;
            } else {
                switch (word.length()) {
                    case 0:
                    case 1:
                    case 2:
                        return 0;
                    case 3:
                    case 4:
                        return 1;
                    case 5:
                        return 2;
                    case 6:
                        return 3;
                    case 7:
                        return 5;
                    default:
                        return 11;
                }
            }
        }
    }


    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-dichlorodiphenyltrichloroethanes.txt");
        int score = 0;
        Set<String> result = (Set<String>) solver.getAllValidWords(board);
        TreeSet<String> ts = new TreeSet<>(Comparator.comparing(o -> ((String) o)));
        ts.addAll(result);
        for (String word : ts) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
