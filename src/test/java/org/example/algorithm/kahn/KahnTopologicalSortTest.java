package org.example.algorithm.kahn;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KahnTopologicalSortTest {

    @Test
    void testSimpleDAG() {
        int n = 3;
        List<List<Integer>> graph = Arrays.asList(
                Arrays.asList(1),
                Arrays.asList(2),
                Collections.emptyList()
        );
        int[] sccId = {0, 1, 2};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2)
        );

        KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, sccs, sccId);
        List<Integer> topoOrder = kahn.sort();

        assertEquals(Arrays.asList(0, 1, 2), topoOrder);
        assertTrue(kahn.getQueueOps() > 0);
    }

    @Test
    void testMultipleComponentsDAG() {
        int n = 4;
        List<List<Integer>> graph = Arrays.asList(
                Arrays.asList(1),
                Collections.emptyList(),
                Arrays.asList(3),
                Collections.emptyList()
        );
        int[] sccId = {0, 0, 1, 1};
        List<List<Integer>> sccs = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(2, 3)
        );

        KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, sccs, sccId);
        List<Integer> topoOrder = kahn.sort();

        assertEquals(2, topoOrder.size());
        assertEquals(Set.of(0, 1), new HashSet<>(topoOrder));
        assertTrue(kahn.getQueueOps() > 0);
    }

    @Test
    void testSingleNodeGraph() {
        int n = 1;
        List<List<Integer>> graph = Collections.singletonList(Collections.emptyList());
        int[] sccId = {0};
        List<List<Integer>> sccs = Collections.singletonList(Collections.singletonList(0));

        KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, sccs, sccId);
        List<Integer> topoOrder = kahn.sort();

        assertEquals(Collections.singletonList(0), topoOrder);
        assertEquals(2, kahn.getQueueOps());
    }

    @Test
    void testGraphWithCycle() {
        int n = 3;
        List<List<Integer>> graph = Arrays.asList(
                Arrays.asList(1),
                Arrays.asList(2),
                Arrays.asList(0)
        );
        int[] sccId = {0, 1, 2};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2)
        );

        KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, sccs, sccId);
        List<Integer> topoOrder = kahn.sort();

        assertTrue(topoOrder.isEmpty(), "Topological sort should be empty due to cycle");
    }

    @Test
    void testDisconnectedGraph() {
        int n = 4;
        List<List<Integer>> graph = Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList(),
                Arrays.asList(3),
                Collections.emptyList()
        );
        int[] sccId = {0, 1, 2, 3};
        List<List<Integer>> sccs = Arrays.asList(
                Collections.singletonList(0),
                Collections.singletonList(1),
                Collections.singletonList(2),
                Collections.singletonList(3)
        );

        KahnTopologicalSort kahn = new KahnTopologicalSort(n, graph, sccs, sccId);
        List<Integer> topoOrder = kahn.sort();

        assertEquals(4, topoOrder.size());
        assertTrue(kahn.getQueueOps() > 0);
        assertTrue(new HashSet<>(topoOrder).containsAll(Set.of(0, 1, 2, 3)));
    }
}
