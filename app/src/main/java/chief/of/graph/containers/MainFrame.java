package chief.of.graph.containers;

import chief.of.graph.SwingUtil;
import chief.of.graph.models.Modes;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    public static final JLabel modeLabel = createModeLabel();

    private static JLabel createModeLabel() {
        JLabel label = SwingUtil.label(Modes.defaultMode.getName());
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setName("Mode");
        label.setVerticalAlignment(SwingConstants.TOP);
        var font = label.getFont().deriveFont(Font.PLAIN, 14);
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(SwingUtil.CREAM_COLOR);
        label.setForeground(SwingUtil.DK_GRN_COLOR);
        int padding = 5;
        label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        return label;
    }

    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SwingUtil.WIDTH, SwingUtil.HEIGHT);
        setLocationRelativeTo(null);
        add(Graph.getInstance());
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createModeMenu());
        setJMenuBar(menuBar);
        add(modeLabel, BorderLayout.NORTH);
        setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu menu = createMenu("File", KeyEvent.VK_F);
        JMenuItem newItem = createMenuItem("New", e -> {
            Graph.getInstance().removeAll();
            revalidate();
            repaint();
        });
        JMenuItem exitItem = createMenuItem("Exit", e -> System.exit(0));
        menu.add(newItem);
        menu.add(exitItem);
        return menu;
    }

    private JMenu createModeMenu() {
        JMenu menu = createMenu("Mode", KeyEvent.VK_M);
        createModeMenuItems().forEach(menu::add);
        return menu;
    }

    private JMenu createMenu(String name, int mnemonic) {
        JMenu menu = new JMenu(name);
        menu.setName(name);
        menu.setBackground(SwingUtil.CREAM_COLOR);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    private JMenuItem createMenuItem(String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.setName(name);
        item.addActionListener(listener);
        return item;
    }

    private List<JMenuItem> createModeMenuItems() {
        return Arrays.stream(Modes.modes)
                .map(m -> {
                    var item = createMenuItem(m.getName(), modeActionListener);
                    if (m.getHotkey() != null) {
                        item.setMnemonic(m.getHotkey());
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    private final ActionListener modeActionListener = e -> {
        Graph.reset();
        JMenuItem item = (JMenuItem) e.getSource();
        Modes.Mode newMode = Arrays.stream(Modes.modes)
                .filter(m -> m.getName().equals(item.getText()))
                .findFirst().orElse(Modes.defaultMode);
        modeLabel.setText(newMode.getName());
        revalidate();
        repaint();
    };
}
