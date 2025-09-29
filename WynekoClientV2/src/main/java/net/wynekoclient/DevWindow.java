package net.wynekoclient;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DevWindow {

    public static boolean noHurtCam = false;
    public static boolean fullBright = false;
    public static boolean customBlockOutline = false;
    public static boolean rainbowBlockOutline = false;
    public static boolean antiBlind = false;
    public static boolean wynekoNameTags = false;
    public static Color blockOutlineColor = Color.BLACK;

    public static boolean customCrosshair = false;

    public static boolean fastPlace = false;
    public static boolean fastPlaceRandom = false;

    // == == == == == == == Other STR == == == == == == == ==

    public static boolean customMainMenu = true;

    private static int currentIndex = 0;
    private static List<String> pngFiles = new ArrayList<>();
    public static ResourceLocation customBg = new ResourceLocation("wynekoclient/themes/1.png");


    // == == == == == == == Other END == == == == == == == ==

    public static boolean keystroks = false;

    // == == == == == == == Tracers STR == == == == == == == ==

    public static boolean tracers = false;
    public static float lineWidth = 3.0f;
    public static boolean distanceBasedColor = true;
    public static boolean rainbowTracers = false;

    // == == == == == == == Tracers END == == == == == == == ==

    public static float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;

    public static boolean entityESP = false;
    public static boolean playerEspShader = false;
    public static boolean playerEspBox = false;
    public static boolean rainbowPlayerEsp = false;

    public static boolean playerEspED = false;

    public static final Logger logger = LogManager.getLogger();

    public static int fastPlaceSpeed = 0; // 0 = мгновенно, 4 = стандартно

    private static final Minecraft mc = Minecraft.getMinecraft();
    public IResourceManager resourceManager = mc.getResourceManager();


    //== == == == == == == == =PROXY= START == == == == == == == ==//

    public static String Type = "SOCKS5";
    public static String Host = "127.0.0.1";
    public static int Port = 9050;
    public static boolean ProxyEnabled = false;

    public static String GetHost() { return Host; }
    public static int GetPort() { return Port; }
    public static String GetType() { return Type; }

    //== == == == == == == == =PROXY= END == == == == == == == ==//

    private static JFrame W = null;
    private static JTabbedPane Tabs = null;
    private static int SelectedTab = 0;

    static void init(){
        CreateWindow();

        File folder = new File("src/main/resources/assets/minecraft/wynekoclient/themes/");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (files != null) {
            for (File f : files) {
                pngFiles.add(f.getName());
            }
        }
    }

    static void CreateWindow() {
        try {
            UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            W = new JFrame("Wyneko Client");
            W.setResizable(false);
            W.setSize(900, 600);
            W.setLocationRelativeTo(null);
            W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            W.setLocation(25, 25);
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

        content.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        for (JComponent comp : components) {
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        // === === === === === Block Outline === === === === === ===
        JCheckBox cbCustomBOutline = new JCheckBox("Custom Block Outline");
        JCheckBox cbRainbowBOutline = new JCheckBox("Rainbow Block Outline");
        // === === === === === === === === === === ===

        JCheckBox cbKeystroks = new JCheckBox("Keystroks");

        JCheckBox cbWynekoNameTags = new JCheckBox("Wyneko NameTags");

        JCheckBox cbCustomCrosshair = new JCheckBox("Custom Crosshair");

        JCheckBox cbAntiBlind = new JCheckBox("AntiBlind");
        JCheckBox cbTracers = new JCheckBox("Tracers");

        //=== === === ESP === === ===

        JCheckBox cbEntityESP = new JCheckBox("ESP");

        JCheckBox cbESPRainbow = new JCheckBox("Rainbow");
        JCheckBox cbESPDTE = new JCheckBox("Direction Of Eyes");

        JCheckBox cbESPShader = new JCheckBox("ESP Shader");
        JCheckBox cbESPBox = new JCheckBox("ESP Box");

        //=== === === ESP === === ===



        //Tracers//

        JCheckBox cbTracerRainbow = new JCheckBox("Rainbow Tracers");
        JCheckBox cbTracerNearPlayer = new JCheckBox("Near Player Tracers");
        JLabel slTracerLineWidthLabel = new JLabel("Line Width");
        JSlider slTracerLineWidth = new JSlider(1,10,3);

        //Tracers//

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

        cbRainbowBOutline.setSelected(rainbowBlockOutline);
        cbRainbowBOutline.addItemListener(e -> rainbowBlockOutline = cbRainbowBOutline.isSelected());

        cbKeystroks.setSelected(keystroks);
        cbKeystroks.addItemListener(e -> keystroks = cbKeystroks.isSelected());

        cbEntityESP.setSelected(entityESP);
        cbEntityESP.addItemListener(e -> entityESP = cbEntityESP.isSelected());

        cbWynekoNameTags.setSelected(wynekoNameTags);
        cbWynekoNameTags.addItemListener(e -> wynekoNameTags = cbWynekoNameTags.isSelected());

        cbAntiBlind.setSelected(antiBlind);
        cbAntiBlind.addItemListener(e -> antiBlind = cbAntiBlind.isSelected());

        cbTracers.setSelected(tracers);
        cbTracers.addItemListener(e -> tracers = cbTracers.isSelected());

        cbTracerRainbow.setSelected(rainbowTracers);
        cbTracerRainbow.addItemListener(e -> rainbowTracers = cbTracerRainbow.isSelected());

        cbTracerNearPlayer.setSelected(distanceBasedColor);
        cbTracerNearPlayer.addItemListener(e -> distanceBasedColor = cbTracerRainbow.isSelected());

        cbCustomCrosshair.setSelected(customCrosshair);
        cbCustomCrosshair.addItemListener(e -> customCrosshair = cbCustomCrosshair.isSelected());


        //ESP)))
            cbEntityESP.addItemListener(e -> {
                if (cbEntityESP.isSelected()) {
                    playerEspShader = false;
                    rainbowPlayerEsp = false;
                    playerEspBox = true;
                    playerEspED = false;

                    cbESPBox.setSelected(true);
                    cbESPShader.setSelected(false);
                    cbESPRainbow.setSelected(false);
                    cbESPDTE.setSelected(false);

                } else {
                    playerEspBox = false;
                    playerEspShader = false;
                    rainbowPlayerEsp = false;
                    playerEspED = false;

                    cbESPBox.setSelected(false);
                    cbESPShader.setSelected(false);
                    cbESPRainbow.setSelected(false);
                    cbESPDTE.setSelected(false);
                }
            });

            cbESPDTE.addItemListener(e -> {
                if (!cbEntityESP.isSelected() || !cbESPBox.isSelected()) {
                    cbESPDTE.setSelected(false);
                    playerEspED = false;
                } else {
                    playerEspED = cbESPDTE.isSelected();
                }
            });

            cbESPRainbow.addItemListener(e -> {
                if (!cbEntityESP.isSelected()) {
                    cbESPRainbow.setSelected(false);
                    rainbowPlayerEsp = false;
                } else {
                    rainbowPlayerEsp = cbESPRainbow.isSelected();
                }
            });

            cbESPShader.addItemListener(e -> {
                if (!cbEntityESP.isSelected()) {
                    cbESPShader.setSelected(false);
                    playerEspShader = false;
                    return;
                }
                if (cbESPShader.isSelected()) {
                    playerEspShader = true;
                    playerEspBox = false;

                    cbESPBox.setSelected(false);
                } else {
                    playerEspShader = false;
                }
            });

            cbESPBox.addItemListener(e -> {
                if (!cbEntityESP.isSelected()) {
                    cbESPBox.setSelected(false);
                    playerEspBox = false;
                    return;
                }
                if (cbESPBox.isSelected()) {
                    playerEspBox = true;
                    playerEspShader = false;

                    cbESPShader.setSelected(false);
                } else {
                    playerEspBox = false;
                }
            });



        //Block Outline
        cbRainbowBOutline.addItemListener(e -> {
            if (cbRainbowBOutline.isSelected()) {
                rainbowBlockOutline = true;
                customBlockOutline = false;

                cbCustomBOutline.setSelected(false); // выключаем кастом
            } else {
                rainbowBlockOutline = false;
            }
        });

        cbCustomBOutline.addItemListener(e -> {
            if (cbCustomBOutline.isSelected()) {
                customBlockOutline = true;
                rainbowBlockOutline = false;

                cbRainbowBOutline.setSelected(false); // выключаем радужный
            } else {
                customBlockOutline = false;
            }
        });


        //Tracers
        cbTracerRainbow.addItemListener(e -> {
            if (cbTracerRainbow.isSelected()) {
                rainbowTracers = true;
                distanceBasedColor = false;

                cbTracerNearPlayer.setSelected(false);
            } else {
                rainbowTracers = false;
            }
        });

        cbTracerNearPlayer.addItemListener(e -> {
            if (cbTracerNearPlayer.isSelected()) {
                distanceBasedColor = true;
                rainbowTracers = false;

                cbTracerRainbow.setSelected(false);
            } else {
                distanceBasedColor = false;
            }
        });

        slTracerLineWidth.addChangeListener(e -> {
            lineWidth = slTracerLineWidth.getValue();
        });


        //== == == == == == == Прослушивание Checkbox == == == == == == ==//


        leftPanel.add(cbNoHurtCam);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbFullBright);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbCustomBOutline);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbRainbowBOutline);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbKeystroks);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbEntityESP);
        leftPanel.add(CreateDetails("Expand", stateMap, cbESPShader, cbESPBox, cbESPRainbow));

        leftPanel.add(cbWynekoNameTags);
        leftPanel.add(CreateDetails("Expand", stateMap, cbWNTItemStack, cbWNTNickname));

        leftPanel.add(cbCustomCrosshair);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbAntiBlind);
        leftPanel.add(CreateSeparator());

        leftPanel.add(cbTracers);
        leftPanel.add(CreateDetails("Expand", stateMap, cbTracerNearPlayer, cbTracerRainbow, slTracerLineWidthLabel, slTracerLineWidth));

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

        rightPanel.setVisible(false);

        cbCustomBOutline.addActionListener(e -> {
            if (customBlockOutline) {
                rightPanel.setVisible(cbCustomBOutline.isSelected());
                localPanel.revalidate();
                localPanel.repaint();
            }
        });

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

        JSlider speedSlider = new JSlider(0, 30, fastPlaceSpeed);
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


        hostField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Host = hostField.getText().trim();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Host = hostField.getText().trim();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Host = hostField.getText().trim();
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
        JCheckBox cbCustomMainMenu = new JCheckBox("Custom MainMenu");
        JButton bNextBG = new JButton("Next Custom Background");

        // Слушатели swing

        cbCustomMainMenu.setSelected(customMainMenu);
        cbCustomMainMenu.addItemListener(e -> customMainMenu = cbCustomMainMenu.isSelected());

        bNextBG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pngFiles.isEmpty()) return;

                currentIndex = (currentIndex + 1) % pngFiles.size();
                String fileName = pngFiles.get(currentIndex);
                customBg = new ResourceLocation("wynekoclient/themes/" + fileName);
            }
        });

        // Слушатели swing



        localPanel.add(label);
        localPanel.add(cbCustomMainMenu);
        localPanel.add(bNextBG);

        return localPanel;
    }
}
