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
        String outcast = null;
        int maxDistance = 0;
        for (String nounA : nouns) {
            int distance = 0;
            for (String nounB : nouns)
                distance += wordnet.distance(nounA, nounB);

            if (distance > maxDistance) {
                outcast = nounA;
                maxDistance = distance;
            }
        }

        return outcast;
    }
}