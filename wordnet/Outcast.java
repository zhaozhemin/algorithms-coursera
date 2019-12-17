public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int distMax = Integer.MIN_VALUE;
        String outcastWord = null;

        for (String i : nouns) {
            int dist = 0;
            for (String x : nouns) {
                dist += wordnet.distance(i, x);
            }
            if (dist > distMax) {
                distMax = dist;
                outcastWord = i;
            }
        }

        return outcastWord;
    }

    public static void main(String[] args) {

    }
}
