package com.taufique.schedViz.ui;

import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.scheduler.*;
import com.taufique.schedViz.simulator.SimulationEngine;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ControlPanelView extends VBox {

    private final List<Process> processes = new ArrayList<>();
    private final TextField pidField = new TextField();
    private final TextField arrivalField = new TextField();
    private final TextField burstField = new TextField();
    private final TextField priorityField = new TextField();
    private final TextField coresField = new TextField();
    private final TextField quantumField = new TextField();
    private final ChoiceBox<String> schedulerChoice = new ChoiceBox<>();
    private final TextArea outputArea = new TextArea();

    public ControlPanelView() {
        setSpacing(10);
        setPadding(new Insets(10));

        // Process input grid
        GridPane processGrid = new GridPane();
        processGrid.setHgap(5);
        processGrid.setVgap(5);

        processGrid.add(new Label("PID:"), 0, 0);
        processGrid.add(pidField, 1, 0);

        processGrid.add(new Label("Arrival:"), 0, 1);
        processGrid.add(arrivalField, 1, 1);

        processGrid.add(new Label("Burst:"), 0, 2);
        processGrid.add(burstField, 1, 2);

        processGrid.add(new Label("Priority:"), 0, 3);
        processGrid.add(priorityField, 1, 3);

        Button addProcessBtn = new Button("Add Process");
        addProcessBtn.setOnAction(e -> handleAddProcess());
        processGrid.add(addProcessBtn, 0, 4, 2, 1);

        // Scheduler & configuration grid
        schedulerChoice.setItems(FXCollections.observableArrayList("FCFS", "Round Robin", "Priority", "SJF"));
        schedulerChoice.setValue("FCFS");

        GridPane configGrid = new GridPane();
        configGrid.setHgap(5);
        configGrid.setVgap(5);

        configGrid.add(new Label("Scheduler:"), 0, 0);
        configGrid.add(schedulerChoice, 1, 0);

        configGrid.add(new Label("CPU Cores:"), 0, 1);
        configGrid.add(coresField, 1, 1);

        configGrid.add(new Label("Quantum (RR):"), 0, 2);
        configGrid.add(quantumField, 1, 2);

        Button runBtn = new Button("Run Simulation");
        runBtn.setOnAction(e -> handleRunSimulation());

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> handleReset());

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        getChildren().addAll(processGrid, configGrid, runBtn, resetBtn, outputArea);
    }

    private void handleAddProcess() {
        try {
            int pid = Integer.parseInt(pidField.getText().trim());
            int arrival = Integer.parseInt(arrivalField.getText().trim());
            int burst = Integer.parseInt(burstField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());

            processes.add(new Process(pid, arrival, burst, priority));
            outputArea.appendText("Added Process PID=" + pid + "\n");

            pidField.clear();
            arrivalField.clear();
            burstField.clear();
            priorityField.clear();
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid process input.\n");
        }
    }

    private void handleRunSimulation() {
        if (processes.isEmpty()) {
            outputArea.appendText("No processes to run.\n");
            return;
        }

        int cores = 1;
        int quantum = 4;

        try { cores = Integer.parseInt(coresField.getText().trim()); } catch (NumberFormatException ignored) {}
        try { quantum = Integer.parseInt(quantumField.getText().trim()); } catch (NumberFormatException ignored) {}

        String selectedScheduler = schedulerChoice.getValue();
        List<Process> processesCopy = new ArrayList<>();
        for (Process p : processes) {
            processesCopy.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }

        List<CPUCore> coresList = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            coresList.add(new CPUCore(i));
        }

        SchedulerInterface scheduler;
        switch (selectedScheduler) {
            case "FCFS":
                scheduler = new FCFS(processesCopy, coresList);
                break;
            case "Round Robin":
                scheduler = new RoundRobin(processesCopy, coresList, quantum);
                break;
            case "Priority":
                scheduler = new Priority(processesCopy, coresList);
                break;
            case "SJF":
                scheduler = new SJF(processesCopy, coresList, false);
                break;
            default:
                outputArea.appendText("Unknown scheduler!\n");
                return;
        }

        SimulationEngine engine = new SimulationEngine(processesCopy, cores, quantum, scheduler, selectedScheduler);
        String result = engine.runSimulation();
        outputArea.appendText(result + "\n");
    }

    private void handleReset() {
        processes.clear();
        outputArea.clear();
    }
}
