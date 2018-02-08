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
                int distance = Math.abs(i * blocks.length + j - 1 - blocks[i][j]);
                if (distance != 0) {
                    hamming++;
                    manhattan = distance;
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
                        firstInx = i * blocks.length + j;
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
        java.util.List<Board> list = new java.util.ArrayList<>();

        return list;

    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            sb.append(" ");
            for (int j = 0; j < blocks[i].length; j++) {
                sb.append(" ").append(blocks[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }
}