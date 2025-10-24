package com.mycompany.javaasm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DisplayMap {

    private static final int RADIUS = 20; // Circle radius
    private static final int CENTER_X = 400; // Center of the circle
    private static final int CENTER_Y = 300;
    private static final int CIRCLE_RADIUS = 200; // Radius of the "map" circle

    private static final AtomicBoolean FX_STARTED = new AtomicBoolean(false);

    private static void runOnFxThread(Runnable action) {
        if (FX_STARTED.compareAndSet(false, true)) {
            new Thread(() -> Platform.startup(() -> {
                Platform.setImplicitExit(false);
                action.run();
            })).start();
        } else {
            Platform.runLater(action);
        }
    }

    // Show overall map
    public static void show(Graph graph) {
        runOnFxThread(() -> openStage(graph, null));
    }

    // Show BFS path highlighted
    public static void showWithBfs(Graph graph, List<String> bfsPath) {
        runOnFxThread(() -> openStage(graph, bfsPath));
    }

    private static void openStage(Graph graph, List<String> bfsPath) {
        Stage stage = new Stage();
        stage.setTitle(bfsPath == null ? "Ocean Shipping Map" : "BFS Shortest Path Map");
        javafx.scene.Group group = createMapGroup(graph, bfsPath);
        Scene scene = new Scene(group, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Create the group with all ports and routes
    private static javafx.scene.Group createMapGroup(Graph graph, List<String> bfsPath) {
        javafx.scene.Group root = new javafx.scene.Group();

        List<String> ports = graph.getPorts();
        int n = ports.size();

        // Map port names to coordinates on a circle
        Map<String, double[]> coordinates = new HashMap<>();
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double x = CENTER_X + CIRCLE_RADIUS * Math.cos(angle);
            double y = CENTER_Y + CIRCLE_RADIUS * Math.sin(angle);
            coordinates.put(ports.get(i), new double[]{x, y});
        }

        // Draw edges
        for (String port : ports) {
            List<String> neighbors = graph.getNeighbors(port);
            for (String neighbor : neighbors) {
                // Avoid drawing duplicate lines
                if (ports.indexOf(neighbor) > ports.indexOf(port)) {
                    double[] from = coordinates.get(port);
                    double[] to = coordinates.get(neighbor);

                    Line line = new Line(from[0], from[1], to[0], to[1]);
                    line.setStroke(Color.GRAY);
                    line.setStrokeWidth(2);

                    // Highlight if part of BFS path
                    if (bfsPath != null && isEdgeInPath(port, neighbor, bfsPath)) {
                        line.setStroke(Color.RED);
                        line.setStrokeWidth(4);
                    }

                    root.getChildren().add(line);
                }
            }
        }

        // Draw ports as circles with names
        for (String port : ports) {
            double[] coord = coordinates.get(port);
            Circle circle = new Circle(coord[0], coord[1], RADIUS);
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLUE);

            Text text = new Text(coord[0] - RADIUS, coord[1] - RADIUS - 5, port);

            // Highlight BFS nodes
            if (bfsPath != null && bfsPath.contains(port)) {
                circle.setFill(Color.YELLOW);
                circle.setStroke(Color.RED);
            }

            root.getChildren().addAll(circle, text);
        }

        return root;
    }

    // Check if an edge is part of the BFS path
    private static boolean isEdgeInPath(String port1, String port2, List<String> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            String a = path.get(i);
            String b = path.get(i + 1);
            if ((a.equals(port1) && b.equals(port2)) || (a.equals(port2) && b.equals(port1))) {
                return true;
            }
        }
        return false;
    }
}
