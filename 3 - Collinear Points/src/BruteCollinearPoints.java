import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("One of the points is null");
        for (Point point : points)
            if (point == null) throw new IllegalArgumentException("One of the points is null");

        Point[] copy = points.clone();
        Arrays.sort(copy);

        for (int i = 1; i < points.length; i++)
            if (points[i].compareTo(points[i - 1]) == 0) throw new IllegalArgumentException("Duplicate found!");

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

    private ArrayList<LineSegment> segments(Point[] points) {
        int n = points.length;
        ArrayList<LineSegment> result = new ArrayList<>();

        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    Comparator<Point> cmp = points[p].slopeOrder();
                    if (threeOnLine(cmp, points[q], points[r])) continue;

                    for (int s = r + 1; s < n; s++) {
                        if (threeOnLine(cmp, points[q], points[s])) continue;
                        result.add(new LineSegment(points[p], points[s]));
                    }
                }
            }
        }

        return result;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments());
    }
}
