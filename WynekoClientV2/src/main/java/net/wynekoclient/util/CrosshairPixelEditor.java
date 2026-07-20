package net.wynekoclient.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CrosshairPixelEditor extends JPanel {

    private static final int SIZE = 16;
    private static final int SCALE = 15;

    private final BufferedImage image;
    private Color currentColor = Color.BLACK;

    public CrosshairPixelEditor() {
        this.image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        setPreferredSize(new Dimension(SIZE * SCALE, SIZE * SCALE));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / SCALE;
                int y = e.getY() / SCALE;

                if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return;

                int argb = image.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;

                if (alpha == 0) {
                    image.setRGB(x, y, currentColor.getRGB());
                } else {
                    image.setRGB(x, y, 0x00000000);
                }

                repaint();

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.setColor(new Color(image.getRGB(x, y), true));
                g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}