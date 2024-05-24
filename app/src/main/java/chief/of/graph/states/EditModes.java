package chief.of.graph.states;

import chief.of.graph.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditModes {

    public static final Mode ADD_VERTEX = new Mode("Add a Vertex", KeyEvent.VK_V);
    public static final Mode ADD_EDGE = new Mode("Add an Edge", KeyEvent.VK_E);
    public static final Mode REMOVE_VERTEX = new Mode("Remove a Vertex");
    public static final Mode REMOVE_EDGE = new Mode("Remove an Edge");
    public static final Mode NONE = new Mode("None", KeyEvent.VK_N);

    public static final Mode[] modes = {ADD_VERTEX, ADD_EDGE, REMOVE_VERTEX, REMOVE_EDGE, NONE};

    public static final Mode defaultMode = ADD_VERTEX;

    public static final JLabel LABEL = createEditModeLabel();

    public static class Mode {

        private final String name;
        private Integer hotkey = null;

        private Mode(String name, int hotkey) {
            this.name = name;
            this.hotkey = hotkey;
        }

        private Mode(String name) {
            this.name = name;

        }

        public String getCopy() {
            return "Current Mode -> " + name;
        }

        public Integer getHotkey() {
            return hotkey;
        }

        public String getName() {
            return name;
        }
    }

    private static JLabel createEditModeLabel() {
        JLabel label = SwingUtil.label(EditModes.defaultMode.getCopy());
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

    public static void reset() {
        EditModes.LABEL.setText(defaultMode.getCopy());
        LABEL.revalidate();
        LABEL.repaint();
    }
}
