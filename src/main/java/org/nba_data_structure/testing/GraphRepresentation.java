package org.nba_data_structure.testing;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GraphRepresentation extends Application {

    static class Edge {
        String destinationCity;
        int distance;

        public Edge(String destinationCity, int distance) {
            this.destinationCity = destinationCity;
            this.distance = distance;
        }
    }

    static class Graph {
        private Map<String, List<Edge>> adjacencyList;

        public Graph() {
            adjacencyList = new HashMap<>();
        }

        public void addEdge(String sourceCity, String destinationCity, int distance) {
            adjacencyList.putIfAbsent(sourceCity, new ArrayList<>());
            adjacencyList.putIfAbsent(destinationCity, new ArrayList<>());
            adjacencyList.get(sourceCity).add(new Edge(destinationCity, distance));
            adjacencyList.get(destinationCity).add(new Edge(sourceCity, distance));
        }

        public List<String> bfs(String start) {
            List<String> visited = new ArrayList<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> visitedSet = new HashSet<>();

            queue.add(start);
            visitedSet.add(start);

            while (!queue.isEmpty()) {
                String current = queue.poll();
                visited.add(current);

                for (Edge edge : adjacencyList.get(current)) {
                    if (!visitedSet.contains(edge.destinationCity)) {
                        queue.add(edge.destinationCity);
                        visitedSet.add(edge.destinationCity);
                    }
                }
            }

            return visited;
        }

        public List<String> dfs(String start) {
            List<String> visited = new ArrayList<>();
            Stack<String> stack = new Stack<>();
            Set<String> visitedSet = new HashSet<>();

            stack.push(start);
            visitedSet.add(start);

            while (!stack.isEmpty()) {
                String current = stack.pop();
                if (!visited.contains(current)) {
                    visited.add(current);
                }

                for (Edge edge : adjacencyList.get(current)) {
                    if (!visitedSet.contains(edge.destinationCity)) {
                        stack.push(edge.destinationCity);
                        visitedSet.add(edge.destinationCity);
                    }
                }
            }

            return visited;
        }

        public int calculateDistance(List<String> route) {
            int totalDistance = 0;

            for (int i = 0; i < route.size() - 1; i++) {
                String city = route.get(i);
                String nextCity = route.get(i + 1);

                for (Edge edge : adjacencyList.get(city)) {
                    if (edge.destinationCity.equals(nextCity)) {
                        totalDistance += edge.distance;
                        break;
                    }
                }
            }

            return totalDistance;
        }
    }

    private Graph graph;
    private Map<String, double[]> cityPositions;
    private Group root;
    private Map<String, Circle> cityCircles;
    private Text resultText;

    @Override
    public void start(Stage primaryStage) {
        graph = new Graph();

        // Adding cities and distances (example data, replace with actual distances)
        graph.addEdge("San Antonio", "Los Angeles", 1940);
        graph.addEdge("San Antonio", "Houston", 320);
        graph.addEdge("San Antonio", "Phoenix", 1500);
        graph.addEdge("Los Angeles", "Golden State", 615);
        graph.addEdge("Golden State", "Denver", 1970);
        graph.addEdge("Denver", "Oklahoma City", 1110);
        graph.addEdge("Oklahoma City", "Houston", 790);
        graph.addEdge("Phoenix", "Los Angeles", 600);
        graph.addEdge("Phoenix", "Orlando", 3445);
        graph.addEdge("Orlando", "Boston", 2035);
        graph.addEdge("Orlando", "Miami", 380);
        graph.addEdge("Boston", "Miami", 2425);

        root = new Group();

        // City positions (for simplicity, these are hard-coded)
        cityPositions = new HashMap<>();
        cityPositions.put("Golden State", new double[]{100, 100});
        cityPositions.put("Los Angeles", new double[]{50, 250});
        cityPositions.put("Denver", new double[]{400, 100});
        cityPositions.put("Oklahoma City", new double[]{300, 250});
        cityPositions.put("Houston", new double[]{480, 280});
        cityPositions.put("Phoenix", new double[]{100, 350});
        cityPositions.put("San Antonio", new double[]{250, 450});
        cityPositions.put("Orlando", new double[]{550, 600});
        cityPositions.put("Boston", new double[]{600, 50});
        cityPositions.put("Miami", new double[]{600, 500});

        // Draw edges
        for (Map.Entry<String, List<Edge>> entry : graph.adjacencyList.entrySet()) {
            String sourceCity = entry.getKey();
            for (Edge edge : entry.getValue()) {
                String destinationCity = edge.destinationCity;
                if (cityPositions.containsKey(sourceCity) && cityPositions.containsKey(destinationCity)) {
                    double[] sourcePos = cityPositions.get(sourceCity);
                    double[] destPos = cityPositions.get(destinationCity);

                    Line line = new Line(sourcePos[0], sourcePos[1], destPos[0], destPos[1]);
                    root.getChildren().add(line);

                    // Midpoint for distance text
                    double midX = (sourcePos[0] + destPos[0]) / 2;
                    double midY = (sourcePos[1] + destPos[1]) / 2;
                    Text distanceText = new Text(midX, midY, edge.distance + " km");
                    distanceText.setFont(new Font(12));
                    distanceText.setFill(Color.BLACK);
                    distanceText.setStroke(Color.WHITE); // Outline the text
                    distanceText.setStrokeWidth(0.5);
                    root.getChildren().add(distanceText);
                }
            }
        }

        // Draw cities
        cityCircles = new HashMap<>();
        for (Map.Entry<String, double[]> entry : cityPositions.entrySet()) {
            String city = entry.getKey();
            double[] pos = entry.getValue();

            Circle circle = new Circle(pos[0], pos[1], 20, Color.LIGHTBLUE);
            Text cityText = new Text(pos[0] - 30, pos[1] - 25, city);
            cityText.setFont(new Font(14));
            cityText.setFill(Color.BLACK);
            cityText.setStroke(Color.WHITE); // Outline the text
            cityText.setStrokeWidth(0.5);
            root.getChildren().add(circle);
            root.getChildren().add(cityText);
            cityCircles.put(city, circle);
        }

        // Buttons for BFS and DFS
        Button bfsButton = new Button("BFS");
        bfsButton.setLayoutX(650);
        bfsButton.setLayoutY(50);
        bfsButton.setOnAction(e -> animateBFS());

        Button dfsButton = new Button("DFS");
        dfsButton.setLayoutX(650);
        dfsButton.setLayoutY(100);
        dfsButton.setOnAction(e -> animateDFS());

        root.getChildren().addAll(bfsButton, dfsButton);

        // Text to display results
        resultText = new Text(650, 200, "");
        resultText.setFont(new Font(14));
        root.getChildren().add(resultText);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("NBA Travel Planner");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Perform BFS and DFS from San Antonio and calculate distances
        List<String> bfsRoute = graph.bfs("San Antonio");
        List<String> dfsRoute = graph.dfs("San Antonio");

        int bfsDistance = graph.calculateDistance(bfsRoute);
        int dfsDistance = graph.calculateDistance(dfsRoute);

        // Determine the better route
        String bestRoute = (bfsDistance < dfsDistance) ? "BFS" : "DFS";
        int bestDistance = Math.min(bfsDistance, dfsDistance);

        // Update result text
        resultText.setText("BFS Total Distance: " + bfsDistance + " km\nDFS Total Distance: " + dfsDistance + " km\nBest Route: " + bestRoute + "\nBest Total Distance: " + bestDistance + " km");
    }

    private void animateBFS() {
        List<String> bfsRoute = graph.bfs("San Antonio");
        animateRoute(bfsRoute);
    }

    private void animateDFS() {
        List<String> dfsRoute = graph.dfs("San Antonio");
        animateRoute(dfsRoute);
    }

    private void animateRoute(List<String> route) {
        new Thread(() -> {
            for (int i = 0; i < route.size(); i++) {
                String city = route.get(i);
                Circle circle = cityCircles.get(city);

                javafx.application.Platform.runLater(() -> circle.setFill(Color.RED));

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> javafx.application.Platform.runLater(() -> circle.setFill(Color.LIGHTBLUE)));
                pause.play();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
