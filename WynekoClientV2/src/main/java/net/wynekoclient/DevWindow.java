package net.wynekoclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.awt.*;
import javax.swing.*;

import static net.wynekoclient.Overrides.fullBright;
import static net.wynekoclient.Overrides.noHurtCam;

public final class DevWindow {



    private final Minecraft mc = Minecraft.getMinecraft();

    private static JFrame W = null;
    private static JTabbedPane Tabs = null;
    private static int SelectedTab = 0;

    static void CreateWindow(){
        W = new JFrame("...");
        W.setResizable(false);

        W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        W.setSize(900,600);
        W.setVisible(true);

        CreateWindowContent();
    }

    private static JCheckBox CreateCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setSelected(false);
        return checkBox;
    }

    private static void CreateTab(String TabName, JPanel Content){
        Tabs.addTab(TabName, Content);
    }

    private static void CreateWindowContent(){
    Tabs = new JTabbedPane();

        CreateTab("Render"      , WynekoRender()); /* 0 */
        /*CreateTab("World"       , WynekoWorld());
        CreateTab("Player"      , WynekoPlayer()); */


        Tabs.addChangeListener((e) -> {
            SelectedTab = Tabs.getSelectedIndex();
        });

        W.add(Tabs);

    }

    //==========================================================//

    private static JPanel WynekoRender(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JCheckBox cbNoHurtCam = CreateCheckBox("No Hurt Cam");
        JCheckBox cbFullBright = CreateCheckBox("Full Bright");

        cbNoHurtCam.addItemListener(e -> {
            noHurtCam = cbNoHurtCam.isSelected();
            System.out.println("NoHurtCam = " + noHurtCam);
        });

        cbFullBright.addItemListener(e -> {
            fullBright = cbFullBright.isSelected();
            System.out.println("FullBright = " + fullBright);
        });

        panel.add(CreateCheckBox("No Hurt Cam "));
        panel.add(CreateCheckBox("Full Bright"));

        return panel;
    }
}
