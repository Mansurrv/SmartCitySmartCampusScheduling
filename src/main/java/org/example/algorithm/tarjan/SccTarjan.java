package org.example.algorithm.tarjan;

import java.util.*;

public class SccTarjan {

    private int N;
    private List<List<Integer>> graph; // Adjacency list
    private int[] ids; // Discovery time (ID) for each node
    private int[] low; // Lowest reachable discovery time (Low-link value)
    private boolean[] onStack; // Is the node currently on the stack?
    private Stack<Integer> stack; // DFS stack
    private int idCounter; // Counter for discovery time
    private List<List<Integer>> sccs; // List of found SCCs
    private int[] sccId; // Map node index to its SCC index

    public SccTarjan(int n, List<List<Integer>> adj) {
        this.N = n;
        this.graph = adj;
        this.ids = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new Stack<>();
        this.idCounter = 0;
        this.sccs = new ArrayList<>();
        this.sccId = new int[n];
        Arrays.fill(ids, -1); // Initialize all IDs to -1 (unvisited)
        Arrays.fill(sccId, -1);
    }

    public List<List<Integer>> findSccs() {
        for (int i = 0; i < N; i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }
        return sccs;
    }

    private void dfs(int u) {
        ids[u] = low[u] = idCounter++;
        stack.push(u);
        onStack[u] = true;

        for (int v : graph.get(u)) {
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

    public Set<String> getCondensationEdges() {
        Set<String> condensationEdges = new HashSet<>();
        for (int u = 0; u < N; u++) {
            int compU = sccId[u];
            for (int v : graph.get(u)) {
                int compV = sccId[v];
                if (compU != compV) {
                    condensationEdges.add("C" + compU + " -> C" + compV);
                }
            }
        }
        return condensationEdges;
    }

    public static void main(String[] args) {
        final int N = 8;
        List<List<Integer>> graph = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(3);
        graph.get(3).add(1);
        graph.get(4).add(5);
        graph.get(5).add(6);
        graph.get(6).add(7);
        graph.get(7).add(4);

        SccTarjan solver = new SccTarjan(N, graph);
        List<List<Integer>> sccs = solver.findSccs();

        System.out.println("--- Strongly Connected Components (SCCs) Analysis ---");
        System.out.println("\nSCC List:");
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> scc = sccs.get(i);
            System.out.printf("  Component %d: %s (Size: %d)\n", i, scc.toString(), scc.size());
        }
        System.out.println("\nTotal SCCs Found: " + sccs.size());

        Set<String> condensationEdges = solver.getCondensationEdges();

        System.out.println("\n--- Condensation Graph (DAG of Components) ---");
        System.out.println("Nodes of the Condensation Graph (Components):");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.printf("  Node C%d: %s\n", i, sccs.get(i).toString());
        }

        System.out.println("\nEdges of the Condensation Graph (Dependency Flow):");
        if (condensationEdges.isEmpty()) {
            System.out.println("  No dependencies between components found (all components are isolated).");
        } else {
            List<String> sortedEdges = new ArrayList<>(condensationEdges);
            Collections.sort(sortedEdges);
            for (String edge : sortedEdges) {
                System.out.printf("  %s (Represents a dependency flow)\n", edge);
            }
        }
    }
}
