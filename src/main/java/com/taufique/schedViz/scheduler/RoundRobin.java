package com.taufique.schedViz.scheduler;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.metrics.MetricsCalculator;
import com.taufique.schedViz.model.ProcessState;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin extends SchedulerInterface {

    private final int timeQuantum;

    public RoundRobin(List<Process> processes, List<CPUCore> cores, int timeQuantum) {
        super(processes, cores);
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void run() {

        Queue<Process> readyQueue = new LinkedList<>();

        // Sort processes by arrival time
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int currentTime = 0;
        int processIndex = 0;
        int[] coreQuantum = new int[cores.size()];

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
            for (int i = 0; i < cores.size(); i++) {
                CPUCore core = cores.get(i);
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    Process p = readyQueue.poll();
                    core.assignProcess(p);
                    coreQuantum[i] = timeQuantum; // reset quantum
                }
            }

            // Execute one unit of time on each core
            for (int i = 0; i < cores.size(); i++) {
                CPUCore core = cores.get(i);
                Process running = core.getRunningProcess();

                if (running != null) {
                    running.executeOneUnit();
                    coreQuantum[i]--;

                    if (running.isCompleted()) {
                        running.setState(ProcessState.TERMINATED);
                        core.releaseProcess();
                        coreQuantum[i] = 0;
                    } else if (coreQuantum[i] <= 0) {
                        running.setState(ProcessState.READY);
                        readyQueue.add(running);
                        core.releaseProcess();
                    }
                }
            }

            currentTime++;
        }

        // Calculate metrics at the end
        calculateMetrics();
    }
}
