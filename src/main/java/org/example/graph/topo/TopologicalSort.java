package org.example.graph.topo;

import org.example.util.Metrics;

import java.util.*;

public class TopologicalSort {
    private List<List<Integer>> graph;
    private int n;
    private Metrics metrics;


    public TopologicalSort(List<List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        this.metrics = new Metrics();
    }


    public List<Integer> kahnSort() {
        metrics.reset();
        metrics.startTiming();

        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueuePushes();
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueuePops();
            result.add(u);

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

    public List<Integer> dfsSort() {
        metrics.reset();
        metrics.startTiming();

        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        Stack<Integer> stack = new Stack<>();


        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (!dfs(i, visited, recStack, stack)) {
                    // Cycle detected
                    metrics.stopTiming();
                    return new ArrayList<>();
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        metrics.stopTiming();
        return result;
    }

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

    public List<Integer> sortOriginalVertices(List<List<Integer>> sccs, List<Integer> sccOrder) {
        List<Integer> result = new ArrayList<>();
        for (int sccIndex : sccOrder) {
            result.addAll(sccs.get(sccIndex));
        }
        return result;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}

