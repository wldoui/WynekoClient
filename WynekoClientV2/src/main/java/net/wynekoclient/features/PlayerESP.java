package net.wynekoclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class PlayerESP {

    private static final Logger logger = LogManager.getLogger();

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean showHitbox = true;
    public static boolean showName = true;

    // Этот метод вызывай из RenderWorldLastEvent или из твоего хука рендера
    public static void onRenderWorldLast(float partialTicks) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (showName) {
                    renderPlayerName(player, partialTicks);
                }
                if (showHitbox) {
                    renderHitbox(player, partialTicks);
                }
            }
        }
    }

    private static void renderPlayerName(EntityPlayer player, float partialTicks) {

        PlayerESP.logger.error("Loaded renderPlayerName condition");

        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY + player.height + 0.5;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        String displayName = player.getDisplayName().getFormattedText();

        double distance = player.getDistanceToEntity(mc.thePlayer);
        float maxDistance = 50;
        float scale = 0.02F + (float) Math.min(Math.min(distance, maxDistance) / 300, 0.8);

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        GlStateManager.enableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        int color = 0xFFFFFF;
        mc.fontRendererObj.drawString(displayName, -mc.fontRendererObj.getStringWidth(displayName) / 2, 0, color, false);

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    private static void renderHitbox(EntityPlayer player, float partialTicks) {
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        // фиксированный зелёный цвет ESP
        GlStateManager.color(0.0F, 1.0F, 0.0F, 1.0F);

        GL11.glLineWidth(2.0F);

        // нижняя грань
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(-player.width / 2, 0, -player.width / 2);
        GL11.glVertex3d(-player.width / 2, 0, player.width / 2);
        GL11.glVertex3d(player.width / 2, 0, player.width / 2);
        GL11.glVertex3d(player.width / 2, 0, -player.width / 2);
        GL11.glEnd();

        // верхняя грань
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(-player.width / 2, player.height, -player.width / 2);
        GL11.glVertex3d(-player.width / 2, player.height, player.width / 2);
        GL11.glVertex3d(player.width / 2, player.height, player.width / 2);
        GL11.glVertex3d(player.width / 2, player.height, -player.width / 2);
        GL11.glEnd();

        // вертикальные линии
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(-player.width / 2, 0, -player.width / 2);
        GL11.glVertex3d(-player.width / 2, player.height, -player.width / 2);

        GL11.glVertex3d(-player.width / 2, 0, player.width / 2);
        GL11.glVertex3d(-player.width / 2, player.height, player.width / 2);

        GL11.glVertex3d(player.width / 2, 0, player.width / 2);
        GL11.glVertex3d(player.width / 2, player.height, player.width / 2);

        GL11.glVertex3d(player.width / 2, 0, -player.width / 2);
        GL11.glVertex3d(player.width / 2, player.height, -player.width / 2);
        GL11.glEnd();

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
}
