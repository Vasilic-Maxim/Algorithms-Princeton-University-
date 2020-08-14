import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] segments;

    /**
     * Finds all segments containing 4 or more points in the given list of points
     *
     * @param points - list of points to be analyzed
     */
    public FastCollinearPoints(Point[] points) {
        validate(points);

        Point[] sorted = points.clone();
        Arrays.sort(sorted);

        duplicates(sorted);

        Point[] pointsCopySO = points.clone();

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        for (Point point : sorted) {
            Arrays.sort(pointsCopySO);
            Arrays.sort(pointsCopySO, point.slopeOrder());
            int count = 1;
            Point lineBeginning = null;
            for (int j = 0; j < pointsCopySO.length - 1; ++j) {
                if (pointsCopySO[j].slopeTo(point) == pointsCopySO[j + 1].slopeTo(point)) {
                    count++;
                    if (lineBeginning == null) {
                        lineBeginning = pointsCopySO[j];
                        count++;
                    } else if (count >= 4 && j + 1 == pointsCopySO.length - 1) {
                        if (lineBeginning.compareTo(point) > 0) {
                            segmentsList.add(new LineSegment(point, pointsCopySO[j + 1]));
                        }
                        count = 1;
                    }
                } else if (count >= 4) {
                    if (lineBeginning.compareTo(point) > 0) {
                        segmentsList.add(new LineSegment(point, pointsCopySO[j]));
                    }
                    count = 1;
                } else {
                    count = 1;
                }
            }
        }

        segments = segmentsList.toArray(new LineSegment[0]);
    }

    /**
     * Returns the number of segments
     *
     * @return number of segments containing 4 or more points
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * Returns array of segments that were discovered in the given points array
     *
     * @return array of discovered segments
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

    //==========================================================================
    // Utils
    //==========================================================================

    private void validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException("One of the points is null");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("One of the points is null");
        }
    }

    private void duplicates(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) throw new IllegalArgumentException("Duplicate found!");
        }
    }

    //==========================================================================
    // Testing
    //==========================================================================

    public static void main(String[] args) {
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
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) p.draw();
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments());
    }
}
