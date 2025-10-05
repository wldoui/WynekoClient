package net.wynekoclient.util;

import net.minecraft.client.Minecraft;

import java.io.File;

public class DataSave {
    public static File getConfigDir() {
        File dir = new File(System.getProperty("user.home"), Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "WynekoClient/options");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    public static File getBindsFile() {
        return new File(getConfigDir(), "config.txt");
    }
}
