package util;

import org.example.util.GraphLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GraphLoaderTest {

    @Test
    void testLoadGraph() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("data/small_1_acyclic.json");

        assertNotNull(graph);
        assertEquals(8, graph.getN());
        assertTrue(graph.isDirected());
        assertEquals("edge", graph.getWeightModel());
        assertTrue(graph.getEdges().size() > 0);
    }

    @Test
    void testGraphProperties() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("tasks.json");
        assertEquals(4, graph.getSource());
        assertNotNull(graph.getEdges());

        for (GraphLoader.Edge edge : graph.getEdges()) {
            assertTrue(edge.getU() >= 0 && edge.getU() < graph.getN());
            assertTrue(edge.getV() >= 0 && edge.getV() < graph.getN());
            assertTrue(edge.getW() > 0);
        }
    }

    @Test
    void testLoadAllDatasets() throws Exception {
        String[] datasets = {
                "data/small_1_acyclic.json",
                "data/small_2_cyclic.json",
                "data/small_3_mixed.json",
                "data/medium_1_sparse.json",
                "data/medium_2_dense.json",
                "data/medium_3_multiple_scc.json",
                "data/large_1_sparse.json",
                "data/large_2_medium.json",
                "data/large_3_dense.json"
        };

        for (String dataset : datasets) {
            GraphLoader.Graph graph = GraphLoader.loadGraph(dataset);
            assertNotNull(graph);
            assertTrue(graph.getN() > 0);
            assertNotNull(graph.getEdges());
        }
    }
}
