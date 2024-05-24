package chief.of.graph.containers;

import chief.of.graph.SwingUtil;
import chief.of.graph.states.AlgoModes;
import chief.of.graph.states.EditModes;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SwingUtil.WIDTH, SwingUtil.HEIGHT);
        setLocationRelativeTo(null);
        add(Graph.getInstance());
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditModeMenu());
        menuBar.add(createAlgoMenu());
        setJMenuBar(menuBar);
        add(EditModes.LABEL, BorderLayout.NORTH);
        add(AlgoModes.displayLabel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu menu = createMenu("File", KeyEvent.VK_F);
        JMenuItem newItem = createMenuItem("New", e -> {
            AlgoModes.reset();
            EditModes.reset();
            Graph.getInstance().removeAll();
            Graph.refresh();
        });
        JMenuItem exitItem = createMenuItem("Exit", e -> System.exit(0));
        menu.add(newItem);
        menu.add(exitItem);
        return menu;
    }
    private JMenu createEditModeMenu() {
        JMenu menu = createMenu("Mode", KeyEvent.VK_M);
        createEditModeMenuItems().forEach(menu::add);
        return menu;
    }

    private JMenu createAlgoMenu() {
        JMenu menu = createMenu("Algorithms", KeyEvent.VK_A);
        menu.add(createMenuItem(AlgoModes.Algorithm.DFS.getCopy(), algoActionListener));
        menu.add(createMenuItem(AlgoModes.Algorithm.BFS.getCopy(), algoActionListener));
        menu.add(createMenuItem(AlgoModes.Algorithm.DIJKSTRA.getCopy(), algoActionListener));
        menu.add(createMenuItem(AlgoModes.Algorithm.PRIM.getCopy(), algoActionListener));
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

    private List<JMenuItem> createEditModeMenuItems() {
        return Arrays.stream(EditModes.modes)
                .map(m -> {
                    var item = createMenuItem(m.getName(), editModeActionListener);
                    if (m.getHotkey() != null) {
                        item.setMnemonic(m.getHotkey());
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    private final ActionListener editModeActionListener = e -> {
        Graph.reset();
        AlgoModes.displayLabel.setText(AlgoModes.defaultMode.copy());
        JMenuItem item = (JMenuItem) e.getSource();
        EditModes.Mode newMode = Arrays.stream(EditModes.modes)
                .filter(m -> m.getName().equals(item.getText()))
                .findFirst().orElse(EditModes.defaultMode);
        EditModes.LABEL.setText(newMode.getCopy());
        revalidate();
        repaint();
    };

    private final ActionListener algoActionListener = e -> {
        Graph.reset();
        EditModes.LABEL.setText(EditModes.NONE.getCopy());
        AlgoModes.currentAlgorithm = AlgoModes.Algorithm.getAlgorithm(((JMenuItem) e.getSource()).getText());
        AlgoModes.displayLabel.setText(AlgoModes.SELECT_VERTEX.copy());
        revalidate();
        repaint();
    };
}
