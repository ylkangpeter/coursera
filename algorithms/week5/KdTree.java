import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

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

        size++;

        if (root == null) {
            root = new MyNode(p, true);
        } else {
            insertNode(root, p);
        }
    }

    private void insertNode(MyNode node, Point2D p) {
        boolean compareX = node.point.x() > p.x();
        boolean compareY = node.point.y() > p.y();

        // if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise go right);
        if (node.useX ? compareX : compareY) {
            if (node.left == null) {
                node.left = new MyNode(p, !node.useX);
            } else {
                insertNode(node.left, p);
            }
        } else {
            if (node.right == null) {
                node.right = new MyNode(p, !node.useX);
            } else {
                insertNode(node.right, p);
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return findPoint(p, root);
    }

    private boolean findPoint(Point2D p, MyNode node) {
        if (node == null) {
            return false;
        } else {
            if (node.point.equals(p)) {
                return true;
            } else {
                boolean compareX = node.point.x() > p.x();
                boolean compareY = node.point.y() > p.y();

                if (node.useX ? compareX : compareY) {
                    return findPoint(p, node.left);
                } else {
                    return findPoint(p, node.right);
                }
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root);
    }

    private void drawNode(MyNode node) {
        if (node != null) {
            StdDraw.point(node.point.x(), node.point.y());
            drawNode(node.left);
            drawNode(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(edu.princeton.cs.algs4.RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        List<Point2D> result = new ArrayList<Point2D>();
        range(rect, root, result);
        return result;
    }

    private void range(RectHV rect, MyNode node, List<Point2D> result) {
        if (node == null) {
            return;
        } else {
            if (rect.contains(node.point)) {
                result.add(node.point);
            }
            if (node.useX ? node.point.x() <= rect.xmax() : node.point.y() <= rect.ymax()) {
                range(rect, node.right, result);
            }
            if (node.useX ? node.point.x() >= rect.xmin() : node.point.y() >= rect.ymin()) {
                range(rect, node.left, result);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty

    // if the closest point discovered so far is closer than the distance between the query point and the rectangle
    //  corresponding to a node, there is no need to explore that node (or its subtrees). That is, search a node only
    //  only if it might contain a point that is closer than the best one found so far. The effectiveness of the pruning
    //  rule depends on quickly finding a nearby point. To do this, organize the recursive method so that when there are
    //  two possible subtrees to go down, you always choose the subtree that is on the same side of the splitting line as
    //  the query point as the first subtree to explore—the closest point found while exploring the first subtree may enable
    //  pruning of the second subtree.
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        } else {
            DistanceObj distanceObj = new DistanceObj();
            distanceObj.distance = Double.MAX_VALUE;

            NNR(p, root, distanceObj);
            return distanceObj.point;
        }
    }

    private void NNR(Point2D point, MyNode node, DistanceObj obj) {
        if (node == null) {
            return;
        } else {
            double distance = node.point.distanceTo(point);
            if (distance < obj.distance) {
                obj.distance = distance;
                obj.point = node.point;
            }
        }

        double tmpDist = Math.abs(node.point.x() - point.x());
        if (!node.useX) {
            tmpDist = Math.abs(node.point.y() - point.y());
        }

        if (node.useX ? (node.point.x() > point.x()) : (node.point.y() > point.y())) {
            // try left first
            NNR(point, node.left, obj);
            // try right
            if (tmpDist <= obj.distance) {
                NNR(point, node.right, obj);
            }
        } else {
            // try right first
            NNR(point, node.right, obj);
            if (tmpDist <= obj.distance) {
                // try left
                NNR(point, node.left, obj);
            }
        }

    }

    private static class DistanceObj {
        private double distance;
        private Point2D point = null;
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

//        StdDraw.setScale(-10, +10);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLUE);
        double xmin = 0.150390625;
        double ymin = 0.853515625;
        double xmax = 0.849609375;
        double ymax = 0.94140625;
        RectHV rectangle = new RectHV(xmin, ymin, xmax, ymax);
        rectangle.draw();

        StdDraw.setPenColor(StdDraw.RED);
//        StdDraw.rectangle(3, 5, 1, 1.5);
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.206107, 0.095492));
        tree.insert(new Point2D(0.975528, 0.654508));
        tree.insert(new Point2D(0.024472, 0.345492));
        tree.insert(new Point2D(0.793893, 0.095492));
        tree.insert(new Point2D(0.793893, 0.904508));
        tree.insert(new Point2D(0.975528, 0.345492));
        tree.insert(new Point2D(0.206107, 0.904508));
        tree.insert(new Point2D(0.500000, 0.000000));
        tree.insert(new Point2D(0.024472, 0.654508));
        tree.insert(new Point2D(0.500000, 1.000000));
        tree.draw();

        System.out.println(tree.contains(new Point2D(9, 6)));
        System.out.println(tree.contains(new Point2D(4, 8)));
        tree.range(rectangle);
    }

}