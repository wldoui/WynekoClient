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

}
