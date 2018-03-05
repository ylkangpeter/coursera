import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private Point[] pp;

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

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

        LinkedList<LineSegment> segments = new LinkedList<>();
        // calc lineSegments
        for (int a = 0; a < pp.length; a++) {
            for (int b = a + 1; b < pp.length; b++) {
                double slope_1 = pp[a].slopeTo(pp[b]);
                for (int c = b + 1; c < pp.length; c++) {
                    double slope_2 = pp[a].slopeTo(pp[c]);
                    for (int d = c + 1; d < pp.length; d++) {
                        double slope_3 = pp[a].slopeTo(pp[d]);
                        if (slope_1 == slope_2 && slope_1 == slope_3) {
                            segments.add(new LineSegment(pp[a], pp[d]));
                        }
                    }
                }
            }
        }
        lineSegments = segments.toArray(new LineSegment[segments.size()]);
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

    public static void main(String[] args) {

        // read the n points from a file

        for (int k = 0; k < 20; k++) {
            In in = new In(args[0]);
            int n = Integer.parseInt(in.readLine());
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
            BruteCollinearPoints collinear = new BruteCollinearPoints(points);
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
//            segment.draw();
            }
        }
//        StdDraw.show();
    }
}