import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {

    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private int numberOfTeams;
    private Map<String, Integer> teamIndexMap;
    private Map<Integer, String> indexTeamMap;
    private Map<Integer, Integer> teamIndexTeamVertexMap;
    private Map<Integer, Integer> teamVertexTeamIndexMap;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();

        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];

        teamIndexMap = new HashMap<>();
        indexTeamMap = new HashMap<>();

        for (int i = 0; i < numberOfTeams; i++) {
            String teamName = in.readString();
            teamIndexMap.put(teamName, i);
            indexTeamMap.put(i, teamName);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int x = 0; x < numberOfTeams; x++) {
                g[i][x] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    public Iterable<String> teams() {
        return teamIndexMap.keySet();
    }

    public int wins(String team) {
        validateTeam(team);
        return w[teamIndexMap.get(team)];
    }

    public int losses(String team) {
        validateTeam(team);
        return l[teamIndexMap.get(team)];
    }

    public int remaining(String team) {
        validateTeam(team);
        return r[teamIndexMap.get(team)];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        int t1 = teamIndexMap.get(team1);
        int t2 = teamIndexMap.get(team2);
        return g[t1][t2];
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        int teamIndex = teamIndexMap.get(team);
        int[] winTeam = mostWins();

        if (w[teamIndex] + r[teamIndex] < winTeam[1]) {
            return true;
        }

        FlowNetwork network = buildFlowNetwork(teamIndex);
        new FordFulkerson(network, 0, calculateVertexNumber() - 1);

        for (FlowEdge e : network.adj(0)) {
            if (e.flow() < e.capacity()) {
                return true;
            }
        }

        return false;
    }

    private int calculateVertexNumber() {
        int num = numberOfTeams;
        return ((num - 1) * (num - 2)) / 2 + num - 1 + 2;
    }

    private FlowNetwork buildFlowNetwork(int exclude) {
        int vertexNum = calculateVertexNumber();
        int source = 0;
        int sink = vertexNum - 1;
        int runningIndex = 1;
        FlowNetwork network = new FlowNetwork(vertexNum);
        teamIndexTeamVertexMap = new HashMap<>();
        teamVertexTeamIndexMap = new HashMap<>();

        for (int i = 0; i < numberOfTeams; i++) {
            if (i == exclude) {
                continue;
            }

            teamIndexTeamVertexMap.put(i, runningIndex);
            teamVertexTeamIndexMap.put(runningIndex, i);
            double cap = w[exclude] + r[exclude] - w[i];
            FlowEdge edge = new FlowEdge(runningIndex, sink, cap);
            network.addEdge(edge);
            runningIndex += 1;
        }

        for (int i = 0; i < numberOfTeams; i++) {
            if (i == exclude) {
                continue;
            }

            for (int x = i + 1; x < numberOfTeams; x++) {
                if (x == exclude) {
                    continue;
                }

                FlowEdge fromSource = new FlowEdge(source, runningIndex, g[i][x]);
                FlowEdge toTeam1 = new FlowEdge(runningIndex, teamIndexTeamVertexMap.get(i),
                                                Double.POSITIVE_INFINITY);
                FlowEdge toTeam2 = new FlowEdge(runningIndex, teamIndexTeamVertexMap.get(x),
                                                Double.POSITIVE_INFINITY);
                network.addEdge(fromSource);
                network.addEdge(toTeam1);
                network.addEdge(toTeam2);
                runningIndex += 1;
            }
        }

        return network;
    }

    private void validateTeam(String team) {
        if (!teamIndexMap.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }

    private int[] mostWins() {
        int currentWin = Integer.MIN_VALUE;
        int currentIndex = -1;
        for (int i = 0; i < w.length; i++) {
            if (w[i] > currentWin) {
                currentWin = w[i];
                currentIndex = i;
            }
        }
        int[] pair = new int[2];
        pair[0] = currentIndex;
        pair[1] = currentWin;
        return pair;
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        Stack<String> stack = new Stack<>();
        int teamIndex = teamIndexMap.get(team);
        int[] winTeam = mostWins();

        if (w[teamIndex] + r[teamIndex] < winTeam[1]) {
            stack.push(indexTeamMap.get(winTeam[0]));
            return stack;
        }

        FlowNetwork network = buildFlowNetwork(teamIndex);
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, calculateVertexNumber() - 1);

        for (FlowEdge e : network.adj(0)) {
            if (e.flow() < e.capacity()) {
                for (FlowEdge x : network.adj(calculateVertexNumber() - 1)) {
                    int other = x.other(calculateVertexNumber() - 1);
                    if (fordFulkerson.inCut(other)) {
                        stack.push(indexTeamMap.get(teamVertexTeamIndexMap.get(other)));
                    }
                }
                return stack;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
