package org.example.graph.dagsp;

import org.example.util.GraphLoader;
import org.example.util.Metrics;

import java.util.*;

public class DAGShortestPath {
    private final List<List<WeightedEdge>> graph;
    private final int n;
    private final String weightModel;
    private final Metrics metrics;

    public DAGShortestPath(List<List<WeightedEdge>> graph, int n, String weightModel) {
        this.graph = graph;
        this.n = n;
        this.weightModel = weightModel;
        this.metrics = new Metrics();
    }

    public static class WeightedEdge {
        private final int v;
        private final int weight;

        public WeightedEdge(int v, int weight) {
            this.v = v;
            this.weight = weight;
        }

        public int getV() {
            return v;
        }

        public int getWeight() {
            return weight;
        }
    }

    public static class ShortestPathResult {
        private final int[] dist;
        private final int[] parent;

        public ShortestPathResult(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }

        public int[] getDist() {
            return dist;
        }

        public int[] getParent() {
            return parent;
        }
    }


    public static class LongestPathResult {
        private final int[] dist;
        private final int[] parent;

        public LongestPathResult(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }

        public int[] getDist() {
            return dist;
        }

        public int[] getParent() {
            return parent;
        }
    }

    public static class CriticalPathResult {
        private final List<Integer> path;
        private final int length;

        public CriticalPathResult(List<Integer> path, int length) {
            this.path = path;
            this.length = length;
        }

        public List<Integer> getPath() {
            return path;
        }

        public int getLength() {
            return length;
        }
    }

    public ShortestPathResult shortestPaths(int source, List<Integer> topoOrder) {
        metrics.reset();
        metrics.startTiming();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        for (int u : topoOrder) {
            if (dist[u] == Integer.MAX_VALUE) continue;
            for (WeightedEdge edge : graph.get(u)) {
                metrics.incrementRelaxations();
                int v = edge.getV();
                int w = edge.getWeight();
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                }
            }
        }

        metrics.stopTiming();
        return new ShortestPathResult(dist, parent);
    }

    public LongestPathResult longestPath(int source, List<Integer> topoOrder) {
        metrics.reset();
        metrics.startTiming();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        for (int u : topoOrder) {
            if (dist[u] == Integer.MIN_VALUE) continue;
            for (WeightedEdge edge : graph.get(u)) {
                metrics.incrementRelaxations();
                int v = edge.getV();
                int w = edge.getWeight();
                if (dist[u] + w > dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                }
            }
        }

        metrics.stopTiming();
        return new LongestPathResult(dist, parent);
    }

    public CriticalPathResult findCriticalPath(List<Integer> topoOrder) {
        metrics.reset();
        metrics.startTiming();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        // treat every node as potential start with distance 0
        for (int u : topoOrder) {
            dist[u] = Math.max(dist[u], 0);
        }

        for (int u : topoOrder) {
            if (dist[u] == Integer.MIN_VALUE) continue;
            for (WeightedEdge edge : graph.get(u)) {
                metrics.incrementRelaxations();
                int v = edge.getV();
                int w = edge.getWeight();
                if (dist[u] + w > dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                }
            }
        }

        // find best endpoint
        int maxDist = Integer.MIN_VALUE;
        int target = -1;
        for (int i = 0; i < n; i++) {
            if (dist[i] != Integer.MIN_VALUE && dist[i] > maxDist) {
                maxDist = dist[i];
                target = i;
            }
        }

        List<Integer> path = new ArrayList<>();
        if (target != -1) {
            int cur = target;
            while (cur != -1) {
                path.add(cur);
                cur = parent[cur];
            }
            Collections.reverse(path);
        }

        metrics.stopTiming();
        return new CriticalPathResult(path, maxDist == Integer.MIN_VALUE ? 0 : maxDist);
    }


    public List<Integer> reconstructPath(int source, int target, int[] parent) {
        List<Integer> path = new ArrayList<>();
        if (target < 0 || target >= parent.length) return path;
        int cur = target;
        while (cur != -1) {
            path.add(cur);
            cur = parent[cur];
        }
        Collections.reverse(path);
        if (path.isEmpty() || path.get(0) != source) {
            return new ArrayList<>(); // no valid path from source to target
        }
        return path;
    }

    public static DAGShortestPath fromGraphLoader(GraphLoader.Graph graph) {
        int n = graph.getN();
        List<List<WeightedEdge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        for (GraphLoader.Edge e : graph.getEdges()) {
            adj.get(e.getU()).add(new WeightedEdge(e.getV(), e.getW()));
        }

        return new DAGShortestPath(adj, n, graph.getWeightModel());
    }

    public static DAGShortestPath fromAdjacency(List<List<WeightedEdge>> adj, int n, String weightModel) {
        return new DAGShortestPath(adj, n, weightModel);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public int getN() {
        return n;
    }
}