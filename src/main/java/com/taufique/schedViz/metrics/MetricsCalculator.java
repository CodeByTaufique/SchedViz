package com.taufique.schedViz.metrics;

import com.taufique.schedViz.model.Process;

import java.util.List;

public class MetricsCalculator {

    // Calculate waiting time and turnaround time for all processes
    // Assumes processes have been scheduled and completion times are known
    public static void calculateMetrics(List<Process> processes) {

        for (Process p : processes) {
            int turnaroundTime = p.getBurstTime() + p.getWaitingTime(); // simple formula
            p.setTurnaroundTime(turnaroundTime);
        }
    }

    // Example: calculate waiting times for FCFS
    public static void calculateFCFSWaitingTime(List<Process> processes) {
        int currentTime = 0;

        for (Process p : processes) {
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }
            int waitingTime = currentTime - p.getArrivalTime();
            p.setWaitingTime(waitingTime);

            currentTime += p.getBurstTime();
        }

        // Once waiting times are set, we can compute turnaround times
        calculateMetrics(processes);
    }
}
