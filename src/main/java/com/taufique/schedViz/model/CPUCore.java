package com.taufique.schedViz.model;

public class CPUCore {

    private int coreId;
    private Process runningProcess;
    private boolean idle;

    public CPUCore(int coreId) {
        this.coreId = coreId;
        this.idle = true;
    }

    public int getCoreId() {
        return coreId;
    }

    public boolean isIdle() {
        return idle;
    }

    public Process getRunningProcess() {
        return runningProcess;
    }

    public void assignProcess(Process process) {
        this.runningProcess = process;
        this.runningProcess.setState(ProcessState.RUNNING);
        this.idle = false;
    }

    public void releaseProcess() {
        if (runningProcess != null) {
            runningProcess.setState(ProcessState.READY);
        }
        this.runningProcess = null;
        this.idle = true;
    }
}
