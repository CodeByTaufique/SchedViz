package com.taufique.schedViz.metrics;

import com.taufique.schedViz.model.Process;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportManager {

    // Export process metrics to a CSV file
    public static void exportToCSV(List<Process> processes, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {

            // CSV Header
            writer.append("PID,ArrivalTime,BurstTime,Priority,WaitingTime,TurnaroundTime,State\n");

            // Process data
            for (Process p : processes) {
                writer.append(String.valueOf(p.getPid())).append(",")
                        .append(String.valueOf(p.getArrivalTime())).append(",")
                        .append(String.valueOf(p.getBurstTime())).append(",")
                        .append(String.valueOf(p.getPriority())).append(",")
                        .append(String.valueOf(p.getWaitingTime())).append(",")
                        .append(String.valueOf(p.getTurnaroundTime())).append(",")
                        .append(p.getState().name())
                        .append("\n");
            }

            System.out.println("Exported metrics to " + fileName);

        } catch (IOException e) {
            System.err.println("Error exporting CSV: " + e.getMessage());
        }
    }
}
