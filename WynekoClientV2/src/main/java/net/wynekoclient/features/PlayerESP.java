package net.wynekoclient.features;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.optifine.shaders.Shaders;

import static net.wynekoclient.DevWindow.rainbowPlayerEsp;
import static net.wynekoclient.util.RainbowColor.getRainbowColor;

public class PlayerESP {

    public static void RenderPlayerEsp(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!Shaders.isShadowPass)
        {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            float f = entityIn.width / 2.0F;
            AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
            AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(
                    axisalignedbb.minX - entityIn.posX + x,
                    axisalignedbb.minY - entityIn.posY + y,
                    axisalignedbb.minZ - entityIn.posZ + z,
                    axisalignedbb.maxX - entityIn.posX + x,
                    axisalignedbb.maxY - entityIn.posY + y,
                    axisalignedbb.maxZ - entityIn.posZ + z
            );

            if (rainbowPlayerEsp) {
                int[] rgb = getRainbowColor(3000, 1.0f, 1.0f);
                GlStateManager.color(rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F, 1.0F);
                RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, rgb[0], rgb[1], rgb[2], 255);
            } else {
                RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 255, 255, 255, 255);
            }


            // направление взгляда (синяя линия)
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            Vec3 vec3 = entityIn.getLook(partialTicks);
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(x, y + (double)entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
            worldrenderer.pos(x + vec3.xCoord * 1.2D, y + (double)entityIn.getEyeHeight() + vec3.yCoord * 1.2D, z + vec3.zCoord * 1.2D).color(0, 0, 125, 255).endVertex();
            tessellator.draw();

            // возвращаем OpenGL в исходное состояние
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }
}
