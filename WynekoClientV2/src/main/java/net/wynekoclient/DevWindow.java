package net.wynekoclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public final class DevWindow {

    private final Minecraft mc = Minecraft.getMinecraft();

    public void renderOverlay(float partialTicks) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(mc);

        int width = 120;
        int height = 56;
        int x = sr.getScaledWidth() / 2 - width / 2;
        int y = sr.getScaledHeight() / 2 - height / 2;

        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        mc.fontRendererObj.drawStringWithShadow("Wyneko Overlay", x + 6, y + 6, 0xFFFFFF);

        String pos = "XYZ: " + (int) mc.thePlayer.posX + " / " + (int) mc.thePlayer.posY + " / " + (int) mc.thePlayer.posZ;
        mc.fontRendererObj.drawStringWithShadow(pos, x + 6, y + 18, 0xA0A0A0);

        String fps = mc.debug.split(",")[0];
        mc.fontRendererObj.drawStringWithShadow(fps, x + 6, y + 30, 0xA0FFA0);
    }
}
