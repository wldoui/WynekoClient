package net.wynekoclient;

import net.minecraft.client.Minecraft;
import java.awt.*;
import javax.swing.*;


public final class DevWindow {

    public static boolean noHurtCam = false;
    public static boolean fullBright = false;
    private static boolean fastPlaceEnabled = false;
    private static int fastPlaceSpeed = 0; // 0 = мгновенно, 4 = стандартно

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

    /*private static JCheckBox CreateCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setSelected(false);
        return checkBox;
    }*/

    private static void CreateTab(String TabName, JPanel Content){
        Tabs.addTab(TabName, Content);
    }

    private static void CreateWindowContent(){
    Tabs = new JTabbedPane();

        CreateTab("Render"      , WynekoRenderTab());
        CreateTab("World"       , WynekoWorldTab());
        CreateTab("Player"      , WynekoPlayerTab());


        Tabs.addChangeListener((e) -> {
            SelectedTab = Tabs.getSelectedIndex();
        });

        W.add(Tabs);

    }

    //==========================================================//

    private static JPanel WynekoRenderTab(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JCheckBox cbNoHurtCam = new JCheckBox("No HurtCam");
        JCheckBox cbFullBright = new JCheckBox("Full Bright");

        cbNoHurtCam.setSelected(noHurtCam);
        cbNoHurtCam.addItemListener(e -> noHurtCam = cbNoHurtCam.isSelected());

        /*cbNoHurtCam.setSelected(fullBright);
        cbNoHurtCam.addItemListener(e -> fullBright = cbFullBright.isSelected());*/

        cbFullBright.setSelected(fullBright);
        cbFullBright.addItemListener(e -> {
            fullBright = cbFullBright.isSelected();
            Minecraft mc = Minecraft.getMinecraft();
            if (fullBright) {
                mc.gameSettings.gammaSetting = 1000.0F;
            } else {
                mc.gameSettings.gammaSetting = 1.0F;
            }
        });

        panel.add(cbNoHurtCam);
        panel.add(cbFullBright);

        return panel;
    }

    public static JPanel WynekoWorldTab() {
        return null;
    }


    private static JPanel WynekoPlayerTab(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));



        JCheckBox cbNoHurtCam = new JCheckBox("SEX)))))");
        //JCheckBox cbFullBright = CreateCheckBox("Full Bright");

        cbNoHurtCam.setSelected(noHurtCam);
        cbNoHurtCam.addItemListener(e -> noHurtCam = cbNoHurtCam.isSelected());

        panel.add(cbNoHurtCam);
        //panel.add(CreateCheckBox("Full Bright"));

        return panel;
    }
}
