package com.taufique.schedViz.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputView extends JPanel {

    private JTextField processIdField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;

    private DefaultListModel<String> processListModel;
    private List<int[]> processData;

    public ProcessInputView() {
        setLayout(new BorderLayout());

        processData = new ArrayList<>();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        inputPanel.add(new JLabel("Process ID:"));
        processIdField = new JTextField();
        inputPanel.add(processIdField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalTimeField = new JTextField();
        inputPanel.add(arrivalTimeField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstTimeField = new JTextField();
        inputPanel.add(burstTimeField);

        JButton addButton = new JButton("Add Process");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        processListModel = new DefaultListModel<>();
        JList<String> processList = new JList<>(processListModel);
        add(new JScrollPane(processList), BorderLayout.CENTER);

        addButton.addActionListener(e -> addProcess());
    }

    private void addProcess() {
        try {
            String id = processIdField.getText();
            int arrival = Integer.parseInt(arrivalTimeField.getText());
            int burst = Integer.parseInt(burstTimeField.getText());

            processData.add(new int[]{arrival, burst});
            processListModel.addElement(id + " | Arrival: " + arrival + " | Burst: " + burst);

            processIdField.setText("");
            arrivalTimeField.setText("");
            burstTimeField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    public List<int[]> getProcessData() {
        return processData;
    }
}
