package com.taufique.schedViz.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GanttChartView extends JPanel {

    private List<String> executionOrder;
    private List<Integer> executionTime;

    public GanttChartView(List<String> executionOrder, List<Integer> executionTime) {
        this.executionOrder = executionOrder;
        this.executionTime = executionTime;
        setPreferredSize(new Dimension(800, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (executionOrder == null || executionTime == null) return;

        int x = 50;
        int y = 80;

        for (int i = 0; i < executionOrder.size(); i++) {
            int width = executionTime.get(i) * 40;

            g.drawRect(x, y, width, 40);
            g.drawString(executionOrder.get(i), x + width / 2 - 10, y + 25);
            g.drawString(String.valueOf(i), x, y + 60);

            x += width;
        }
    }
}
