package com.taufique.schedViz.model;

public class Process {

    private int pid;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int priority;
    private ProcessState state;

    // ===== Metrics for scheduling analysis =====
    private int waitingTime;
    private int turnaroundTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.state = ProcessState.NEW;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    // ======== Getters ========
    public int getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public int getPriority() { return priority; }
    public ProcessState getState() { return state; }
    public int getWaitingTime() { return waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }

    // ======== Setters ========
    public void setState(ProcessState state) { this.state = state; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }

    // ======== Process Execution ========
    public void executeOneUnit() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    public boolean isCompleted() {
        return remainingTime == 0;
    }

    @Override
    public String toString() {
        return "P" + pid + " [" + state + "] BT: " + burstTime + " RT: " + remainingTime;
    }
}
