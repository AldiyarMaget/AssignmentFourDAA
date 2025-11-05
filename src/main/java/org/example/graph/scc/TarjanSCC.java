package org.example.graph.scc;

import org.example.util.Metrics;
import org.example.util.GraphLoader;

import java.util.*;

public class TarjanSCC {
    private List<List<Integer>> graph;
    private int n;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private int time;
    private List<List<Integer>> sccs;
    private Metrics metrics;

    public TarjanSCC(List<List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        this.disc = new int[n];
        Arrays.fill(this.disc, -1);
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new ArrayDeque<>();
        this.time = 0;
        this.sccs = new ArrayList<>();
        this.metrics = new Metrics();
    }

    public List<List<Integer>> findSCCs() {
        metrics.reset();
        metrics.startTiming();
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i);
            }
        }
        metrics.stopTiming();
        return new ArrayList<>(sccs);
    }

    private void dfs(int u) {
        metrics.incrementDfsVisits();
        disc[u] = time;
        low[u] = time;
        time++;
        stack.push(u);
        onStack[u] = true;

        for (int v : graph.get(u)) {
            metrics.incrementEdgeTraversals();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> component = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                component.add(v);
            } while (v != u);
            sccs.add(component);
        }
    }

    public List<List<Integer>> buildCondensationGraph() {
        Map<Integer, Integer> vertexToSCC = getVertexToSCC();
        int numSCCs = sccs.size();
        List<Set<Integer>> condensation = new ArrayList<>();
        for (int i = 0; i < numSCCs; i++) condensation.add(new HashSet<>());

        for (int u = 0; u < n; u++) {
            int sU = vertexToSCC.get(u);
            for (int v : graph.get(u)) {
                int sV = vertexToSCC.get(v);
                if (sU != sV) {
                    condensation.get(sU).add(sV);
                }
            }
        }

        List<List<Integer>> result = new ArrayList<>();
        for (Set<Integer> s : condensation) result.add(new ArrayList<>(s));
        return result;
    }

    public Map<Integer, Integer> getVertexToSCC() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int s = 0; s < sccs.size(); s++) {
            for (int v : sccs.get(s)) {
                map.put(v, s);
            }
        }
        return map;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public static TarjanSCC fromGraphLoader(GraphLoader.Graph g) {
        int n = g.getN();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (GraphLoader.Edge e : g.getEdges()) {
            adj.get(e.getU()).add(e.getV());
        }
        return new TarjanSCC(adj, n);
    }
}

