public class TestWeek4 {
    public static void main(String[] args) {

        // create initial board from file
        edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(args[0]);
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
            for (Board board : solver.solution())
                edu.princeton.cs.algs4.StdOut.println(board);
        }
    }
}