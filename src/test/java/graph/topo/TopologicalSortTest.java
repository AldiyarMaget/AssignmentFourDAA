package graph.topo;

import org.example.graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void testKahnSortSimple() {
        List<List<Integer>> graph = new ArrayList<>();
        graph.add(Arrays.asList(1, 2));
        graph.add(Arrays.asList(3));
        graph.add(Arrays.asList(3));
        graph.add(new ArrayList<>());

        TopologicalSort topo = new TopologicalSort(graph, 4);
        List<Integer> result = topo.kahnSort();
        
        assertEquals(4, result.size());
        assertTrue(result.contains(0));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    void testDfsSortSimple() {
        List<List<Integer>> graph = new ArrayList<>();
        graph.add(Arrays.asList(1, 2));
        graph.add(Arrays.asList(3));
        graph.add(Arrays.asList(3));
        graph.add(new ArrayList<>());

        TopologicalSort topo = new TopologicalSort(graph, 4);
        List<Integer> result = topo.dfsSort();
        
        assertEquals(4, result.size());
        assertTrue(result.contains(0));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    void testSortOriginalVertices() {
        List<List<Integer>> sccs = new ArrayList<>();
        sccs.add(Arrays.asList(0, 1, 2));
        sccs.add(Arrays.asList(3));
        sccs.add(Arrays.asList(4, 5));

        List<Integer> sccOrder = Arrays.asList(0, 1, 2);
        
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            graph.add(new ArrayList<>());
        }
        
        TopologicalSort topo = new TopologicalSort(graph, 3);
        List<Integer> result = topo.sortOriginalVertices(sccs, sccOrder);
        
        assertEquals(6, result.size());
        assertTrue(result.contains(0));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        assertTrue(result.contains(4));
        assertTrue(result.contains(5));
    }

    @Test
    void testMetrics() {
        List<List<Integer>> graph = new ArrayList<>();
        graph.add(Arrays.asList(1));
        graph.add(new ArrayList<>());

        TopologicalSort topo = new TopologicalSort(graph, 2);
        topo.kahnSort();
        
        assertTrue(topo.getMetrics().getQueuePushes() > 0);
        assertTrue(topo.getMetrics().getQueuePops() > 0);
    }
}

