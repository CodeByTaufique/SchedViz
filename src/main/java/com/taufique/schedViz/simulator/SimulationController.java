package com.taufique.schedViz.simulator;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.metrics.ExportManager;
import com.taufique.schedViz.scheduler.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationController {

    @FXML
    private TextArea outputArea;

    @FXML
    private ChoiceBox<String> schedulerChoice;

    @FXML
    private TextField quantumField;

    @FXML
    private TextField coresField; // user specifies number of CPU cores

    private final List<Process> processes = new ArrayList<>();
    private final List<CPUCore> cores = new ArrayList<>();

    @FXML
    public void initialize() {
        // Scheduler options
        schedulerChoice.setItems(FXCollections.observableArrayList(
                "FCFS", "Round Robin", "Priority", "SJF"
        ));
        schedulerChoice.setValue("FCFS");
    }

    @FXML
    private void handleAddProcess() {
        // Example: in real GUI, take user input fields for process data
        int pid = processes.size() + 1;
        int arrivalTime = pid * 2;
        int burstTime = 3 + pid;
        int priority = pid; // Example priority
        processes.add(new Process(pid, arrivalTime, burstTime, priority));
        outputArea.appendText("Added Process P" + pid + " Arrival: " + arrivalTime +
                " Burst: " + burstTime + " Priority: " + priority + "\n");
    }

    @FXML
    private void handleRunSimulation() {
        if (processes.isEmpty()) {
            outputArea.appendText("No processes added!\n");
            return;
        }

        // Get number of CPU cores from user
        int numCores = 1; // default
        String coresText = coresField != null ? coresField.getText() : null;
        if (coresText != null) coresText = coresText.trim();
        try {
            if (coresText != null && !coresText.isEmpty()) {
                numCores = Integer.parseInt(coresText);
                if (numCores < 1) numCores = 1;
            }
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid core count, using default 1\n");
            numCores = 1;
        }

        // Initialize cores dynamically (replace previous cores)
        cores.clear();
        for (int i = 0; i < numCores; i++) {
            cores.add(new CPUCore(i));
        }

        // Choose scheduler (null-safe)
        String selected = schedulerChoice != null && schedulerChoice.getValue() != null
                ? schedulerChoice.getValue()
                : "FCFS";

        // Pass defensive copies to scheduler so it can mutate without affecting UI list directly
        List<Process> processesCopy = processes.stream()
                .map(p -> new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()))
                .collect(Collectors.toList());

        // Create cores copy by index (do not rely on CPUCore#getId())
        List<CPUCore> coresCopy = new ArrayList<>();
        for (int i = 0; i < numCores; i++) {
            coresCopy.add(new CPUCore(i));
        }

        // Use Object to hold the concrete scheduler instance to avoid compile-time type mismatch
        final Object schedulerInstance;
        switch (selected) {
            case "FCFS":
                schedulerInstance = new FCFS(processesCopy, coresCopy);
                break;

            case "Round Robin": {
                int quantum = 4; // default
                String qText = quantumField != null ? quantumField.getText() : null;
                if (qText != null) qText = qText.trim();
                try {
                    if (qText != null && !qText.isEmpty()) {
                        quantum = Integer.parseInt(qText);
                        if (quantum < 1) {
                            outputArea.appendText("Quantum must be >= 1. Using default 4\n");
                            quantum = 4;
                        }
                    }
                } catch (NumberFormatException e) {
                    outputArea.appendText("Invalid quantum, using default 4\n");
                    quantum = 4;
                }
                schedulerInstance = new RoundRobin(processesCopy, coresCopy, quantum);
                break;
            }

            case "Priority":
                schedulerInstance = new Priority(processesCopy, coresCopy);
                break;

            case "SJF":
                schedulerInstance = new SJF(processesCopy, coresCopy, false); // non-preemptive
                break;

            default:
                outputArea.appendText("Unknown scheduler!\n");
                return;
        }

        // Run simulation off the JavaFX Application Thread
        Task<List<Process>> simulationTask = new Task<List<Process>>() {
            @Override
            protected List<Process> call() {
                // Run the concrete scheduler's run() method by checking its type.
                // After run(), schedulers are expected to mutate the provided processesCopy list in-place.
                try {
                    if (schedulerInstance instanceof FCFS) {
                        ((FCFS) schedulerInstance).run();
                    } else if (schedulerInstance instanceof RoundRobin) {
                        ((RoundRobin) schedulerInstance).run();
                    } else if (schedulerInstance instanceof Priority) {
                        ((Priority) schedulerInstance).run();
                    } else if (schedulerInstance instanceof SJF) {
                        ((SJF) schedulerInstance).run();
                    } else {
                        throw new IllegalStateException("Unsupported scheduler instance");
                    }
                    // Return the processesCopy which should now contain results
                    return processesCopy;
                } catch (Exception ex) {
                    Platform.runLater(() -> outputArea.appendText("Simulation error: " + ex.getMessage() + "\n"));
                    throw ex;
                }
            }

            @Override
            protected void succeeded() {
                List<Process> results = getValue(); // this is processesCopy
                Platform.runLater(() -> {
                    outputArea.appendText("===== Simulation Results (" + selected + ") =====\n");
                    for (Process p : results) {
                        outputArea.appendText("PID: " + p.getPid() +
                                " Waiting: " + p.getWaitingTime() +
                                " Turnaround: " + p.getTurnaroundTime() +
                                " State: " + p.getState() + "\n");
                    }

                    // Export metrics using the scheduler results (processesCopy)
                    try {
                        ExportManager.exportToCSV(results, selected.toLowerCase().replace(" ", "_") + "_metrics.csv");
                        outputArea.appendText("Metrics exported for " + selected + "\n");
                    } catch (Exception e) {
                        outputArea.appendText("Failed to export metrics: " + e.getMessage() + "\n");
                    }
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> outputArea.appendText("Simulation failed.\n"));
            }
        };

        Thread t = new Thread(simulationTask, "Simulation-Thread");
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void handleReset() {
        processes.clear();
        // Ensure we release any process references on cores (if CPUCore has such method)
        cores.forEach(core -> {
            try {
                core.releaseProcess();
            } catch (Exception ignored) {
            }
        });
        cores.clear();
        if (outputArea != null) {
            outputArea.clear();
            outputArea.appendText("Simulation reset!\n");
        }
    }
}