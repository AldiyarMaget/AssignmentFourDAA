package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static class Graph {
        private final boolean directed;
        private final int n;
        private final List<Edge> edges;
        private final int source;
        private final String weightModel;

        public Graph(boolean directed, int n, List<Edge> edges, int source, String weightModel) {
            this.directed = directed;
            this.n = n;
            this.edges = edges;
            this.source = source;
            this.weightModel = weightModel;
        }

        public boolean isDirected() {
            return directed;
        }

        public int getN() {
            return n;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public int getSource() {
            return source;
        }

        public String getWeightModel() {
            return weightModel;
        }
    }

    public static class Edge {
        private final int u;
        private final int v;
        private final int w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }

        public int getW() {
            return w;
        }
    }

    public static Graph loadGraph(String filename) throws IOException {
        InputStream is = null;

        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/" + filename);
        }

        if (is == null) {
            File f = new File(filename);
            if (f.exists()) {
                is = new FileInputStream(f);
            } else {
                File f2 = new File("data/" + filename);
                if (f2.exists()) {
                    is = new FileInputStream(f2);
                } else {
                    if (filename.startsWith("data/")) {
                        File f3 = new File(filename);
                        if (f3.exists()) {
                            is = new FileInputStream(f3);
                        }
                    }
                }
            }
        }

        if (is == null) {
            throw new FileNotFoundException(filename + " not found in classpath or file system");
        }

        try (InputStream input = is) {
            JsonNode root = mapper.readTree(input);

            boolean directed = root.path("directed").asBoolean(true);
            int n = root.path("n").asInt();
            JsonNode edgesArray = root.path("edges");
            List<Edge> edges = new ArrayList<>();
            if (edgesArray.isArray()) {
                for (JsonNode edgeNode : edgesArray) {
                    int u = edgeNode.path("u").asInt();
                    int v = edgeNode.path("v").asInt();
                    int w = edgeNode.path("w").asInt();
                    edges.add(new Edge(u, v, w));
                }
            }
            int source = root.path("source").asInt(0);

            String weightModel = "edge";
            if (root.has("weight_model")) weightModel = root.get("weight_model").asText();
            else if (root.has("weightModel")) weightModel = root.get("weightModel").asText();

            return new Graph(directed, n, edges, source, weightModel);
        }
    }
}
