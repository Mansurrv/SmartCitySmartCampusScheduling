package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.algorithm.dag.DagPathSolver;
import org.example.algorithm.kahn.KahnTopologicalSort;
import org.example.algorithm.tarjan.SccTarjan;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String[] files = {
                "data/small/small_sparse_1.json",
                "data/small/small_medium_2.json",
                "data/small/small_dense_3.json",
                "data/medium/medium_sparse_4.json",
                "data/medium/medium_medium_5.json",
                "data/medium/medium_dense_6.json",
                "data/large/large_sparse_7.json",
                "data/large/large_medium_8.json",
                "data/large/large_dense_9.json"
        };

        ObjectMapper mapper = new ObjectMapper();
        String csvFile = "results/results.csv";
        FileWriter writer = new FileWriter(csvFile);

        writer.write("Graph,Nodes,Edges,Directed,Source,DFS_Visits,DFS_Edges,SCC_Count," +
                "Kahn_Time_ns,Kahn_QueueOps,Topo_Order,DAG_SP_Time_ns,Relaxations," +
                "DAG_Longest_Path_Length,Critical_Path_Comps,Critical_Path_Tasks\n");

        for (int i = 0; i < files.length; i++) {
            String filePath = files[i];
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("File not found: " + filePath);
                continue;
            }

            System.out.println("\n==== Processing file: " + filePath + " ====");

            Map<String, Object> json = mapper.readValue(file, Map.class);

            boolean directed = (boolean) json.get("directed");
            int n = (int) json.get("n");
            List<Map<String, Object>> edgesList = (List<Map<String, Object>>) json.get("edges");
            int source = (int) json.get("source");
            int edgeCount = edgesList.size();

            List<List<Integer>> graph = new ArrayList<>();
            List<SccTarjan.Edge> weightedEdges = new ArrayList<>();
            for (int j = 0; j < n; j++) graph.add(new ArrayList<>());

            for (Map<String, Object> edge : edgesList) {
                int u = (int) edge.get("u");
                int v = (int) edge.get("v");
                int w = (int) edge.get("w");

                graph.get(u).add(v);
                weightedEdges.add(new SccTarjan.Edge(u, v, w));
                if (!directed) graph.get(v).add(u);
            }

            SccTarjan scc = new SccTarjan(n, weightedEdges);
            long startTarjan = System.nanoTime();
            scc.run();
            long endTarjan = System.nanoTime();

            KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, scc.getSccs(), scc.getSccId());
            long startKahn = System.nanoTime();
            List<Integer> topoOrder = kahn.sort();
            long endKahn = System.nanoTime();
            long kahnTime = endKahn - startKahn;

            DagPathSolver dagSolverShortest = new DagPathSolver(
                    weightedEdges, scc.getSccId(), scc.getSccs(), topoOrder, DagPathSolver.Mode.SHORTEST
            );
            long startSP = System.nanoTime();
            DagPathSolver.ShortestPathResult spResult = dagSolverShortest.shortestPath(scc.getSccId()[source]);
            long endSP = System.nanoTime();
            long dagSPTime = endSP - startSP;

            DagPathSolver dagSolverLongest = new DagPathSolver(
                    weightedEdges, scc.getSccId(), scc.getSccs(), topoOrder, DagPathSolver.Mode.LONGEST
            );
            DagPathSolver.PathResult lpResult = dagSolverLongest.longestPath(scc.getSccId()[source]);

            String graphName = "graph_" + (i + 1);

            String row = String.format(
                    "%s,%d,%d,%b,%d,%d,%d,%d,%d,%d,\"%s\",%d,%d,%d,\"%s\",\"%s\"\n",
                    graphName,
                    n,
                    edgeCount,
                    directed,
                    source,
                    scc.dfsVisits,
                    scc.dfsEdges,
                    scc.getSccs().size(),
                    kahnTime,
                    kahn.getQueueOps(),
                    topoOrder.toString(),
                    dagSPTime,
                    dagSolverShortest.getRelaxationCount(),
                    lpResult.length,
                    lpResult.componentPath.toString(),
                    lpResult.taskPath.toString()
            );

            writer.write(row);
        }

        writer.close();
        System.out.println("\nAll results saved to " + csvFile);
    }
}
