package org.example.algorithm.kahn;
import java.util.*;

public class KahnTopologicalSort {

    private final int N;
    private final List<List<Integer>> graph;
    private final List<List<Integer>> sccs;
    private final int[] sccId;

    private int queueOps = 0;
    public int getQueueOps() { return queueOps; }

    public KahnTopologicalSort(int n, List<List<Integer>> graph, List<List<Integer>> sccs, int[] sccId) {
        this.N = n;
        this.graph = graph;
        this.sccs = sccs;
        this.sccId = sccId;
    }

    public List<Integer> sort() {
        int numComponents = sccs.size();
        List<Set<Integer>> compAdj = new ArrayList<>();
        int[] inDegree = new int[numComponents];

        for (int i = 0; i < numComponents; i++) compAdj.add(new HashSet<>());

        for (int u = 0; u < N; u++) {
            int compU = sccId[u];
            if (compU == -1) continue;

            for (int v : graph.get(u)) {
                int compV = sccId[v];
                if (compV == -1) continue;
                if (compU != compV && compAdj.get(compU).add(compV)) {
                    inDegree[compV]++;
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numComponents; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
                queueOps++;
            }
        }

        List<Integer> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            queueOps++; // count poll
            sortedOrder.add(u);

            for (int v : compAdj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                    queueOps++; // count push
                }
            }
        }

        if (sortedOrder.size() != numComponents) {
            System.err.println("Error: Graph contains a cycle!");
            return Collections.emptyList();
        }
        return sortedOrder;
    }
}
