import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private boolean isSolvable = false;

    private Iterable<Board> solution = null;

    private int moves = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (initial.hamming() == 0) {
            isSolvable = true;
            edu.princeton.cs.algs4.Stack<Board> list = new edu.princeton.cs.algs4.Stack<Board>();
            list.push(initial);
            solution = list;
            return;
        }
        MinPQ<Node> queue = new MinPQ<Node>((o1, o2) -> {
            int priority = o1.getBoard().manhattan() + o1.step - o2.getBoard().manhattan() - o2.step;
//            if (priority != 0) {
            return priority;
//            }
//            return o1.step - o2.step;
        });
        queue.insert(new Node(initial, null));

        java.util.Set<String> history = new java.util.HashSet<String>();
        history.add(initial.toString());

        while (!queue.isEmpty()) {
            Node node = queue.delMin();
            Board board = node.getBoard();
            Iterable<Board> iter = board.neighbors();
            Board bb = null;
            for (Board bs : iter) {
                if (bs.manhattan() == 0) {
                    Node resultNode = new Node(bs, node);
                    moves = resultNode.step;
                    edu.princeton.cs.algs4.Stack<Board> list = new edu.princeton.cs.algs4.Stack<Board>();
                    while (resultNode != null) {
                        list.push(resultNode.getBoard());
                        resultNode = resultNode.previous;
                    }

                    solution = list;
                    isSolvable = true;
                    return;
                } else if (!history.contains(bs.toString())) {
                    queue.insert(new Node(bs, node));
                    history.add(bs.toString());
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) {
            return -1;
        } else {
            return moves;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        } else {
            return solution;
        }
    }

    private static class Node {
        private Board board;
        private Node previous;
        private int step = 0;

        public Node(Board board, Node previousNode) {
            this.board = board;
            if (previousNode != null) {
                this.previous = previousNode;
                this.step = previousNode.step + 1;
            }
        }

        private Node(Node previousNode) {
            this.board = previousNode.board;
            this.previous = previousNode.getPrevious();
            this.step = previousNode.getPrevious().step;

        }

        public Board getBoard() {
            return board;
        }

        public Node getPrevious() {
            return previous;
        }

        public int getStep() {
            return step;
        }

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        int[][] blocks = {{1, 0, 2}, {7, 5, 4}, {8, 6, 3}};
        Board board = new Board(blocks);
        Solver solver = new Solver(board);
        System.out.println("moves:\t" + solver.moves());
        System.out.println("isSolvable:\t" + solver.isSolvable());
        int i = 1;
        for (Board b : solver.solution()) {
            System.out.println("---------" + i++ + "------------");
            System.out.println(b);
        }
    }
}