package org.example.algorithm.dag;

import org.example.algorithm.tarjan.SccTarjan;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DagPathSolverTest {

    @Test
    void testShortestPathSimpleGraph() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 5),
                new SccTarjan.Edge(1, 2, 10)
        );
        int[] sccId = {0, 1, 2};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2)
        );
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagPathSolver solver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.SHORTEST);
        DagPathSolver.ShortestPathResult sp = solver.shortestPath(0);

        assertEquals(0, sp.distances.get(0));
        assertEquals(5, sp.distances.get(1));
        assertEquals(15, sp.distances.get(2));
        assertEquals(2, solver.getRelaxationCount());
    }

    @Test
    void testLongestPathSimpleGraph() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 2),
                new SccTarjan.Edge(0, 2, 3),
                new SccTarjan.Edge(1, 3, 4),
                new SccTarjan.Edge(2, 3, 1)
        );
        int[] sccId = {0, 1, 2, 3};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2),
                Collections.singletonList(3)
        );
        List<Integer> topoOrder = Arrays.asList(0, 1, 2, 3);

        DagPathSolver solver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.LONGEST);
        DagPathSolver.PathResult lp = solver.longestPath(0);

        assertEquals(6, lp.length);
        assertEquals(Arrays.asList(0, 1, 3), lp.componentPath);
        assertEquals(Arrays.asList(0, 1, 3), lp.taskPath);
        assertEquals(3, solver.getRelaxationCount());
    }

    @Test
    void testDagWithMultipleEdges() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 2),
                new SccTarjan.Edge(0, 1, 5),
                new SccTarjan.Edge(1, 2, 3)
        );
        int[] sccId = {0, 1, 2};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2)
        );
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagPathSolver lpSolver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.LONGEST);
        DagPathSolver.PathResult lp = lpSolver.longestPath(0);
        assertEquals(8, lp.length);
        assertEquals(Arrays.asList(0, 1, 2), lp.componentPath);

        DagPathSolver spSolver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.SHORTEST);
        DagPathSolver.ShortestPathResult sp = spSolver.shortestPath(0);
        assertEquals(2, sp.distances.get(1));  // minimum weight among 0->1 edges
        assertEquals(5, sp.distances.get(2));  // shortest path: 0->1->2
    }

    @Test
    void testDisconnectedGraph() {
        List<SccTarjan.Edge> edges = Collections.singletonList(
                new SccTarjan.Edge(0, 1, 3)
        );
        int[] sccId = {0, 1, 2};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2)
        );
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);

        DagPathSolver solver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.SHORTEST);
        DagPathSolver.ShortestPathResult sp = solver.shortestPath(0);
        assertEquals(DagPathSolver.INF, sp.distances.get(2));
    }

    @Test
    void testSingleNodeGraph() {
        List<SccTarjan.Edge> edges = Collections.emptyList();
        int[] sccId = {0};
        List<List<Integer>> sccs = Collections.singletonList(Collections.singletonList(0));
        List<Integer> topoOrder = Collections.singletonList(0);

        DagPathSolver spSolver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.SHORTEST);
        DagPathSolver.ShortestPathResult sp = spSolver.shortestPath(0);

        DagPathSolver lpSolver = new DagPathSolver(edges, sccId, sccs, topoOrder, DagPathSolver.Mode.LONGEST);
        DagPathSolver.PathResult lp = lpSolver.longestPath(0);

        assertEquals(0, sp.distances.get(0));
        assertEquals(0, lp.length);
        assertEquals(Collections.singletonList(0), lp.componentPath);
    }
}
