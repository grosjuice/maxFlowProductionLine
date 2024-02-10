package com.villeneuve.justin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * The Max Flow Problem involves finding the maximum amount of flow that can be sent from a designated source node
 * to a designated sink node in a flow network. A flow network is represented by a directed graph where each edge has
 * a capacity indicating the maximum amount of flow it can carry. The goal is to determine the maximum flow from the
 * source to the sink while respecting capacity constraints and maintaining flow conservation at intermediate nodes.
 *
 * This code is a variant of the common max flow problem, where the capacities reside on the edges. Here, the capacities
 * are initially on the nodes. This allows to model other types of problems, like determining the max flow in a production
 * line.
 */

public class Main {
    public static void main(String[] args) {
        String in = args[0]; // input file
        String out = args[1]; // output file
        FlowNetwork G = readFile(in);
        G.edmondsKarp(); // run edmonds karp algorithm to find max flow in graph
        writeFile(out, G.toString());
    }

    /**
     *
     * @param inputFile the graph to apply the edmond's karp algorithm (for the maxflow problem).
     * @return the flow network
     */
    public static FlowNetwork readFile(String inputFile) {
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource(inputFile).getFile());

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // find number of vertices and edges in the graph
        int n = Integer.parseInt(scanner.nextLine()); // num of vertices
        int m = Integer.parseInt(scanner.nextLine()); // num of edges
        String s = scanner.nextLine(); // get rid of ---
        LinkedList<Edge>[] adj = new LinkedList[n];
        Map<String, Edge> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            adj[i] = new LinkedList<>();
        }

        double[] capacities = new double[n];

        // find capacities of vertices
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.equals("---")) break;

            String[] lineSplit = line.split(" ");

            int v = Integer.valueOf(lineSplit[0]);
            double capacity;

            if (lineSplit[1].equals("INFINITY")) {
                capacity = Double.POSITIVE_INFINITY;
            } else {
                capacity = Double.valueOf(lineSplit[1]);
            }

            capacities[v] = capacity;
        }

        // find edges
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            Edge e = new Edge(u, v);
            adj[u].add(e);
            adj[v].add(e);
            map.put(u + " " + v, e);
        }

        // determine the out degree of each vertex in order to calculate the capacity of each edge
        int[] outDegrees = new int[n];

        for (int i = 0; i < n; i++) {
            int outDegree = 0;

            for (Edge e : adj[i]) {
                int from = e.getU();
                if (from == i) {
                    outDegree++;
                }
            }
            outDegrees[i] = outDegree;
        }

        // set capacities of edges
        for (int i = 0 ; i < n; i++) {
            for (Edge e : adj[i]) {
                int from = e.getU();
                if (from == i) {
                    if (outDegrees[i] == 0) {
                        e.setCapacity(capacities[i]);
                    } else {
                        e.setCapacity(capacities[i] / outDegrees[i]);
                    }
                }
            }
        }

        return new FlowNetwork(n, m, adj, map, capacities);
    }

    /**
     *
     * writes the results to a file
     *
     * @param output the output file
     * @param content the content written to the file
     */
    public static void writeFile(String output, String content) {
        String path = System.getProperty("user.dir") + "/data/" + output;


        FileWriter out = null;
        try {
            out = new FileWriter(path, true);
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
