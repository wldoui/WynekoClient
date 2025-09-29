package net.wynekoclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Keystroks {
    private static final Minecraft mc = Minecraft.getMinecraft();

    // Клики для подсчёта CPS
    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();

    // Цвета
    private static final int bg = 0xAA222222;      // фон кнопки
    private static final int pressed = 0xFF33C1FF; // цвет при нажатии
    private static final int text = 0xFFFFFFFF;    // белый текст

    // Размеры
    private static final int size = 20;
    private static final int spacing = 2;

    // Основной рендер HUD
    public static void render() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        ScaledResolution sr = new ScaledResolution(mc);

        int startX = 20;
        int startY = sr.getScaledHeight() - 80;

        // ===== W =====
        drawKey(startX + size + spacing, startY, "W", Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));

        // ===== A S D =====
        drawKey(startX, startY + size + spacing, "A", Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
        drawKey(startX + size + spacing, startY + size + spacing, "S", Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
        drawKey(startX + (size + spacing) * 2, startY + size + spacing, "D", Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));

        // ===== ЛКМ =====
        boolean lmb = Mouse.isButtonDown(0);
        if (lmb) registerClick(leftClicks);
        drawKeyWithCPS(startX, startY + (size + spacing) * 2, "LMB", lmb, getCPS(leftClicks));

        // ===== ПКМ =====
        boolean rmb = Mouse.isButtonDown(1);
        if (rmb) registerClick(rightClicks);
        drawKeyWithCPS(startX + size + spacing, startY + (size + spacing) * 2, "RMB", rmb, getCPS(rightClicks));

        // ===== SPACE =====
        drawKey(startX, startY + (size + spacing) * 3, "SPACE", Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()), size * 3 + spacing * 2, size);
    }

    // Рисуем квадрат + текст
    private static void drawKey(int x, int y, String label, boolean down) {
        drawKey(x, y, label, down, size, size);
    }

    private static void drawKey(int x, int y, String label, boolean down, int w, int h) {
        drawRect(x, y, x + w, y + h, down ? pressed : bg);
        int textWidth = mc.fontRendererObj.getStringWidth(label);
        mc.fontRendererObj.drawStringWithShadow(label,
                x + (w - textWidth) / 2,
                y + (h - 8) / 2,
                text);
    }

    // Рисуем кнопку + CPS под ней
    private static void drawKeyWithCPS(int x, int y, String label, boolean down, int cps) {
        drawKey(x, y, label, down, size, size);
        String cpsText = cps + " CPS";
        int textWidth = mc.fontRendererObj.getStringWidth(cpsText);
        mc.fontRendererObj.drawStringWithShadow(cpsText, x + (size - textWidth) / 2, y + size + 2, 0xFFFFFFFF);
    }

    // Подсчёт CPS
    private static void registerClick(List<Long> list) {
        list.add(System.currentTimeMillis());
    }

    private static int getCPS(List<Long> list) {
        long time = System.currentTimeMillis();
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) {
            if (time - it.next() > 1000L) {
                it.remove();
            }
        }
        return list.size();
    }

    // Рисуем прямоугольник через Tessellator (старый MCP)
    private static void drawRect(int left, int top, int right, int bottom, int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(left,  bottom, 0.0D).color(r, g, b, a).endVertex();
        wr.pos(right, bottom, 0.0D).color(r, g, b, a).endVertex();
        wr.pos(right, top,    0.0D).color(r, g, b, a).endVertex();
        wr.pos(left,  top,    0.0D).color(r, g, b, a).endVertex();
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
