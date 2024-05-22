package chief.of.graph.states;

import chief.of.graph.SwingUtil;

import javax.swing.*;
import java.awt.*;

public class AlgoModes {
    public static Algorithm currentAlgorithm = Algorithm.NOT_SELECTED;

    public static final Mode SELECT_VERTEX = new Mode("Please choose a starting vertex");
    public static final Mode RUNNING = new Mode("Please wait...");
    public static final Mode BLANK = new Mode("");
    public static final Mode defaultMode = BLANK;

    public static final JLabel displayLabel = createDisplayLabel();

    public record Mode(String copy) { }

    public enum Algorithm {
        BFS("Breadth-First Search"),
        DFS("Depth-First Search"),
        DIJKSTRA("Dijkstra's Algorithm"),
        PRIM("Prim's Algorithm"),
        NOT_SELECTED(null);

        private final String copy;

        Algorithm(String copy) {
            this.copy = copy;
        }

        public String getCopy() {
            return copy;
        }

        public static Algorithm getAlgorithm(String name) {
            for (Algorithm algo : Algorithm.values()) {
                if (algo.copy.equals(name)) {
                    return algo;
                }
            }
            return NOT_SELECTED;
        }
    }

    private static JLabel createDisplayLabel() {

        JLabel label = SwingUtil.label(AlgoModes.defaultMode.copy());
        label.setName("Display");
        label.setVerticalAlignment(SwingConstants.TOP);
        var font = label.getFont().deriveFont(Font.PLAIN, 14);
        label.setFont(font);
        label.setOpaque(true);
        label.setVisible(true);
        label.setBackground(SwingUtil.CREAM_COLOR);
        label.setForeground(SwingUtil.DK_GRN_COLOR);
        int padding = 5;
        label.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        return label;
    }

    public static void updateDisplayLabel(String text) {
        AlgoModes.displayLabel.setText(text);
        refresh();
    }

    public static void reset() {
        currentAlgorithm = Algorithm.NOT_SELECTED;
        displayLabel.setText(defaultMode.copy);
        refresh();
    }

    private static void refresh() {
        displayLabel.revalidate();
        displayLabel.repaint();
    }

    public static void updateDisplayLabel(AlgoModes.Mode mode) {
        AlgoModes.displayLabel.setText(mode.copy);
        AlgoModes.displayLabel.revalidate();
        AlgoModes.displayLabel.repaint();
    }
}
