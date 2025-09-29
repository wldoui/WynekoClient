package net.wynekoclient.features;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.wynekoclient.util.WynekoFontRenderer;
import net.wynekoclient.util.menu.BindsMenu;

import java.awt.*;
import java.io.IOException;

public class ConnectionMenu extends GuiScreen {

    private GuiTextField nameField;

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height  / 2 + 65, 100, 20, I18n.format("gui.cancel")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 135, this.height / 2 - 10, 100, 20, "Bind"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 35, this.height / 2  - 10, 100, 20, "Friends"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 3005, this.height / 2  - 10, 100, 20, "F"));
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                            getClass().getResourceAsStream("/assets/minecraft/font/Comfortaa-Regular.ttf"))
                    .deriveFont(32f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WynekoFontRenderer fontRenderer = new WynekoFontRenderer(font);
        fontRenderer.drawString("Wyneko Client Options", this.width / 2 - 95, this.height / 10 + 10, -1, 0.5f);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        if (button.enabled) {
            if (button.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == 2) {
                this.mc.displayGuiScreen(new BindsMenu(this));
            }
        }
    }
}
