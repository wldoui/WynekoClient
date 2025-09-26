package net.wynekoclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


public class WynekoFontRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<Character, DynamicTexture> charCache = new HashMap<>();
    private final Map<Character, Integer> charWidths = new HashMap<>();
    private Font font;
    private int fontHeight;

    public WynekoFontRenderer(Font font) {
        setFont(font);
    }

    public void setFont(Font font) {
        this.font = font;

        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tmp.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g.getFontMetrics();
        this.fontHeight = fm.getHeight();
        g.dispose();

        charCache.clear();
        charWidths.clear();
    }

    public void drawString(String text, float x, float y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1f); // масштабируем текст визуально
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771); // GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA

        float posX = 0; // координаты уже учитывают translate
        float posY = 0;

        for (char c : text.toCharArray()) {
            DynamicTexture tex = getCharTexture(c);
            mc.getTextureManager().bindTexture(mc.getTextureManager().getDynamicTextureLocation("char_" + c, tex));

            int width = charWidths.getOrDefault(c, 1);
            int height = fontHeight;

            drawTexturedQuad(posX, posY, width, height);
            posX += width;
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private DynamicTexture getCharTexture(char c) {
        if (charCache.containsKey(c)) return charCache.get(c);

        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTmp = tmp.createGraphics();
        gTmp.setFont(font);
        gTmp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = gTmp.getFontMetrics();
        int width = fm.charWidth(c);
        int height = fm.getHeight();
        int ascent = fm.getAscent();
        gTmp.dispose();

        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(c), 0, ascent);
        g.dispose();

        charWidths.put(c, width);
        DynamicTexture dynTex = new DynamicTexture(img);
        charCache.put(c, dynTex);
        return dynTex;
    }

    private void drawTexturedQuad(float x, float y, float width, float height) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, 0).tex(0, 1).endVertex();
        renderer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
        renderer.pos(x + width, y, 0).tex(1, 0).endVertex();
        renderer.pos(x, y, 0).tex(0, 0).endVertex();
        tessellator.draw();
    }

    public int getStringWidth(String text, float scale) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += charWidths.getOrDefault(c, 1);
        }
        return (int)(width * scale);
    }

    public int getFontHeight(float scale) {
        return (int)(fontHeight * scale);
    }
}