package net.wynekoclient;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.wynekoclient.features.PlayerESP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class DevWindow {

    public static boolean noHurtCam = false;
    public static boolean EntityESP = false;
    public static boolean fullBright = false;
    public static boolean customBlockOutline = false;
    public static boolean antiBlind = false;
    public static boolean wynekoNameTags = false;
    public static Color blockOutlineColor = Color.BLACK;
    public static boolean fastPlace = false;
    public static boolean fastPlaceRandom = false;

    public static float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;

    public static boolean espShader = false;
    public static boolean espBox = false;

    public IResourceManager resourceManager = mc.getResourceManager();
    public static final Logger logger = LogManager.getLogger();

    public static int fastPlaceSpeed = 0; // 0 = мгновенно, 4 = стандартно

    private static final Minecraft mc = Minecraft.getMinecraft();


    //== == == == == == == == =PROXY= == == == == == == == ==//

    public static String Type = "SOCKS5";
    public static String Host = "127.0.0.1";
    public static int Port = 9050;
    public static boolean ProxyEnabled = false;

    public static String GetHost() { return Host; }
    public static int GetPort() { return Port; }
    public static String GetType() { return Type; }
    public static String GetCbProxyEnabled() { return Type; }

    //== == == == == == == == =PROXY= == == == == == == == ==//

    private static JFrame W = null;
    private static JTabbedPane Tabs = null;
    private static int SelectedTab = 0;

    static void CreateWindow() {
        try {
            UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            W = new JFrame("Wyneko Client");
            W.setResizable(false);
            W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            W.setSize(900, 600);

            CreateWindowContent();

            W.setVisible(true);
        });
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
        CreateTab("Other", WynekoOtherPanel());

        Tabs.addChangeListener(e -> SelectedTab = Tabs.getSelectedIndex());

        W.add(Tabs);
    }

    private static JSeparator CreateSeparator(){
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }


    private static JPanel CreateDetails(String title, Map<String, Boolean> stateMap, JComponent... components) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JToggleButton toggle = new JToggleButton("▶ " + title);
        toggle.setFocusPainted(false);
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        for (JComponent comp : components) {
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);

            // если это чекбокс — регистрируем в stateMap
            if (comp instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) comp;
                stateMap.put(cb.getText(), cb.isSelected());
                cb.addActionListener(e -> stateMap.put(cb.getText(), cb.isSelected()));
            }

            content.add(comp);
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
        Map<String, Boolean> stateMap = new HashMap<>();
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        //Мусорка

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JCheckBox cbNoHurtCam = new JCheckBox("No HurtCam");
        JCheckBox cbFullBright = new JCheckBox("Full Bright");
        JCheckBox cbCustomBOutline = new JCheckBox("Custom Block Outline");
        JCheckBox cbEntityESP = new JCheckBox("ESP");
        JCheckBox cbWynekoNameTags = new JCheckBox("Wyneko NameTags");
        JCheckBox cbAntiBlind = new JCheckBox("AntiBlind");

        JCheckBox cbESPShader = new JCheckBox("Shader");
        JCheckBox cbESPBox = new JCheckBox("Box");

        JCheckBox cbWNTItemStack = new JCheckBox("ItemStack");
        JCheckBox cbWNTNickname = new JCheckBox("Nickname") ;

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
        cbEntityESP.addItemListener(e ->{
            EntityESP = cbEntityESP.isSelected();

        });

        cbWynekoNameTags.setSelected(wynekoNameTags);
        cbWynekoNameTags.addItemListener(e -> wynekoNameTags = cbWynekoNameTags.isSelected());

        cbAntiBlind.setSelected(antiBlind);
        cbAntiBlind.addItemListener(e -> antiBlind = cbAntiBlind.isSelected());

        cbESPShader.addItemListener(e -> espShader = cbESPShader.isSelected());
        cbESPShader.addItemListener(e -> espBox = cbESPBox.isSelected());

        if (espBox){
            PlayerESP.onRenderWorldLast(partialTicks);
            DevWindow.logger.error("Loaded EspBox condition");
        }

        //== == == == == == == Прослушивание Checkbox == == == == == == ==//


        leftPanel.add(cbNoHurtCam);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbFullBright);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbCustomBOutline);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbEntityESP);
        leftPanel.add(CreateDetails("Expand", stateMap, cbESPShader, cbESPBox));

        leftPanel.add(cbWynekoNameTags);
        leftPanel.add(CreateDetails("Expand", stateMap, cbWNTItemStack, cbWNTNickname));

        leftPanel.add(cbAntiBlind);
        leftPanel.add(CreateSeparator());

        localPanel.add(leftPanel, BorderLayout.WEST);


        //== == == == == == == == == == == == RightPanel == == == == == == == == == == == ==//

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

        JCheckBox cbRandomFastPlace = new JCheckBox("Random For Fast Place");
        cbRandomFastPlace.setSelected(fastPlaceRandom);
        cbRandomFastPlace.addItemListener(e -> fastPlaceRandom = cbRandomFastPlace.isSelected());

        JSlider speedSlider = new JSlider(0, 4, fastPlaceSpeed);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setBorder(BorderFactory.createTitledBorder("FastPlace Speed (0 = мгновенно)"));
        speedSlider.addChangeListener(e -> fastPlaceSpeed = speedSlider.getValue());

        localPanel.add(cbFastPlace);
        localPanel.add(cbRandomFastPlace);
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

    private static JPanel WynekoConnectionTab() {
        JPanel localPanel = new JPanel(new GridBagLayout());
        localPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // === Enable checkbox сверху слева
        JCheckBox enableBox = new JCheckBox("Enable proxy", ProxyEnabled);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; // растягиваем на три колонки
        localPanel.add(enableBox, gbc);
        row++;

        // === Host, Port, Type на одной строке
        gbc.gridwidth = 1; gbc.gridy = row;

        gbc.gridx = 0;
        localPanel.add(new JLabel("Host:"), gbc);
        gbc.gridx = 1;
        JTextField hostField = new JTextField(Host, 10);
        localPanel.add(hostField, gbc);

        gbc.gridx = 2;
        localPanel.add(new JLabel("Port:"), gbc);
        gbc.gridx = 3;
        JTextField portField = new JTextField(String.valueOf(Port), 5);
        localPanel.add(portField, gbc);

        gbc.gridx = 4;
        localPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 5;
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"SOCKS5", "HTTP"});
        typeBox.setSelectedItem(Type);
        localPanel.add(typeBox, gbc);
        row++;

        // === Buttons Connect / Disconnect
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 6;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton connectBtn = new JButton("Connect");
        JButton disconnectBtn = new JButton("Disconnect");
        btnRow.add(connectBtn);
        btnRow.add(disconnectBtn);
        localPanel.add(btnRow, gbc);
        row++;

        // === Info label
        gbc.gridy = row;
        JLabel infoLabel = new JLabel("Proxy info: disconnected");
        localPanel.add(infoLabel, gbc);

        // === Actions
        connectBtn.addActionListener(e -> {
            try {
                ProxyEnabled = enableBox.isSelected();
                Host = hostField.getText().trim();
                Port = Integer.parseInt(portField.getText().trim());
                Type = (String) typeBox.getSelectedItem();

                Proxy proxy;
                if (ProxyEnabled) {
                    Proxy.Type t = Type.equalsIgnoreCase("HTTP") ? Proxy.Type.HTTP : Proxy.Type.SOCKS;
                    proxy = new Proxy(t, new InetSocketAddress(Host, Port));
                } else {
                    proxy = Proxy.NO_PROXY;
                }

                // Запрос информации о текущем IP через прокси
                URL url = new URL("http://ip-api.com/json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONObject obj = new JSONObject(sb.toString());
                String ip = obj.optString("query", "?");
                String country = obj.optString("country", "?");
                String city = obj.optString("city", "?");

                infoLabel.setText(String.format("Proxy info: IP=%s, Country=%s, City=%s, Type=%s",
                        ip, country, city, Type));

            } catch (Exception ex) {
                infoLabel.setText("Proxy connection failed: " + ex.getMessage());
            }
        });

        disconnectBtn.addActionListener(e -> {
            ProxyEnabled = false;
            infoLabel.setText("Proxy info: disconnected");
        });

        return localPanel;
    }



    private static JPanel WynekoOtherPanel(){
        JPanel localPanel = new JPanel();
        localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Other settings coming soon...");
        localPanel.add(label);

        return localPanel;
    }

    private static void OnUpdate(){

    }
}
