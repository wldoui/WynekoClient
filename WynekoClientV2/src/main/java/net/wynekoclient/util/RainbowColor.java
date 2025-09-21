package net.wynekoclient.util;

import java.awt.*;

public class RainbowColor {
    public static int[] getRainbowColor(int speed, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % speed) / (float) speed;
        int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new int[] { red, green, blue };
    }
}
