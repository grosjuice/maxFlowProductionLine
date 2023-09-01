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

        int n = Integer.parseInt(scanner.nextLine()); // num of vertices
        int m = Integer.parseInt(scanner.nextLine()); // num of edges
        LinkedList<Edge>[] adj = new LinkedList[n];
        Map<String, Edge> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            adj[i] = new LinkedList<>();
        }

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            int w = Integer.parseInt(line[2]); // weight (the capacity)
            Edge e = new Edge(u, v, w, 0);
            adj[u].add(e);
            adj[v].add(e);
            map.put(u + " " + v, e);
        }

        return new FlowNetwork(n, m, adj, map);
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
