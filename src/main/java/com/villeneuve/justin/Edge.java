package com.villeneuve.justin;

public class Edge implements Comparable<Edge>{

    // attributes
    private final int u;
    private final int v;
    private final int capacity;
    private double flow;

    // constructor
    public Edge(int u, int v, int capacity, double flow) {
        this.u = u;
        this.v = v;
        this.capacity = capacity;
        this.flow = 0.0;
    }

    // getters and setters
    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFlow() {
        return flow;
    }

    public int from() {
        return u;
    }

    public int to() {
        return v;
    }

    /**
     * Given an edge and an endpoint vertex of that edge, returns the other endpoint vertex
     * @param vertex one of the endpoints of the edge
     * @return returns other vertex
     */
    public int other(int vertex) {
        if (vertex == u) return v;
        else if (vertex == v) return u;
        else throw new IllegalArgumentException("Invalid vertex");
    }

    /**
     * Used to find the values of capacity edges in the residual graph
     *
     * @return the residual capacity of an edge in the residual graph
     */
    public double getResidualCapacityTo(int vertex) {
        if (vertex == v) return capacity - flow;
        else if (vertex == u) return flow;
        else throw new IllegalArgumentException("Invalid vertex");
    }

    /**
     *
     * @param vertex adds flow to the edge (u, vertex)
     * @param amount the amount of flow added to the edge
     */
    public void addFLowTo(int vertex, double amount) {
        if (vertex == u) flow -= amount;
        else if (vertex == v) flow += amount;
        else throw new IllegalArgumentException("Invalid vertex");
    }

    @Override
    public int compareTo(Edge other) {
        if (this.flow < other.flow) return -1;
        else if (this.flow > other.flow) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "u=" + u +
                ", v=" + v +
                ", capacity=" + capacity +
                ", flow=" + flow +
                '}';
    }
}
