import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
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
        int ancestor = ancestor(v, w);
        if (ancestor == -1) return -1;

        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G, w);
        return pathV.distTo(ancestor) + pathW.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        boolean[] marked = new boolean[G.V()];
        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        q.enqueue(w);
        marked[v] = marked[w] = true;

        while (!q.isEmpty()) {
            int candidate = q.dequeue();
            for (int ancestor : G.adj(candidate)) {
                if (!marked[ancestor]) {
                    marked[ancestor] = true;
                    q.enqueue(ancestor);
                } else return ancestor;
            }
        }

        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int result = INFINITY;
        for (int i : v)
            for (int j : w)
                result = Math.min(result, length(i, j));

        return result;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }
}