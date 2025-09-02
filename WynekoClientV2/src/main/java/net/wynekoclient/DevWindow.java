package net.wynekoclient;

import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class DevWindow {

    public static boolean noHurtCam = false;
    public static boolean EntityESP = false;
    public static boolean fullBright = false;
    public static boolean customBlockOutline = false;
    public static Color blockOutlineColor = Color.BLACK;
    public static boolean fastPlace = false;

    public static int fastPlaceSpeed = 0; // 0 = мгновенно, 4 = стандартно

    private final Minecraft mc = Minecraft.getMinecraft();

    private static JFrame W = null;
    private static JTabbedPane Tabs = null;
    private static int SelectedTab = 0;

    static void CreateWindow() {
        W = new JFrame("Wyneko Client");
        W.setResizable(false);
        W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        W.setSize(900, 600);

        CreateWindowContent();

        W.setVisible(true);
    }

    private static void CreateTab(String TabName, JPanel Content) {
        Tabs.addTab(TabName, Content);
    }

    private static void CreateWindowContent() {
        Tabs = new JTabbedPane();

        CreateTab("Render", WynekoRenderTab());
        CreateTab("World", WynekoWorldTab());
        CreateTab("Player", WynekoPlayerTab());
        CreateTab("Connection", WynekoConnectionTab());

        Tabs.addChangeListener(e -> SelectedTab = Tabs.getSelectedIndex());

        W.add(Tabs);
    }

    /*private static JPanel CreateDetails(String title, JComponent... components) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JToggleButton toggle = new JToggleButton("▶ " + title);
        toggle.setFocusPainted(false);
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        for (JComponent c : components) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(c);
        }
        content.setVisible(false);

        toggle.addActionListener(e -> {
            boolean expanded = toggle.isSelected();
            toggle.setText((expanded ? "▼ " : "▶ ") + title);
            content.setVisible(expanded);
            container.revalidate();
            container.repaint();
        });

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));


        container.add(toggle);
        container.add(content);
        container.add(separator);

        return container;
    }*/

    private static JSeparator CreateSeparator(){
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    private static JPanel CreateDetails(String title, Map<String, Boolean> stateMap, String... names) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JToggleButton toggle = new JToggleButton("▶ " + title);
        toggle.setFocusPainted(false);
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        for (String name : names) {
            JCheckBox cb = new JCheckBox(name);

            // инициализируем значение
            stateMap.put(name, cb.isSelected());

            // слушатель для обновления stateMap
            cb.addActionListener(e -> stateMap.put(name, cb.isSelected()));

            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(cb);
        }

        content.setVisible(false);

        toggle.addActionListener(e -> {
            boolean expanded = toggle.isSelected();
            toggle.setText((expanded ? "▼ " : "▶ ") + title);
            content.setVisible(expanded);
            container.revalidate();
            container.repaint();
        });

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        container.add(toggle);
        container.add(content);
        container.add(separator);

        return container;
    }

    //==========================================================//

    private static JPanel WynekoRenderTab() {
        JPanel localPanel = new JPanel(new BorderLayout());

        //Левая панель с чекбоксами >:G эта строка проклята
        JPanel leftPanel = new JPanel();

        //Мусорка
        Map<String, Boolean> expandStates = new HashMap<>();
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        //Мусорка

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JCheckBox cbNoHurtCam = new JCheckBox("No HurtCam");
        JCheckBox cbFullBright = new JCheckBox("Full Bright");
        JCheckBox cbCustomBOutline = new JCheckBox("Custom Block Outline");
        JCheckBox cbEntityESP = new JCheckBox("ESP");

        //== == == == == == == Прослушивание Checkbox == == == == == == ==//

        cbNoHurtCam.setSelected(noHurtCam);
        cbNoHurtCam.addItemListener(e -> noHurtCam = cbNoHurtCam.isSelected());

        cbFullBright.setSelected(fullBright);
        cbFullBright.addItemListener(e -> {
            fullBright = cbFullBright.isSelected();
            Minecraft mc = Minecraft.getMinecraft();
            mc.gameSettings.gammaSetting = fullBright ? 1000.0F : 1.0F;
        });

        cbCustomBOutline.setSelected(customBlockOutline);
        cbCustomBOutline.addItemListener(e -> customBlockOutline = cbCustomBOutline.isSelected());

        cbEntityESP.setSelected(EntityESP);
        cbEntityESP.addItemListener(e -> EntityESP = cbEntityESP.isSelected());

        //== == == == == == == Прослушивание Checkbox == == == == == == ==//



        leftPanel.add(cbNoHurtCam);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbFullBright);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbCustomBOutline);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbEntityESP);
        leftPanel.add(CreateDetails("Expand", expandStates, "Shader", "Box"));

        localPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JSlider redSlider = new JSlider(0, 255, 0);
        redSlider.setBorder(BorderFactory.createTitledBorder("Red"));
        JSlider greenSlider = new JSlider(0, 255, 0);
        greenSlider.setBorder(BorderFactory.createTitledBorder("Green"));
        JSlider blueSlider = new JSlider(0, 255, 0);
        blueSlider.setBorder(BorderFactory.createTitledBorder("Blue"));
        JSlider alphaSlider = new JSlider(0, 255, 255);
        alphaSlider.setBorder(BorderFactory.createTitledBorder("Alpha"));

        JPanel colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(0, 50));
        colorPreview.setBackground(Color.BLACK);
        colorPreview.setBorder(BorderFactory.createTitledBorder("Preview"));
        colorPreview.setMinimumSize(new Dimension(40, 15));
        colorPreview.setMaximumSize(new Dimension(400, 150));


        Runnable updateColor = () -> {
            int r = redSlider.getValue();
            int g = greenSlider.getValue();
            int b = blueSlider.getValue();
            int a = alphaSlider.getValue();
            Color newColor = new Color(r, g, b, a);

            colorPreview.setBackground(newColor);
            blockOutlineColor = newColor;
        };
        redSlider.addChangeListener(e -> updateColor.run());
        greenSlider.addChangeListener(e -> updateColor.run());
        blueSlider.addChangeListener(e -> updateColor.run());
        alphaSlider.addChangeListener(e -> updateColor.run());

        rightPanel.add(redSlider);
        rightPanel.add(greenSlider);
        rightPanel.add(blueSlider);
        rightPanel.add(alphaSlider);
        rightPanel.add(colorPreview);

        localPanel.add(rightPanel, BorderLayout.CENTER);

        return localPanel;
    }


    public static JPanel WynekoWorldTab() {
        JPanel localPanel = new JPanel();
        localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));

        JCheckBox cbFastPlace = new JCheckBox("Fast Place");
        cbFastPlace.setSelected(fastPlace);
        cbFastPlace.addItemListener(e -> fastPlace = cbFastPlace.isSelected());

        JSlider speedSlider = new JSlider(0, 4, fastPlaceSpeed);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setBorder(BorderFactory.createTitledBorder("FastPlace Speed (0 = мгновенно)"));
        speedSlider.addChangeListener(e -> fastPlaceSpeed = speedSlider.getValue());

        localPanel.add(cbFastPlace);
        localPanel.add(speedSlider);

        return localPanel;
    }

    private static JPanel WynekoPlayerTab() {
        JPanel localPanel = new JPanel();
        localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Player settings coming soon...");
        localPanel.add(label);

        return localPanel;
    }

    private static JPanel WynekoConnectionTab(){
        JPanel localPanel = new JPanel();
        localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Proxy settings coming soon...");
        localPanel.add(label);

        return localPanel;
    }

    private static void OnUpdate(){

    }
}
