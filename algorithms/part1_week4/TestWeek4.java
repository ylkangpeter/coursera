import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class TestWeek4 {
    public static void main(String[] args) {

        File f = new File("/Volumes/wd/git/coursera/algorithms/week4/test");
        String[] files = f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains("puzzle17.txt");
            }
        });

        Arrays.sort(files);

        // create initial board from file
        for (String file : files) {
            edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(file);
            int n = in.readInt();
            int[][] blocks = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    blocks[i][j] = in.readInt();
            Board initial = new Board(blocks);

            // solve the puzzle
            Solver solver = new Solver(initial);

            // print solution to standard output
            if (!solver.isSolvable())
                edu.princeton.cs.algs4.StdOut.println("No solution possible");
            else {
                edu.princeton.cs.algs4.StdOut.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution()) {
                    edu.princeton.cs.algs4.StdOut.println(board);
                    System.out.println();
                }
            }
        }
    }
}