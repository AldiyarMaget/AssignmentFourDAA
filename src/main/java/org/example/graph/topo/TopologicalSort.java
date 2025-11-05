package org.example.graph.topo;

import org.example.util.Metrics;

import java.util.*;

/**
 * Topological sorting algorithms for directed acyclic graphs (DAGs).
 * Implements both Kahn's algorithm and DFS-based approach.
 */
public class TopologicalSort {
    private List<List<Integer>> graph;
    private int n;
    private Metrics metrics;

    /**
     * Constructor for TopologicalSort.
     * @param graph adjacency list representation of the DAG
     * @param n number of vertices
     */
    public TopologicalSort(List<List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        this.metrics = new Metrics();
    }

    /**
     * Kahn's algorithm for topological sorting.
     * Uses queue-based BFS approach with in-degree counting.
     * @return topological order of vertices, or empty list if cycle detected
     */
    public List<Integer> kahnSort() {
        metrics.reset();
        metrics.startTiming();

        // Compute in-degree for each vertex
        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        // Initialize queue with vertices of in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueuePushes();
            }
        }

        // Process vertices in topological order
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueuePops();
            result.add(u);

            // Remove u and update in-degrees of neighbors
            for (int v : graph.get(u)) {
                metrics.incrementEdgeTraversals();
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementQueuePushes();
                }
            }
        }

        metrics.stopTiming();
        return result;
    }

    /**
     * DFS-based topological sorting.
     * @return topological order of vertices, or empty list if cycle detected
     */
    public List<Integer> dfsSort() {
        metrics.reset();
        metrics.startTiming();

        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        Stack<Integer> stack = new Stack<>();

        // Perform DFS from each unvisited vertex
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (!dfs(i, visited, recStack, stack)) {
                    // Cycle detected
                    metrics.stopTiming();
                    return new ArrayList<>();
                }
            }
        }

        // Reverse stack to get topological order
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        metrics.stopTiming();
        return result;
    }

    /**
     * Helper DFS method for topological sort.
     * Detects cycles and builds ordering.
     */
    private boolean dfs(int u, boolean[] visited, boolean[] recStack, Stack<Integer> stack) {
        visited[u] = true;
        recStack[u] = true;
        metrics.incrementDfsVisits();

        for (int v : graph.get(u)) {
            metrics.incrementEdgeTraversals();
            if (!visited[v]) {
                if (!dfs(v, visited, recStack, stack)) {
                    return false;
                }
            } else if (recStack[v]) {
                return false;
            }
        }

        recStack[u] = false;
        stack.push(u);
        return true;
    }

    /**
     * Sort original vertices based on SCC component order.
     * @param sccs list of strongly connected components
     * @param sccOrder topological order of SCCs
     * @return topological order of original vertices
     */
    public List<Integer> sortOriginalVertices(List<List<Integer>> sccs, List<Integer> sccOrder) {
        List<Integer> result = new ArrayList<>();
        for (int sccIndex : sccOrder) {
            result.addAll(sccs.get(sccIndex));
        }
        return result;
    }

    /**
     * Get metrics for algorithm performance tracking.
     * @return Metrics object with operation counts and timing
     */
    public Metrics getMetrics() {
        return metrics;
    }
}

