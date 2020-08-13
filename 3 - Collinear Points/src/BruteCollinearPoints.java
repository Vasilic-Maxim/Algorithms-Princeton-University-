import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        Point[] copy = points.clone();
        noPoints(copy);
        validate(copy);
        this.segments = segments(copy);
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
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

    private ArrayList<LineSegment> segments(Point[] points) {
        int n = points.length;
        ArrayList<LineSegment> segments = new ArrayList<>();
        Arrays.sort(points);

        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    Comparator<Point> cmp = points[p].slopeOrder();
                    if (threeOnLine(cmp, points[q], points[r])) continue;

                    for (int s = r + 1; s < n; s++) {
                        if (threeOnLine(cmp, points[q], points[s])) continue;
                        segments.add(new LineSegment(points[p], points[s]));
                    }
                }
            }
        }

        return segments;
    }

    private boolean threeOnLine(Comparator<Point> cmp, Point second, Point third) {
        return cmp.compare(second, third) != 0;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments());
    }
}
