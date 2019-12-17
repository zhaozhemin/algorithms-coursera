import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.G = new Digraph(G);
    }

    private int[] helper(Integer v, Integer w, Iterable<Integer> vs, Iterable<Integer> ws) {
        BreadthFirstDirectedPaths vbfs;
        BreadthFirstDirectedPaths wbfs;

        if (v != null) {
            vbfs = new BreadthFirstDirectedPaths(G, v);
            wbfs = new BreadthFirstDirectedPaths(G, w);
        }
        else {
            vbfs = new BreadthFirstDirectedPaths(G, vs);
            wbfs = new BreadthFirstDirectedPaths(G, ws);
        }

        int mindist = Integer.MAX_VALUE;
        int sca = -1;

        for (int i = 0; i < G.V(); i++) {
            if (vbfs.hasPathTo(i) && wbfs.hasPathTo(i)) {
                int vdist = vbfs.distTo(i);
                int wdist = wbfs.distTo(i);
                if ((vdist + wdist) < mindist) {
                    mindist = vdist + wdist;
                    sca = i;
                }
            }
        }

        if (mindist == Integer.MAX_VALUE) {
            mindist = -1;
        }

        return new int[] { sca, mindist };
    }

    private void validateVertex(Integer v) {
        if (v == null || v < 0 || v >= G.V()) {
            throw new IllegalArgumentException();
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return helper(v, w, null, null)[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return helper(v, w, null, null)[0];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i : v) {
            validateVertex(i);
        }
        for (Integer i : w) {
            validateVertex(i);
        }
        return helper(null, null, v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i : v) {
            validateVertex(i);
        }
        for (Integer i : w) {
            validateVertex(i);
        }
        return helper(null, null, v, w)[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
