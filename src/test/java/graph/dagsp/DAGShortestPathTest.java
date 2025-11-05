package graph.dagsp;

import org.example.graph.dagsp.DAGShortestPath;
import org.example.util.GraphLoader;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        List<List<DAGShortestPath.WeightedEdge>> graph = new ArrayList<>();
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(1, 2), new DAGShortestPath.WeightedEdge(2, 5)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(3, 3)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(3, 1)));
        graph.add(new ArrayList<>());

        DAGShortestPath dagSP = new DAGShortestPath(graph, 4, "edge");
        List<Integer> topoOrder = Arrays.asList(0, 1, 2, 3);
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPaths(0, topoOrder);
        
        assertEquals(0, result.getDist()[0]);
        assertTrue(result.getDist()[1] < Integer.MAX_VALUE);
        assertTrue(result.getDist()[2] < Integer.MAX_VALUE);
    }

    @Test
    void testLongestPath() {
        List<List<DAGShortestPath.WeightedEdge>> graph = new ArrayList<>();
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(1, 2), new DAGShortestPath.WeightedEdge(2, 5)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(3, 3)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(3, 1)));
        graph.add(new ArrayList<>());

        DAGShortestPath dagSP = new DAGShortestPath(graph, 4, "edge");
        List<Integer> topoOrder = Arrays.asList(0, 1, 2, 3);
        DAGShortestPath.LongestPathResult result = dagSP.longestPath(0, topoOrder);
        
        assertEquals(0, result.getDist()[0]);
        assertTrue(result.getDist()[3] > 0);
    }

    @Test
    void testReconstructPath() {
        List<List<DAGShortestPath.WeightedEdge>> graph = new ArrayList<>();
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(1, 2)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(2, 3)));
        graph.add(new ArrayList<>());

        DAGShortestPath dagSP = new DAGShortestPath(graph, 3, "edge");
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPaths(0, topoOrder);
        
        List<Integer> path = dagSP.reconstructPath(0, 2, result.getParent());
        assertFalse(path.isEmpty());
        assertEquals(0, path.get(0));
        assertEquals(2, path.get(path.size() - 1));
    }

    @Test
    void testCriticalPath() {
        List<List<DAGShortestPath.WeightedEdge>> graph = new ArrayList<>();
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(1, 2)));
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(2, 5)));
        graph.add(new ArrayList<>());

        DAGShortestPath dagSP = new DAGShortestPath(graph, 3, "edge");
        List<Integer> topoOrder = Arrays.asList(0, 1, 2);
        DAGShortestPath.CriticalPathResult result = dagSP.findCriticalPath(topoOrder);
        
        assertNotNull(result.getPath());
        assertFalse(result.getPath().isEmpty());
        assertTrue(result.getLength() > 0);
    }

    @Test
    void testFromGraphLoader() throws Exception {
        GraphLoader.Graph graph = GraphLoader.loadGraph("tasks.json");
        DAGShortestPath dagSP = DAGShortestPath.fromGraphLoader(graph);
        
        assertNotNull(dagSP);
        assertEquals(graph.getWeightModel(), dagSP.getWeightModel());
    }

    @Test
    void testMetrics() {
        List<List<DAGShortestPath.WeightedEdge>> graph = new ArrayList<>();
        graph.add(Arrays.asList(new DAGShortestPath.WeightedEdge(1, 2)));
        graph.add(new ArrayList<>());

        DAGShortestPath dagSP = new DAGShortestPath(graph, 2, "edge");
        List<Integer> topoOrder = Arrays.asList(0, 1);
        dagSP.shortestPaths(0, topoOrder);
        
        assertTrue(dagSP.getMetrics().getRelaxations() > 0);
    }
}

