package org.nba_data_structure.application;

import javafx.animation.PauseTransition;
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

import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import javafx.util.Duration;
import org.nba_data_structure.data_structures.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.nba_data_structure.bean.GraphBean;
import org.nba_data_structure.data_structures.Stack;
import org.nba_data_structure.dao.GraphDao;
import java.sql.SQLException;

public class GraphRepresentation extends Application {

    ArrayList<GraphBean> graphNode = new ArrayList<>();
    private static class Edge {
        String destinationCity;
        int distance;

        public Edge(String destinationCity, int distance) {
            this.destinationCity = destinationCity;
            this.distance = distance;
        }
    }

    private static class Graph {
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

        public Map<String, List<Edge>> getAdjacencyList() {
            return adjacencyList;
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
    public void start(Stage primaryStage) throws SQLException {
        graph = new Graph();

        GraphDao dao = new GraphDao();
        try {
            graphNode = dao.getAllNode();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (GraphBean graphBean : graphNode) {
            graph.addEdge(graphBean.getSource_city(), graphBean.getDestination_city(), graphBean.getWeight());
        }

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
        for (Map.Entry<String, List<Edge>> entry : graph.getAdjacencyList().entrySet()) {
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

        // Back to Dashboard Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setLayoutX(650);
        backButton.setLayoutY(150);
        backButton.setOnAction(e -> switchToDashboard());

        root.getChildren().addAll(bfsButton, dfsButton, backButton);

        // Text to display results
        resultText = new Text(650, 200, "");
        resultText.setFont(new Font(14));
        root.getChildren().add(resultText);

        // Perform BFS and DFS from San Antonio and calculate distances
        List<String> bfsRoute = graph.bfs("San Antonio");
        List<String> dfsRoute = graph.dfs("San Antonio");

        int bfsDistance = graph.calculateDistance(bfsRoute);
        int dfsDistance = graph.calculateDistance(dfsRoute);

        // Print routes and distances
        System.out.println("BFS Route: " + bfsRoute);
        System.out.println("BFS Total Distance: " + bfsDistance + " km");

        System.out.println("DFS Route: " + dfsRoute);
        System.out.println("DFS Total Distance: " + dfsDistance + " km");

        // Determine the better route
        String bestRoute = (bfsDistance < dfsDistance) ? "BFS" : "DFS";
        int bestDistance = Math.min(bfsDistance, dfsDistance);

        System.out.println("Best Route: " + bestRoute);
        System.out.println("Best Total Distance: " + bestDistance + " km");

        // Update result text
        resultText.setText("BFS Total Distance: " + bfsDistance + " km\nDFS Total Distance: " + dfsDistance + " km\nBest Route: " + bestRoute + "\nBest Total Distance: " + bestDistance + " km");

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
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


    private void switchToDashboard() {
        try {
            // Get the current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // Load the dashboard FXML file
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));

            // Set the new scene
            Scene scene = new Scene(dashboardRoot, 800, 500);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
