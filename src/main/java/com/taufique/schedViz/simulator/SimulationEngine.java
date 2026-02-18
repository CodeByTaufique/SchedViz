package com.taufique.schedViz.simulator;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.scheduler.SchedulerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SimulationEngine runs a CPU scheduling simulation, calculates metrics,
 * and provides a printable summary. Everything is contained in this class.
 */
public class SimulationEngine {

    private final List<Process> originalProcesses;
    private final int numCores;
    private final int quantum;
    private final SchedulerInterface scheduler;
    private final String schedulerName;

    public SimulationEngine(List<Process> processes,
                            int numCores,
                            int quantum,
                            SchedulerInterface scheduler,
                            String schedulerName) {
        this.originalProcesses = processes;
        this.numCores = Math.max(1, numCores);
        this.quantum = quantum;
        this.scheduler = scheduler;
        this.schedulerName = schedulerName;
    }

    /**
     * Runs the simulation and returns a formatted summary string
     */
    public String runSimulation() {
        // Defensive copy of processes
        List<Process> processesCopy = originalProcesses.stream()
                .map(p -> new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()))
                .collect(Collectors.toList());

        // Initialize cores
        List<CPUCore> coresCopy = new ArrayList<>();
        for (int i = 0; i < numCores; i++) coresCopy.add(new CPUCore(i));

        // Set scheduler state
        scheduler.setProcesses(processesCopy);
        scheduler.setCores(coresCopy);

        // Run simulation
        long startTime = System.currentTimeMillis();
        scheduler.run();
        long duration = System.currentTimeMillis() - startTime;

        // Compute metrics
        double avgWaiting = processesCopy.stream().mapToInt(Process::getWaitingTime).average().orElse(0);
        double avgTurnaround = processesCopy.stream().mapToInt(Process::getTurnaroundTime).average().orElse(0);

        // Build summary string
        StringBuilder sb = new StringBuilder();
        sb.append("===== Simulation Result =====\n");
        sb.append("Scheduler: ").append(schedulerName).append("\n");
        sb.append("CPU Cores: ").append(numCores).append("\n");
        sb.append("Duration: ").append(duration).append(" ms\n");
        sb.append(String.format("Average Waiting Time: %.2f\n", avgWaiting));
        sb.append(String.format("Average Turnaround Time: %.2f\n", avgTurnaround));
        sb.append("\nProcesses:\n");
        sb.append(String.format("%-5s %-10s %-12s %-12s %-8s\n",
                "PID", "Arrival", "Burst", "Waiting", "Turnaround"));
        for (Process p : processesCopy) {
            sb.append(String.format("%-5d %-10d %-12d %-12d %-8d\n",
                    p.getPid(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime()
            ));
        }

        return sb.toString();
    }

    public int getQuantum() {
        return quantum;
    }

    public int getNumCores() {
        return numCores;
    }

    public String getSchedulerName() {
        return schedulerName;
    }
}
