import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        if (graph == null) throw new NullPointerException("Argument should not be null");

        this.graph = new Digraph(graph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
        return helper(vPaths, wPaths)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
        return helper(vPaths, wPaths)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
        return helper(vPaths, wPaths)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
        return helper(vPaths, wPaths)[1];
    }

    private int[] helper(BreadthFirstDirectedPaths vPaths, BreadthFirstDirectedPaths wPaths) {
        int distance = INFINITY;
        int ancestor = INFINITY;
        for (int i = 0; i < graph.V(); i++) {
            if (vPaths.hasPathTo(i) && wPaths.hasPathTo(i)) {
                int currentDistance = vPaths.distTo(i) + wPaths.distTo(i);
                if (currentDistance < distance) {
                    distance = currentDistance;
                    ancestor = i;
                }
            }
        }

        if (distance == INFINITY) return new int[]{-1, -1};
        return new int[]{distance, ancestor};
    }

    private void validateVertices(Iterable<Integer> v) {
        if (v == null) throw new IllegalArgumentException("Required a sequence of vertices, got NULL instead.");
    }
}