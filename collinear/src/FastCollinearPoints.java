import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to find all segments containing 4 or more points in the given list of points
 */
public class FastCollinearPoints {

    private final LineSegment[] segments;

    /**
     * Finds all segments containing 4 or more points in the given list of points
     *
     * @param points - list of points to be analyzed
     */
    public FastCollinearPoints(Point[] points) {
        // points array is not null
        noPoints(points);
        Point[] pointsCopySO = Arrays.copyOf(points, points.length);
        validate(pointsCopySO);

        Point[] pointsCopyNO = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopyNO);

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        for (Point origin : pointsCopyNO) {
            Arrays.sort(pointsCopySO);
            Arrays.sort(pointsCopySO, origin.slopeOrder());
            int count = 1;
            Point lineBeginning = null;
            for (int j = 0; j < pointsCopySO.length - 1; ++j) {
                if (pointsCopySO[j].slopeTo(origin) == pointsCopySO[j + 1].slopeTo(origin)) {
                    count++;
                    if (lineBeginning == null) {
                        lineBeginning = pointsCopySO[j];
                        count++;
                    } else if (count >= 4 && j + 1 == pointsCopySO.length - 1) {
                        if (lineBeginning.compareTo(origin) > 0) {
                            segmentsList.add(new LineSegment(origin, pointsCopySO[j + 1]));
                        }
                        count = 1;
                    }
                } else if (count >= 4) {
                    if (lineBeginning.compareTo(origin) > 0) {
                        segmentsList.add(new LineSegment(origin, pointsCopySO[j]));
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

    private static void validate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            validatePoint(points[i]);
            if (i > 0) duplicates(points[i - 1], points[i]);
        }
    }

    private static void noPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("One of the points is null");
    }

    private static void validatePoint(Point point) {
        if (point == null) throw new IllegalArgumentException("One of the points is null");
    }

    private static void duplicates(Point first, Point second) {
        if (first.compareTo(second) == 0) throw new IllegalArgumentException("Duplicate found!");
    }

    //==========================================================================
    // Testing
    //==========================================================================

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) points[i] = new Point(in.readInt(), in.readInt());

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
