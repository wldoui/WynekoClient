package net.wynekoclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;


public class CustomFontRendererObj extends GuiScreen {
    public static FontRenderer fontRenderer;
    public static void init() {
        Minecraft mc = Minecraft.getMinecraft();
        fontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii2.png"), mc.getTextureManager(), false);
    }
}
