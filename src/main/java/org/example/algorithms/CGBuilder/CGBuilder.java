package org.example.algorithms.CGBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CGBuilder {
    public static List<List<Integer>> build(List<List<Integer>> adj, int[] compID, int scc) {
        List<List<Integer>> adjList = new ArrayList<>(scc);
        for (int i = 0; i < scc; i++) {
            adjList.add(new ArrayList<>());
        }

        Set<String> dagEdges = new HashSet<>();
        for (int u = 0; u < adj.size(); u++) {
            int compU = compID[u];
            for (int v : adj.get(u)) {
                int compV = compID[v];
                if (compU != compV) {
                    String key = compU + "->" + compV;
                    if (!dagEdges.add(key)) {
                        adjList.get(compU).add(compV);
                    }
                }
            }
        }
        return adjList;
    }
}
