package com.taufique.schedViz.scheduler;

import com.taufique.schedViz.model.CPUCore;
import com.taufique.schedViz.model.Process;
import com.taufique.schedViz.metrics.MetricsCalculator;

import java.util.List;

/**
 * Abstract base class for all schedulers.
 * Provides common fields and helper methods.
 */
public abstract class SchedulerInterface {

    protected List<Process> processes;
    protected List<CPUCore> cores;

    public SchedulerInterface(List<Process> processes, List<CPUCore> cores) {
        this.processes = processes;
        this.cores = cores;
    }

    /**
     * Run the scheduler simulation.
     * Must be implemented by each concrete scheduler.
     */
    public abstract void run();

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public void setCores(List<CPUCore> cores) {
        this.cores = cores;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    /**
     * Check if any core is still running a process.
     */
    protected boolean anyCoreBusy() {
        for (CPUCore core : cores) {
            if (!core.isIdle()) return true;
        }
        return false;
    }

    /**
     * Helper to calculate metrics after scheduling.
     * Can be called at the end of run().
     */
    protected void calculateMetrics() {
        MetricsCalculator.calculateMetrics(processes);
    }
}
