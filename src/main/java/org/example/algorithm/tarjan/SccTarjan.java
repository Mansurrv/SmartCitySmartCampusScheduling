package org.example.algorithm.tarjan;
import java.util.*;

public class SccTarjan {

    private int N;
    private List<List<Integer>> graph;
    private List<Edge> allEdges;
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int idCounter;
    private List<List<Integer>> sccs;
    private int[] sccId;

    public int dfsVisits = 0;
    public int dfsEdges = 0;

    public static class Edge {
        public int u;
        public int v;
        public int weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    public SccTarjan(int n, List<Edge> edges) {
        this.N = n;
        this.allEdges = edges;

        this.graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (Edge edge : edges) graph.get(edge.u).add(edge.v);

        this.ids = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new Stack<>();
        this.idCounter = 0;
        this.sccs = new ArrayList<>();
        this.sccId = new int[n];
        Arrays.fill(ids, -1);
        Arrays.fill(sccId, -1);
    }

    private void dfs(int u) {
        dfsVisits++;
        ids[u] = low[u] = idCounter++;
        stack.push(u);
        onStack[u] = true;

        for (int v : graph.get(u)) {
            dfsEdges++;
            if (ids[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], ids[v]);
            }
        }

        if (ids[u] == low[u]) {
            List<Integer> newScc = new ArrayList<>();
            int componentId = sccs.size();
            while (!stack.isEmpty()) {
                int node = stack.pop();
                onStack[node] = false;
                sccId[node] = componentId;
                newScc.add(node);
                if (node == u) break;
            }
            Collections.sort(newScc);
            sccs.add(newScc);
        }
    }

    public void run() {
        for (int i = 0; i < N; i++) {
            if (ids[i] == -1) dfs(i);
        }
    }

    public List<List<Integer>> getSccs() { return sccs; }
    public int[] getSccId() { return sccId; }
    public int getN() { return N; }
    public List<Edge> getAllEdges() { return allEdges; }
}
