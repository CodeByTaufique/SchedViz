package com.taufique.schedViz.scheduler;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.model.ProcessState;
import com.taufique.schedViz.metrics.MetricsCalculator;

import java.util.List;
import java.util.PriorityQueue;

public class Priority extends SchedulerInterface {

    public Priority(List<Process> processes, List<CPUCore> cores) {
        super(processes, cores);
    }

    @Override
    public void run() {

        // Priority queue: lower priority value = higher priority
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                (p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority())
        );

        // Sort by arrival time initially
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int currentTime = 0;
        int processIndex = 0;

        while (processIndex < processes.size() || !readyQueue.isEmpty() || anyCoreBusy()) {

            // Add newly arrived processes to ready queue
            while (processIndex < processes.size() &&
                    processes.get(processIndex).getArrivalTime() <= currentTime) {
                Process p = processes.get(processIndex);
                p.setState(ProcessState.READY);
                readyQueue.add(p);
                processIndex++;
            }

            // Assign idle cores
            for (CPUCore core : cores) {
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    Process p = readyQueue.poll();
                    core.assignProcess(p);
                }
            }

            // Execute one unit of time on each core
            for (CPUCore core : cores) {
                Process running = core.getRunningProcess();
                if (running != null) {
                    running.executeOneUnit();
                    if (running.isCompleted()) {
                        running.setState(ProcessState.TERMINATED);
                        core.releaseProcess();
                    }
                }
            }

            currentTime++; // advance time
        }

        // Calculate metrics at the end
        calculateMetrics();
    }
}
