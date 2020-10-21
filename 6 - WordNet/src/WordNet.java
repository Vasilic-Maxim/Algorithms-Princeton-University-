import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.Arrays;

public class WordNet {
    private final ST<String, Bag<Integer>> st;
    private final String[] keys;
    private final Digraph graph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validateString(synsets);
        validateString(hypernyms);

        In in = new In(synsets);
        st = new ST<>();
        int n = 0;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            if (!st.contains(a[1])) st.put(a[1], new Bag<>());
            st.get(a[1]).add(Integer.parseInt(a[0]));
            n++;
        }

        keys = new String[n];
        for (String name : st.keys()) for (Integer i : st.get(name))
            keys[i] = name;

        graph = new Digraph(n);
        in = new In(hypernyms);
        while (!in.hasNextLine()) {
            String[] ids = in.readLine().split("/");
            int set_id = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++)
                graph.addEdge(set_id, Integer.parseInt(ids[i]));
        }

        // Check if digraph has cycles
        validateDAG(graph);

        // Create an instance of SAP class
        sap = new SAP(graph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return Arrays.asList(keys);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return st.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateWord(nounA);
        validateWord(nounB);

        return sap.length(st.get(nounA), st.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateWord(nounA);
        validateWord(nounB);

        int i = sap.ancestor(st.get(nounA), st.get(nounB));
        return keys[i];
    }

    private void validateDAG(Digraph digraph) {
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) throw new IllegalArgumentException("The graph contains a cycle.");
    }

    private void validateString(String item) {
        if (item == null) throw new IllegalArgumentException("Required a string, got NULL instead.");
    }

    private void validateWord(String noun) {
        validateString(noun);
        if (!isNoun(noun))
            throw new IllegalArgumentException("The key is not int the dictionary.");
    }
}