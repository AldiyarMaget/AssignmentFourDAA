package graph.scc;

import org.example.graph.scc.TarjanSCC;
import org.example.graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import org.example.util.GraphLoader;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void testSimpleGraph() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("tasks.json");
        TarjanSCC tarjan = TarjanSCC.fromGraphLoader(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertNotNull(sccs);
        assertTrue(sccs.size() > 0);
        
        int totalVertices = 0;
        for (List<Integer> scc : sccs) {
            totalVertices += scc.size();
        }
        assertEquals(graph.getN(), totalVertices);
    }

    @Test
    void testCondensationGraph() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("tasks.json");
        TarjanSCC tarjan = TarjanSCC.fromGraphLoader(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        List<List<Integer>> condensation = tarjan.buildCondensationGraph();
        
        assertEquals(sccs.size(), condensation.size());
        
        TopologicalSort topo = new TopologicalSort(condensation, condensation.size());
        List<Integer> topoOrder = topo.kahnSort();
        assertEquals(condensation.size(), topoOrder.size());
    }

    @Test
    void testVertexToSCCMapping() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("tasks.json");
        TarjanSCC tarjan = TarjanSCC.fromGraphLoader(graph);
        tarjan.findSCCs();
        Map<Integer, Integer> vertexToSCC = tarjan.getVertexToSCC();
        
        assertEquals(graph.getN(), vertexToSCC.size());
        for (int i = 0; i < graph.getN(); i++) {
            assertTrue(vertexToSCC.containsKey(i));
        }
    }

    @Test
    void testAcyclicGraph() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("data/small_1_acyclic.json");
        TarjanSCC tarjan = TarjanSCC.fromGraphLoader(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(graph.getN(), sccs.size());
    }

    @Test
    void testCyclicGraph() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("data/small_2_cyclic.json");
        TarjanSCC tarjan = TarjanSCC.fromGraphLoader(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertTrue(sccs.size() < graph.getN());
        boolean hasCycle = false;
        for (List<Integer> scc : sccs) {
            if (scc.size() > 1) {
                hasCycle = true;
                break;
            }
        }
        assertTrue(hasCycle);
    }
}

