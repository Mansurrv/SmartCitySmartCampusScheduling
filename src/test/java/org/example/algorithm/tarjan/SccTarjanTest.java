package org.example.algorithm.tarjan;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SccTarjanTest {

    @Test
    void testSingleNodeGraph() {
        List<SccTarjan.Edge> edges = Collections.emptyList();
        SccTarjan scc = new SccTarjan(1, edges);
        scc.run();

        assertEquals(1, scc.getSccs().size());
        assertEquals(Collections.singletonList(0), scc.getSccs().get(0));
        assertEquals(0, scc.dfsEdges);
        assertEquals(1, scc.dfsVisits);
    }

    @Test
    void testSimpleAcyclicGraph() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 1),
                new SccTarjan.Edge(1, 2, 1)
        );
        SccTarjan scc = new SccTarjan(3, edges);
        scc.run();

        assertEquals(3, scc.getSccs().size());
        assertTrue(scc.getSccs().stream().flatMap(List::stream).toList().containsAll(Arrays.asList(0,1,2)));
        assertEquals(2, scc.dfsEdges);
        assertEquals(3, scc.dfsVisits);
    }

    @Test
    void testGraphWithCycle() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 1),
                new SccTarjan.Edge(1, 2, 1),
                new SccTarjan.Edge(2, 0, 1)
        );
        SccTarjan scc = new SccTarjan(3, edges);
        scc.run();

        assertEquals(1, scc.getSccs().size());
        assertEquals(Set.of(0,1,2), new HashSet<>(scc.getSccs().get(0)));
        assertEquals(3, scc.dfsVisits);
        assertEquals(3, scc.dfsEdges);
    }

    @Test
    void testDisconnectedGraph() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 1),
                new SccTarjan.Edge(2, 3, 1)
        );
        SccTarjan scc = new SccTarjan(4, edges);
        scc.run();

        assertEquals(4, scc.getSccs().size()); // each node is its own SCC
        Set<Integer> allNodes = new HashSet<>();
        for (List<Integer> component : scc.getSccs()) allNodes.addAll(component);
        assertEquals(Set.of(0,1,2,3), allNodes);
        assertEquals(2, scc.dfsEdges);
        assertEquals(4, scc.dfsVisits);
    }

    @Test
    void testMultipleSccs() {
        List<SccTarjan.Edge> edges = Arrays.asList(
                new SccTarjan.Edge(0, 1, 1),
                new SccTarjan.Edge(1, 0, 1),
                new SccTarjan.Edge(2, 3, 1),
                new SccTarjan.Edge(3, 2, 1),
                new SccTarjan.Edge(1, 2, 1)
        );
        SccTarjan scc = new SccTarjan(4, edges);
        scc.run();

        assertEquals(2, scc.getSccs().size());
        Set<Integer> firstScc = new HashSet<>(scc.getSccs().get(0));
        Set<Integer> secondScc = new HashSet<>(scc.getSccs().get(1));
        assertTrue(firstScc.equals(Set.of(0,1)) || secondScc.equals(Set.of(0,1)));
        assertTrue(firstScc.equals(Set.of(2,3)) || secondScc.equals(Set.of(2,3)));
        assertEquals(5, scc.dfsEdges);
        assertEquals(4, scc.dfsVisits);
    }
}
