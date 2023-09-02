package com.villeneuve.justin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String in = args[0];
        String out = args[1];
        FlowNetwork G = readFile(in);
        G.edmondsKarp();
        writeFile(out, G.toString());
    }

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

    public static void writeFile(String output, String content) {
        ClassLoader classLoader = Main.class.getClassLoader();
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
