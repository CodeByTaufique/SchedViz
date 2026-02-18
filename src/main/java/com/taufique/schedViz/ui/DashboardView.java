package com.taufique.schedViz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DashboardView extends VBox {

    private final Label schedulerLabel = new Label("-");
    private final Label coresLabel = new Label("-");
    private final Label durationLabel = new Label("-");
    private final Label avgWaitingLabel = new Label("-");
    private final Label avgTurnaroundLabel = new Label("-");

    public DashboardView() {

        setSpacing(15);
        setPadding(new Insets(15));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Simulation Dashboard");
        title.setFont(new Font(18));

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Scheduler:"), 0, 0);
        grid.add(schedulerLabel, 1, 0);

        grid.add(new Label("CPU Cores:"), 0, 1);
        grid.add(coresLabel, 1, 1);

        grid.add(new Label("Duration (ms):"), 0, 2);
        grid.add(durationLabel, 1, 2);

        grid.add(new Label("Avg Waiting Time:"), 0, 3);
        grid.add(avgWaitingLabel, 1, 3);

        grid.add(new Label("Avg Turnaround Time:"), 0, 4);
        grid.add(avgTurnaroundLabel, 1, 4);

        getChildren().addAll(title, grid);
    }

    public void updateDashboard(String scheduler,
                                int cores,
                                long duration,
                                double avgWaiting,
                                double avgTurnaround) {

        schedulerLabel.setText(scheduler);
        coresLabel.setText(String.valueOf(cores));
        durationLabel.setText(String.valueOf(duration));
        avgWaitingLabel.setText(String.format("%.2f", avgWaiting));
        avgTurnaroundLabel.setText(String.format("%.2f", avgTurnaround));
    }

    public void reset() {
        schedulerLabel.setText("-");
        coresLabel.setText("-");
        durationLabel.setText("-");
        avgWaitingLabel.setText("-");
        avgTurnaroundLabel.setText("-");
    }
}
