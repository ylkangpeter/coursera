import edu.princeton.cs.algs4.Point2D;

public class KdTree {

    private int size = 0;

    private MyNode root = null;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new MyNode(p, true);
        } else {
            traverseNode(root, p);
        }
    }

    private void traverseNode(MyNode node, Point2D p) {
        if (node.useX) {
            if(node.point.x())
        } else {

        }

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    // draw all points to standard draw
    public void draw() {
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(edu.princeton.cs.algs4.RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    private static class MyNode {
        private boolean useX = true;
        private Point2D point;
        private MyNode left;
        private MyNode right;

        public MyNode(Point2D point, boolean useX) {
            this.point = point;
            this.useX = useX;
        }

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}