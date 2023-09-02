package com.villeneuve.justin;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FlowNetwork {
    private int n; // number of vertices
    private int m; // number of edges
    private double flowValue; // the current flow value
    private LinkedList<Edge>[] adj; // adjacency list
    private Map<String, Edge> map;
    private int s; // source
    private int t; // sink
    private Edge[] edgeTo;
    private final double[] vertexCapacities;

    public FlowNetwork(int n, int m, LinkedList<Edge>[] adj, Map<String, Edge> map, double[] vertexCapacities) {
        this.n = n;
        this.m = m;
        this.flowValue = 0.0;
        this.adj = adj;
        this.map = map;
        this.s = 0;
        this.t = n - 1;
        this.vertexCapacities = vertexCapacities;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }


    /**
     * get the adjacency list of some vertex u
     *
     * @param u a vertex
     * @return all edges incident to vertex u
     */
    public LinkedList<Edge> getAdj(int u) {
        return adj[u];
    }

    /**
     * runs a bfs to determine an augmented path in the residual graph, if it exists.
     * An augmented path was found iff t was visited.
     *
     * @return true if t was visited. False otherwise.
     */
    public boolean hasAugmentedPath() {
        int n = getN();
        boolean[] visited = new boolean[n];
        edgeTo = new Edge[n]; // array that tells us which edge was used to in the augmented path

        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            visited[u] = true;

            for (Edge e : getAdj(u)) { // (u,v)
                int v = e.other(u);

                // residual capacity > 0 means there is an edge from u to v in the residual network
                if (e.getResidualCapacityTo(v) > 0 && !visited[v]) {
                    edgeTo[v] = e;
                    queue.add(v);
                }
            }
        }

        return visited[t];
    }

    /**
     * Edmonds karp algorithm is the ford fulkerson algo to find max flow of a flow network but using bfs to find
     * aygmented paths in the residual network.
     *
     * @return the max flow of the flow network
     */
    public double edmondsKarp() {
        while (hasAugmentedPath()) {
            double bottleneck = Double.POSITIVE_INFINITY;

            // find the bottleneck in the augmented path p, i.e., the edge with the min weight that lies in p
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottleneck = Math.min(bottleneck, edgeTo[v].getResidualCapacityTo(v));
            }

            // augment the flow
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addFLowTo(v, bottleneck);
            }
            flowValue += bottleneck;
        }

        return flowValue;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        // edge output
        for (Edge e : map.values()) {
            String edge = "Edge : (" + e.getU() + "," + e.getV() + ")";
            String flow = "flow = " + e.getFlow();
            String capacity = "capacity = " + e.getCapacity();

            s.append(edge + "\t\t" + flow + "\t\t" + capacity + "\n");
        }

        s.append("---\n");

        // vertex output
        double[] vertexOutFlows = new double[n];
        for (int i = 0; i < n; i++) {
            double outflow = 0;
            for (Edge e : adj[i]) {
                if (e.getU() == i) {
                    outflow += e.getFlow();
                }
            }
            vertexOutFlows[i] = outflow;
        }

        for (int i = 0; i < n; i++) {
            double epsilon = 1E-10;

            String vertex = "vertex : " + i;
            String flow = "flow = " + vertexOutFlows[i];
            String capacity = "capacity : " + vertexCapacities[i];
            String bottleneck = "";

            if (Math.abs(vertexCapacities[i] - vertexOutFlows[i]) < epsilon) {
                bottleneck += "bottleneck";
            }

            s.append(vertex + "\t\t" + flow + "\t\t" + capacity + "\t\t" + bottleneck + "\n");
        }



        return s.toString();
    }
}
