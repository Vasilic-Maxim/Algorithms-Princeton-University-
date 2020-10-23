import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private final ST<Integer, String> sets;
    private final ST<String, Bag<Integer>> nouns;
    private final Digraph graph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validateString(synsets);
        validateString(hypernyms);

        // Collect sets and nouns they contain
        In in = new In(synsets);
        sets = new ST<>();
        nouns = new ST<>();
        while (in.hasNextLine()) {
            String[] setInfo = in.readLine().split(",");
            int id = Integer.parseInt(setInfo[0]);

            // Register the set name
            sets.put(id, setInfo[1]);

            // Register the set nouns
            for (String noun : setInfo[1].split(" ")) {
                if (!nouns.contains(noun)) nouns.put(noun, new Bag<>());
                nouns.get(noun).add(id);
            }
        }

        // Build digraph
        graph = new Digraph(sets.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] ids = in.readLine().split(",");
            int id = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++)
                graph.addEdge(id, Integer.parseInt(ids[i]));
        }

        // Check if digraph has cycles
        validateDAG();

        // Create an instance of SAP class
        sap = new SAP(graph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        In in = new In();
        while (in.hasNextLine()) {
            String nounA = in.readString();
            String nounB = in.readString();
            System.out.println(wordnet.distance(nounA, nounB));
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validateString(word);
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        int i = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        validateAncestor(i);

        return sets.get(i);
    }

    private void validateAncestor(int v) {
        if (v == -1) throw new IllegalArgumentException("There is no common ancestor for that pair of vertices.");
    }

    private void validateDAG() {
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) throw new IllegalArgumentException("The graph contains a cycle.");

        boolean hasRoot = false;
        for (int i = 0; i < graph.V(); i++)
            if (graph.outdegree(i) == 0) {
                if (hasRoot) throw new IllegalArgumentException("The graph has more then one root.");
                hasRoot = true;
            }

        if (!hasRoot) throw new IllegalArgumentException("The graph does not have a root.");
    }

    private void validateString(String s) {
        if (s == null) throw new IllegalArgumentException("Required a string, got NULL instead.");
    }

    private void validateNoun(String noun) {
        if (!isNoun(noun)) throw new IllegalArgumentException("The key is not int the dictionary.");
    }
}