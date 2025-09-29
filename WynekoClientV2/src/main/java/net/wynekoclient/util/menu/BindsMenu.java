package net.wynekoclient.util.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BindsMenu extends GuiScreen {

    private final GuiScreen parent;

    // -------------------- MACRO CLASS --------------------
    public static class Macro {
        public int key;
        public String command;

        public Macro(int key, String command) {
            this.key = key;
            this.command = command;
        }

        @Override
        public String toString() {
            return key + ":" + command;
        }
    }

    // -------------------- MANAGER --------------------
    public static class MacroManager {
        private static final List<Macro> macros = new ArrayList<>();

        private static File getFile() {
            File dir = new File(Minecraft.getMinecraft().mcDataDir, "WynekoClient/config");
            if (!dir.exists()) dir.mkdirs();
            return new File(dir, "config.txt");
        }

        public static void load() {
            macros.clear();
            File file = getFile();
            if (!file.exists()) return;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(":", 2);
                    if (split.length == 2) {
                        int key = Integer.parseInt(split[0]);
                        String cmd = split[1];
                        macros.add(new Macro(key, cmd));
                    }
                }
            } catch (Exception ignored) {}
        }

        public static void save() {
            File file = getFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (Macro m : macros) {
                    writer.println(m.toString());
                }
            } catch (Exception ignored) {}
        }

        public static List<Macro> getMacros() {
            return macros;
        }

        public static void addMacro(Macro macro) {
            macros.add(macro);
            save();
        }

        public static void removeMacro(Macro macro) {
            macros.remove(macro);
            save();
        }
    }

    // -------------------- MAIN GUI --------------------
    public BindsMenu(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 60, 98, 20, "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 2, this.height - 60, 98, 20, "Remove"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height - 30, 200, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Binds Menu", this.width / 2, 20, 0xFFFFFF);

        int y = 50;
        for (Macro m : MacroManager.getMacros()) {
            this.drawString(this.fontRendererObj, "Key: " + Keyboard.getKeyName(m.key), this.width / 2 - 100, y, 0xFFFFFF);
            this.drawString(this.fontRendererObj, m.command, this.width / 2, y, 0xAAAAAA);
            y += 15;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAddMacro(this));
        } else if (button.id == 1) {
            if (!MacroManager.getMacros().isEmpty()) {
                MacroManager.removeMacro(MacroManager.getMacros().get(MacroManager.getMacros().size() - 1));
            }
        } else if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    // -------------------- ADD MACRO SCREEN --------------------
    public static class GuiAddMacro extends GuiScreen {
        private final GuiScreen parent;
        private GuiTextField cmdField;
        private int selectedKey = -1;
        private boolean waitingForKey = false;

        public GuiAddMacro(GuiScreen parent) {
            this.parent = parent;
        }

        @Override
        public void initGui() {
            this.buttonList.clear();
            this.cmdField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 80, 200, 20);
            this.cmdField.setMaxStringLength(100);

            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 90, 200, 20, "Set Key"));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 60, 200, 20, "Save"));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height - 30, 200, 20, "Cancel"));
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "Add Macro", this.width / 2, 20, 0xFFFFFF);
            this.cmdField.drawTextBox();

            String keyText;
            if (waitingForKey) {
                keyText = "Press any key...";
            } else if (selectedKey == -1) {
                keyText = "No key selected";
            } else {
                keyText = "Key: " + Keyboard.getKeyName(selectedKey);
            }
            this.drawCenteredString(this.fontRendererObj, keyText, this.width / 2, 60, 0xAAAAAA);

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            if (waitingForKey) {
                if (keyCode != Keyboard.KEY_ESCAPE) {
                    selectedKey = keyCode;
                }
                waitingForKey = false;
            } else {
                this.cmdField.textboxKeyTyped(typedChar, keyCode);
            }
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 0) {
                waitingForKey = true;
            } else if (button.id == 1 && selectedKey != -1 && !this.cmdField.getText().isEmpty()) {
                MacroManager.addMacro(new Macro(selectedKey, this.cmdField.getText()));
                this.mc.displayGuiScreen(parent);
            } else if (button.id == 2) {
                this.mc.displayGuiScreen(parent);
            }
        }

        @Override
        public void updateScreen() {
            this.cmdField.updateCursorCounter();
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.cmdField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    // -------------------- KEY HANDLER --------------------
    public static class MacroHandler {
        private static final Minecraft mc = Minecraft.getMinecraft();
        private static final List<Integer> pressed = new ArrayList<>();

        public static void onTick() {
            if (mc.thePlayer == null || mc.currentScreen != null) return;

            for (Macro m : MacroManager.getMacros()) {
                if (Keyboard.isKeyDown(m.key)) {
                    if (!pressed.contains(m.key)) {
                        pressed.add(m.key);
                        mc.thePlayer.sendChatMessage(m.command);
                    }
                } else {
                    pressed.remove((Integer) m.key);
                }
            }
        }
    }
}