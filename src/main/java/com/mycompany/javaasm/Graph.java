package com.mycompany.javaasm;

import java.util.*;

public class Graph {
    private Map<String, List<String>> adjList = new HashMap<>();

    // Add port
    public void addPort(String port) {
        if (!adjList.containsKey(port)) {
            adjList.put(port, new ArrayList<>());
            System.out.println("Port '" + port + "' added successfully!");
        } else {
            System.out.println(port + " already exists!");
        }
    }

    // Remove port
    public void removePort(String port) {
        if (adjList.containsKey(port)) {
            adjList.remove(port);
            for (List<String> neighbors : adjList.values()) {
                neighbors.remove(port);
            }
            System.out.println(port + " removed successfully!");
        } else {
            System.out.println("Port " + port + " not found!");
        }
    }

    // Add route
    public void addRoute(String port1, String port2) {
        if (adjList.containsKey(port1) && adjList.containsKey(port2)) {
            if (!adjList.get(port1).contains(port2)) adjList.get(port1).add(port2);
            if (!adjList.get(port2).contains(port1)) adjList.get(port2).add(port1);
            System.out.println("Route added between " + port1 + " and " + port2);
        } else {
            System.out.println("One or both ports not found!");
        }
    }

    // Remove route
    public void removeRoute(String port1, String port2) {
        if (adjList.containsKey(port1) && adjList.containsKey(port2)) {
            if (adjList.get(port1).contains(port2) && adjList.get(port2).contains(port1)) {
                adjList.get(port1).remove(port2);
                adjList.get(port2).remove(port1); 
                System.out.println("Route removed between " + port1 + " and " + port2);
            } else {
                System.out.println("No route exists between " + port1 + " and " + port2);
            }
        } else {
            System.out.println("One or both ports not found!");
        }
    }

    // Display network
    public void displayGraph() {
        System.out.println("\n--- International Ocean Shipping Network ---");
        for (String port : adjList.keySet()) {
            System.out.println(port + " -> " + adjList.get(port));
        }
    }

    // Search port
    public void searchPort(String port) {
        if (adjList.containsKey(port)) {
            System.out.println(port + " found! Connected to: " + adjList.get(port));
        } else {
            System.out.println("Port not found!");
        }
    }

    public List<String> getPorts() {
        return new ArrayList<>(adjList.keySet());
    }

    public List<String> getNeighbors(String port) {
        return adjList.getOrDefault(port, Collections.emptyList());
    }

    // BFS shortest path
    public List<String> shortestRouteBfs(String startPort, String endPort) {
        if (!adjList.containsKey(startPort) || !adjList.containsKey(endPort)) return Collections.emptyList();
        if (startPort.equals(endPort)) return Arrays.asList(startPort);

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(startPort);
        visited.add(startPort);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : adjList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    if (neighbor.equals(endPort)) {
                        return reconstructPath(parent, startPort, endPort);
                    }
                    queue.add(neighbor);
                }
            }
        }
        return Collections.emptyList(); // no path
    }

    private List<String> reconstructPath(Map<String, String> parent, String start, String end) {
        LinkedList<String> path = new LinkedList<>();
        String step = end;
        while (step != null) {
            path.addFirst(step);
            if (step.equals(start)) break;
            step = parent.get(step);
        }
        return path.isEmpty() || !path.getFirst().equals(start) ? Collections.emptyList() : path;
    }
}
