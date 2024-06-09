package org.nba_data_structure.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parameters params = getParameters();
        String applicationToLaunch = params.getRaw().isEmpty() ? "dashboard" : params.getRaw().get(0);

        if ("graph".equalsIgnoreCase(applicationToLaunch)) {
            GraphRepresentation graphRepresentation = new GraphRepresentation();
            graphRepresentation.start(stage);
        } else {
            launchDashboard(stage);
        }

        stage.setFullScreen(true);
    }

    private void launchDashboard(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/dashboard.fxml")));
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
        stage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
