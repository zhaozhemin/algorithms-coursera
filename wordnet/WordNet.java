import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {

    private HashMap<String, List<Integer>> wordSynsetId;
    private HashMap<Integer, String> synsetIdWord;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        In synsetsFile = new In(synsets);
        wordSynsetId = new HashMap<>();
        synsetIdWord = new HashMap<>();

        while (synsetsFile.hasNextLine()) {
            String line = synsetsFile.readLine();
            String[] fields = line.split(",");
            synsetIdWord.put(Integer.parseInt(fields[0]), fields[1]);
            String[] words = fields[1].split(" ");
            for (String w : words) {
                List<Integer> ws = wordSynsetId.get(w);
                if (ws == null) {
                    ws = new ArrayList<>();
                    wordSynsetId.put(w, ws);
                }
                ws.add(Integer.parseInt(fields[0]));
            }
        }

        List<String> lines = new ArrayList<>();
        In hypernymsFile = new In(hypernyms);

        while (hypernymsFile.hasNextLine()) {
            String line = hypernymsFile.readLine();
            lines.add(line);
        }

        G = new Digraph(lines.size());

        for (String line : lines) {
            String[] id = line.split(",");
            for (int i = 1; i < id.length; i++) {
                G.addEdge(Integer.parseInt(id[0]), Integer.parseInt(id[i]));
            }
        }

        DirectedCycle directedCycle = new DirectedCycle(G);

        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordSynsetId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        return wordSynsetId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        List<Integer> synsetA = wordSynsetId.get(nounA);
        List<Integer> synsetB = wordSynsetId.get(nounB);
        return sap.length(synsetA, synsetB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        List<Integer> synsetA = wordSynsetId.get(nounA);
        List<Integer> synsetB = wordSynsetId.get(nounB);
        int ancestor = sap.ancestor(synsetA, synsetB);
        return synsetIdWord.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
