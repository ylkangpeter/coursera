public class Board {

    private int[][] blocks;

    private int hamming = 0;

    private int manhattan = 0;

    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException();
        }
        if (blocks[0] == null) {
            throw new IllegalArgumentException();
        }
        this.blocks = new int[blocks[0].length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    continue;
                }
                int distance = Math.abs((blocks[i][j] - 1) / blocks.length - i) + Math.abs((blocks[i][j] - 1) % blocks.length - j);
                if (distance != 0) {
                    hamming++;
                    manhattan += distance;
                }
            }
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        return this.hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        int firstInx = -1;
        int secondInx = -1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                newBlocks[i][j] = blocks[i][j];
                if (newBlocks[i][j] != 0) {
                    if (firstInx < 0) {
                        firstInx = i * blocks.length + j;
                    } else if (secondInx < 0) {
                        secondInx = i * blocks.length + j;
                    }
                }
            }
        }
        // swap
        int tmp = newBlocks[firstInx / blocks.length][firstInx % blocks.length];
        newBlocks[firstInx / blocks.length][firstInx % blocks.length] = newBlocks[secondInx / blocks.length][secondInx % blocks.length];
        newBlocks[secondInx / blocks.length][secondInx % blocks.length] = tmp;
        return new Board(newBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) {
            throw new IllegalArgumentException();
        }
        Board yBoard = (Board) y;
        return yBoard.toString().equals(this.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        java.util.List<Board> list = new java.util.ArrayList<Board>();
        int row = 0;
        int col = 0;
        boolean isFound = false;
        for (int i = 0; i < blocks.length && !isFound; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // left -> right
        if (col != 0) {
            int[][] copyArray = copyArray(blocks);
            copyArray[row][col] = copyArray[row][col - 1];
            copyArray[row][col - 1] = 0;
            Board board = new Board(copyArray);
            list.add(board);
        }
        // right -> left
        if (col != blocks[0].length - 1) {
            int[][] copyArray = copyArray(blocks);
            copyArray[row][col] = copyArray[row][col + 1];
            copyArray[row][col + 1] = 0;
            Board board = new Board(copyArray);
            list.add(board);
        }
        // down -> up
        if (row != 0) {
            int[][] copyArray = copyArray(blocks);
            copyArray[row][col] = copyArray[row - 1][col];
            copyArray[row - 1][col] = 0;
            Board board = new Board(copyArray);
            list.add(board);
        }
        // up -> down
        if (row != blocks.length - 1) {
            int[][] copyArray = copyArray(blocks);
            copyArray[row][col] = copyArray[row + 1][col];
            copyArray[row + 1][col] = 0;
            Board board = new Board(copyArray);
            list.add(board);
        }
        return list;
    }

    private int[][] copyArray(int[][] array) {
        int[][] newArray = new int[array.length][array[0].length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                newArray[i][j] = blocks[i][j];
            }
        }
        return newArray;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                sb.append("\t").append(blocks[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] blocks = {{2, 1, 3}, {6, 5, 4}, {7, 8, 0}};
        Board board = new Board(blocks);
        System.out.println("origin:\n" + board.toString());
        System.out.println("==============");
        System.out.println("manhattan:\t" + board.manhattan());
        System.out.println("hamming:\t" + board.hamming());
        System.out.println("==============");
        System.out.println("twin:\n" + board.twin());
        System.out.println("neighbors:\n");
        for (Board b1 : board.neighbors()) {
            System.out.println(b1);
            System.out.println("---");
        }

        blocks = new int[][]{{0, 1, 3}, {6, 5, 4}, {7, 8, 2}};
        board = new Board(blocks);
        System.out.println("origin:\n" + board.toString());
        System.out.println("==============");
        System.out.println("manhattan:\t" + board.manhattan());
        System.out.println("hamming:\t" + board.hamming());
        System.out.println("==============");
        System.out.println("twin:\n" + board.twin());
        System.out.println("neighbors:\n");
        for (Board b1 : board.neighbors()) {
            System.out.println(b1);
            System.out.println("---");
        }

        blocks = new int[][]{{6, 1, 3}, {0, 5, 4}, {7, 8, 2}};
        board = new Board(blocks);
        System.out.println("origin:\n" + board.toString());
        System.out.println("==============");
        System.out.println("manhattan:\t" + board.manhattan());
        System.out.println("hamming:\t" + board.hamming());
        System.out.println("==============");
        System.out.println("twin:\n" + board.twin());
        System.out.println("neighbors:\n");
        for (Board b1 : board.neighbors()) {
            System.out.println(b1);
            System.out.println("---");
        }

        blocks = new int[][]{{0, 2, 3}, {4, 5, 6}, {7, 8, 1}};
        board = new Board(blocks);
        System.out.println("origin:\n" + board.toString());
        System.out.println("==============");
        System.out.println("manhattan:\t" + board.manhattan());
        System.out.println("hamming:\t" + board.hamming());
        System.out.println("==============");
        System.out.println("twin:\n" + board.twin());
        System.out.println("neighbors:\n");
        for (Board b1 : board.neighbors()) {
            System.out.println(b1);
            System.out.println("---");
        }
    }
}