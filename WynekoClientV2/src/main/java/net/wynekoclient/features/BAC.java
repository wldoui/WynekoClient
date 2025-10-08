package net.wynekoclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BAC {
    private static Minecraft mc = Minecraft.getMinecraft();
    List<Entity> entityList = new ArrayList<>(mc.theWorld.playerEntities);
    private void BypassAC(){
        for (Entity e : entityList) {

        }
    }
}
