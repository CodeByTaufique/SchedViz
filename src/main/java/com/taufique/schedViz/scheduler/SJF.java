package com.taufique.schedViz.scheduler;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.model.ProcessState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJF extends SchedulerInterface {

    private final boolean preemptive;

    public SJF(List<Process> processes, List<CPUCore> cores, boolean preemptive) {
        super(processes, cores);
        this.preemptive = preemptive;
    }

    @Override
    public void run() {
        List<Process> readyQueue = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int processIndex = 0;

        while (processIndex < processes.size() || !readyQueue.isEmpty() || anyCoreBusy()) {

            // Add newly arrived processes
            while (processIndex < processes.size() &&
                    processes.get(processIndex).getArrivalTime() <= currentTime) {
                Process p = processes.get(processIndex);
                p.setState(ProcessState.READY);
                readyQueue.add(p);
                processIndex++;
            }

            // Sort ready queue by remaining burst time
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            // Assign idle cores
            for (CPUCore core : cores) {
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    Process p = readyQueue.remove(0);
                    core.assignProcess(p);
                }
            }

            // Preemptive SJF logic
            if (preemptive) {
                for (CPUCore core : cores) {
                    Process running = core.getRunningProcess();
                    if (running != null && !readyQueue.isEmpty()) {
                        Process shortest = readyQueue.get(0);
                        if (shortest.getRemainingTime() < running.getRemainingTime()) {
                            running.setState(ProcessState.READY);
                            readyQueue.add(running);
                            core.assignProcess(readyQueue.remove(0));
                        }
                    }
                }
            }

            // Execute one unit on each core
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

            currentTime++;
        }

        // Calculate waiting and turnaround times
        calculateMetrics();
    }
}
