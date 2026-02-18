package com.taufique.schedViz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.taufique.schedViz.ui.ControlPanelView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ControlPanelView root = new ControlPanelView();
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("SchedViz Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
