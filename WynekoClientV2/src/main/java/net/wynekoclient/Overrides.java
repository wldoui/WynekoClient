package net.wynekoclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Overrides {

    private final Minecraft mc = Minecraft.getMinecraft();

    public static boolean noHurtCam = false;
    public static boolean fullBright = false;

    public void hurtCameraEffectCustom(float partialTicks) {
        if (noHurtCam) return; // отключаем тряску

        if (mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) mc.getRenderViewEntity();
            float f = (float) entity.hurtTime - partialTicks;

            if (f < 0.0F) return;

            f = f / (float) entity.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float) Math.PI);
            float yaw = entity.attackedAtYaw;

            GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        }
    }
}
