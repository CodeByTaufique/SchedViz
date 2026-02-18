package com.taufique.schedViz.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorManager {

    private static final Map<String, Color> processColors = new HashMap<>();
    private static final Random random = new Random();

    public static Color getColor(String processId) {
        if (!processColors.containsKey(processId)) {
            processColors.put(processId, generateRandomColor());
        }
        return processColors.get(processId);
    }

    private static Color generateRandomColor() {
        int r = 100 + random.nextInt(156);
        int g = 100 + random.nextInt(156);
        int b = 100 + random.nextInt(156);
        return new Color(r, g, b);
    }

    public static void reset() {
        processColors.clear();
    }
}
