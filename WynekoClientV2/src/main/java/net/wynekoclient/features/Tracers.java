package net.wynekoclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.wynekoclient.util.RainbowColor;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static net.wynekoclient.DevWindow.*;

public class Tracers {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderTracers(float partialTicks) {
        EntityPlayerSP player = mc.thePlayer;
        if (player == null || mc.theWorld == null) return;

        // Скопировать список, чтобы избежать ConcurrentModificationException
        List<Entity> entityList = new ArrayList<>(mc.theWorld.playerEntities);

        // Интерполированная позиция игрока (в мировых координатах) — именно ОТ ТЕБЯ (ThePlayer), не камеры
        double playerX = interp(player.lastTickPosX, player.posX, partialTicks);
        double playerY = interp(player.lastTickPosY, player.posY, partialTicks) + player.getEyeHeight();
        double playerZ = interp(player.lastTickPosZ, player.posZ, partialTicks);

        // GL подготовка
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        // Рендерить поверх всего? отключи тест глубины. Если хочешь, чтобы линии учитывали глубину — не отключай.
        GlStateManager.disableDepth();

        // Толщина линий — ставим один раз
        GL11.glLineWidth(lineWidth);

        // Пороги дистанций (можешь настраивать)
        final double d0 = 3.0;
        final double d1 = 6.0;
        final double d2 = 12.0;
        final double d0sq = d0 * d0;
        final double d1sq = d1 * d1;
        final double d2sq = d2 * d2;

        // Готовим, чтобы не делать много аллокаций в цикле
        for (Entity e : entityList) {
            // учитывать игроков на клиенте (EntityOtherPlayerMP, AbstractClientPlayer, EntityPlayerSP если нужно)
            if (!(e instanceof EntityPlayer)) continue;
            EntityPlayer target = (EntityPlayer) e;

            if (target == player) continue; // не рисуем до себя
            if (target.isDead) continue;
            // опционально: if (target.isInvisible()) continue;

            // Интерполированная позиция цели — используем eye position чтобы линия шла к голове
            double targetX = interp(target.lastTickPosX, target.posX, partialTicks);
            double targetY = interp(target.lastTickPosY, target.posY, partialTicks) + target.getEyeHeight();
            double targetZ = interp(target.lastTickPosZ, target.posZ, partialTicks);

            // Расстояние (в мире) от игрока до цели — РЕАЛЬНОЕ, не связанное с камерой
            double dx = targetX - playerX;
            double dy = targetY - playerY;
            double dz = targetZ - playerZ;
            double distSq = dx * dx + dy * dy + dz * dz;

            // Выбор цвета: строго взаимно-исключающие режимы
            float r, g, b;
            if (rainbowTracers) {
                int[] rgb = RainbowColor.getRainbowColor(3000, 1.0f, 1.0f);
                r = rgb[0] / 255f;
                g = rgb[1] / 255f;
                b = rgb[2] / 255f;
            } else if (distanceBasedColor) {
                // Используем distSq для быстрых веток, sqrt только один раз на нужную ветку
                double distance = Math.sqrt(distSq);
                if (distance <= d0) {
                    r = g = b = 0.0f; // черный
                } else if (distance <= d1) {
                    float factor = (float) ((distance - d0) / (d1 - d0)); // 0..1
                    float v = factor * 0.5f; // 0..0.5
                    r = g = b = v;
                } else if (distance <= d2) {
                    float factor = (float) ((distance - d1) / (d2 - d1)); // 0..1
                    float v = 0.5f + factor * 0.5f; // 0.5..1.0
                    r = g = b = v;
                } else {
                    r = g = b = 1.0f; // белый
                }
            } else {
                r = g = b = 1.0f; // дефолт
            }

            // Рисуем линию между интерполированными позициями (world coords)
            drawLine3D(playerX, playerY, playerZ, targetX, targetY, targetZ, r, g, b);
        }

        // Сброс состояний OpenGL
        GL11.glLineWidth(1.0F);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    /** Линейная интерполяция для позиций */
    private static double interp(double last, double current, float partialTicks) {
        return last + (current - last) * partialTicks;
    }

    /** Рисует линию с учётом смещения камеры (viewerPos) — корректный world->camera преобраз */
    private static void drawLine3D(double sx, double sy, double sz,
                                   double ex, double ey, double ez,
                                   float r, float g, float b) {
        double camX = mc.getRenderManager().viewerPosX;
        double camY = mc.getRenderManager().viewerPosY;
        double camZ = mc.getRenderManager().viewerPosZ;

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(sx - camX, sy - camY, sz - camZ).color(r, g, b, 1.0f).endVertex();
        wr.pos(ex - camX, ey - camY, ez - camZ).color(r, g, b, 1.0f).endVertex();
        tess.draw();
    }
}
