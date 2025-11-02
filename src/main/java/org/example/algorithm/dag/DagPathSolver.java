package org.example.algorithm.dag;

import org.example.algorithm.tarjan.SccTarjan;

import java.util.*;

public class DagPathSolver {

    private final int numComponents;
    private final List<Integer> topoOrder;
    private final List<List<Integer>> sccs;
    private final List<List<WeightedCompEdge>> weightedCompGraph;
    public static final int INF = Integer.MAX_VALUE / 2;
    private static final int NEG_INF = Integer.MIN_VALUE / 2;

    private int relaxationCount = 0;
    public int getRelaxationCount() { return relaxationCount; }

    private static class WeightedCompEdge {
        int toComp;
        int weight;
        public WeightedCompEdge(int toComp, int weight) { this.toComp = toComp; this.weight = weight; }
    }

    public record PathResult(int length, List<Integer> componentPath, List<Integer> taskPath) {
    }

    public record ShortestPathResult(Map<Integer, Integer> distances, Map<Integer, Integer> predecessor) {
    }

    public enum Mode { SHORTEST, LONGEST }

    public DagPathSolver(List<SccTarjan.Edge> allEdges, int[] sccId, List<List<Integer>> sccs, List<Integer> topoOrder, Mode mode) {
        this.sccs = sccs;
        this.numComponents = sccs.size();
        this.topoOrder = topoOrder;
        this.weightedCompGraph = new ArrayList<>();
        for (int i = 0; i < numComponents; i++) weightedCompGraph.add(new ArrayList<>());

        Map<Integer, Integer> edgeMap = new HashMap<>();
        for (SccTarjan.Edge edge : allEdges) {
            int compU = sccId[edge.u], compV = sccId[edge.v];
            if (compU != -1 && compV != -1 && compU != compV) {
                int key = compU * numComponents + compV;
                if (mode == Mode.SHORTEST) {
                    edgeMap.merge(key, edge.weight, Math::min);
                } else {
                    edgeMap.merge(key, edge.weight, Math::max);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : edgeMap.entrySet()) {
            int key = entry.getKey();
            int compU = key / numComponents, compV = key % numComponents;
            weightedCompGraph.get(compU).add(new WeightedCompEdge(compV, entry.getValue()));
        }
    }



    public ShortestPathResult shortestPath(int startComp) {
        relaxationCount = 0;
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> predecessor = new HashMap<>();
        for (int i = 0; i < numComponents; i++) { dist.put(i, INF); predecessor.put(i, -1); }
        dist.put(startComp, 0);

        for (int u : topoOrder) {
            int dU = dist.get(u);
            if (dU == INF) continue;
            for (WeightedCompEdge edge : weightedCompGraph.get(u)) {
                int v = edge.toComp;
                int newDist = dU + edge.weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    predecessor.put(v, u);
                    relaxationCount++;
                }
            }
        }
        return new ShortestPathResult(dist, predecessor);
    }

    public PathResult longestPath(int startComp) {
        relaxationCount = 0;
        Map<Integer, Integer> length = new HashMap<>();
        Map<Integer, Integer> predecessor = new HashMap<>();
        for (int i = 0; i < numComponents; i++) { length.put(i, NEG_INF); predecessor.put(i, -1); }
        length.put(startComp, 0);

        int maxLen = 0, endComp = startComp;
        for (int u : topoOrder) {
            int lU = length.get(u);
            if (lU == NEG_INF) continue;
            if (lU > maxLen) { maxLen = lU; endComp = u; }
            for (WeightedCompEdge edge : weightedCompGraph.get(u)) {
                int v = edge.toComp;
                int newLen = lU + edge.weight;
                if (newLen > length.get(v)) {
                    length.put(v, newLen);
                    predecessor.put(v, u);
                    relaxationCount++;
                }
            }
        }

        List<Integer> compPath = new ArrayList<>();
        Integer cur = endComp;
        while (cur != null && cur != -1 && cur != startComp) { compPath.addFirst(cur); cur = predecessor.get(cur); }
        if (cur != null && cur == startComp) compPath.addFirst(startComp);

        List<Integer> taskPath = new ArrayList<>();
        for (int c : compPath) taskPath.addAll(sccs.get(c));

        return new PathResult(maxLen, compPath, taskPath);
    }
}
