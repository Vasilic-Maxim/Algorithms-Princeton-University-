import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    public String outcast(String[] nouns) {
        int n = nouns.length;
        int maxDistance = Integer.MIN_VALUE;
        String outcast = null;
        for (int i = 0; i < n - 1; i++)
            for (int j = i + 1; j < n; j++) {
                int distance = wordnet.distance(nouns[i], nouns[j]);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    outcast = nouns[i];
                }
            }

        return outcast;
    }
}