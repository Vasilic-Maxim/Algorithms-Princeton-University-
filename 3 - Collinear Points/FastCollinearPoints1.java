import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints1 {
    private final ArrayList<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints1(Point[] points) {
        validate(points);
        Point[] main = points.clone();
        Arrays.sort(main);

        duplicates(main);

        int n = main.length;
        ArrayList<String> segmentsAsString = new ArrayList<>();

        // find all combinations of points
        for (int i = 0; i < n - 2; i++) {
            // sort by point
            Comparator<Point> comp = main[i].slopeOrder();
            Arrays.sort(main, comp);
            // track points on a line
            ArrayList<Point> segmentPoints = new ArrayList<>();
            segmentPoints.add(main[i]);

            int j = 0;
            while (j < n - 2) {
                if (comp.compare(main[j], main[j + 2]) == 0) {
                    // search for a valid segment
                    int last = j + 2;
                    while (last < n && comp.compare(main[j], main[last]) == 0) last++;
                    segmentPoints.addAll(Arrays.asList(main).subList(j, last));
                    j = last;
                }
                else j++;
            }

            if (segmentPoints.size() < 4) continue;

            segmentPoints.sort(Point::compareTo);
            LineSegment segment = new LineSegment(
                    segmentPoints.get(0),
                    segmentPoints.get(segmentPoints.size() - 1)
            );

            if (!segmentsAsString.contains(segment.toString())) {
                segments.add(segment);
                segmentsAsString.add(segment.toString());
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
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
        for (int i = 0; i < n; i++) points[i] = new Point(in.readInt(), in.readInt());

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) p.draw();
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints1 collinear = new FastCollinearPoints1(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments());
    }
}
