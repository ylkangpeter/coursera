import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private WeightedQuickUnionUF unionUF;
    private int top = 0;
    private int bottom;
    private boolean[][] grids;
    private int n;
    private int numberOfOpenSites = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.grids = new boolean[n][n];
        // n+2, top & bottom
        this.unionUF = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
        this.bottom = n * n + 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        if (this.grids[row - 1][col - 1]) {
            return;
        }
        this.grids[row - 1][col - 1] = true;
        numberOfOpenSites++;

        if (row == 1) {
            // first line
            unionUF.union(col, top);
        }
        if (row == n) {
            // last line
            unionUF.union(inxHelper(row, col), bottom);
        }
        // middle up/down
        if (row > 1 && isOpen(row - 1, col)) {
            unionUF.union(inxHelper(row - 1, col), inxHelper(row, col));
        }
        if (row < n && isOpen(row + 1, col)) {
            unionUF.union(inxHelper(row + 1, col), inxHelper(row, col));
        }
        // left/right
        if (col > 1 && isOpen(row, col - 1)) {
            unionUF.union(inxHelper(row, col), inxHelper(row, col - 1));
        }
        if (col < n && isOpen(row, col + 1)) {
            unionUF.union((inxHelper(row, col)), inxHelper(row, col + 1));
        }

    }

    private int inxHelper(int row, int col) {
        return (row - 1) * n + col;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > 0 && row <= n && col > 0 && col <= n) {
            return this.grids[row - 1][col - 1];
        } else {
            throw new IllegalArgumentException();
        }

    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > 0 && row <= n && col > 0 && col <= n) {
            return unionUF.connected(top, inxHelper(row, col));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionUF.connected(top, bottom);
    }

//    public static void main(String[] args) throws Exception {
//        File[] files = new File("/Users/kangyongliang262/Downloads/percolation/").listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.getName().endsWith("input4.txt");
//            }
//        });
//
//        for (File file : files) {
//            System.out.print(file.getName());
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            int n = Integer.parseInt(br.readLine());
//            Percolation percolation = new Percolation(n);
//
//            String line = "";
//            while ((line = br.readLine()) != null
//                    && !line.equals("")
//                    && !line.equals("\n")) {
//                line = line.trim();
//                String[] tmps = line.split(" ");
//                int row = 0;
//                int col = 0;
//                for (int i = 0; i < tmps.length; i++) {
//                    if (!tmps[i].equals("")) {
//                        int val = Integer.parseInt(tmps[i]);
//                        if (row == 0) {
//                            row = val;
//                        } else {
//                            col = val;
//                            break;
//                        }
//                    }
//                }
//                int i = percolation.numberOfOpenSites;
////                System.out.println(line);
//                percolation.open(row, col);
//            }
//            System.out.println("\t\t" + percolation.percolates());
//        }
//    }
}
