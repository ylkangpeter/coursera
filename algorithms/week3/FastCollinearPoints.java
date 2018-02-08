import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FastCollinearPoints {

    private Point[] pp;

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        pp = points.clone();
        for (int i = 0; i < pp.length; i++) {
            if (pp[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        Arrays.sort(pp);

        for (int i = 0; i < pp.length - 1; i++) {
            if (pp[i].compareTo(pp[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        List<Point[]> list = new ArrayList<>();


        for (int i = 0; i < pp.length; i++) {
            InnerClass[] tmps = new InnerClass[pp.length - 1 - i];
            for (int m = i + 1, k = 0; m < pp.length; m++) {
                tmps[k++] = new InnerClass(pp[m], pp[i].slopeTo(pp[m]));
            }

            Arrays.sort(tmps);

            // Warning. Do not trust the professor, there will be more than 4 points in a line T T
            for (int k = 0; k < tmps.length - 2; k++) {
                if (tmps[k].slope == tmps[k + 1].slope && tmps[k].slope == tmps[k + 2].slope) {
                    int addup = 3;
                    while (k + addup < tmps.length && tmps[k].slope == tmps[k + addup].slope) {
                        addup++;
                    }
                    list.add(new Point[]{pp[i], tmps[k + addup - 1].endPoint});
                    k = k + addup - 1;
                }
            }
        }


        Collections.sort(list, (o1, o2) -> {
            if (o1[1].compareTo(o2[1]) == 0) {
                return o1[0].compareTo(o2[0]);
            } else {
                return o1[1].compareTo(o2[1]);
            }
        });

        if (!list.isEmpty()) {
            Iterator<Point[]> iterator = list.iterator();
            Point[] pre = iterator.next();
            double preSlope = pre[1].slopeTo(pre[0]);

            for (; iterator.hasNext(); ) {
                Point[] cur = iterator.next();
                double curSlote = cur[1].slopeTo(cur[0]);
                if (pre[1].compareTo(cur[1]) == 0 && cur[1].slopeTo(cur[0]) == preSlope) {
                    iterator.remove();
                } else {
                    preSlope = curSlote;
                    pre = cur;
                }

            }
        }

        lineSegments = new LineSegment[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lineSegments[i] = new LineSegment(list.get(i)[0], list.get(i)[1]);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++)
            result[i] = lineSegments[i];
        return result;
    }

    private static class InnerClass implements Comparable<InnerClass> {
        Point endPoint;
        double slope;

        InnerClass(Point endPoint, double slope) {
            this.endPoint = endPoint;
            this.slope = slope;
        }

        @Override
        public int compareTo(InnerClass o) {
            if (slope > o.slope) {
                return -1;
            } else if (slope < o.slope) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println(Double.POSITIVE_INFINITY == Double.POSITIVE_INFINITY);
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
//            segment.draw();
        }
//        StdDraw.show();
    }

}