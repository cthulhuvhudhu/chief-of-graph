package chief.of.graph.models;

import java.awt.event.*;

public class Modes {

    public static final Mode ADD_VERTEX = new Mode("Add a Vertex", KeyEvent.VK_V);
    public static final Mode ADD_EDGE = new Mode("Add an Edge", KeyEvent.VK_E);
    public static final Mode REMOVE_VERTEX = new Mode("Remove a Vertex");
    public static final Mode REMOVE_EDGE = new Mode("Remove an Edge");
    public static final Mode NONE = new Mode("None", KeyEvent.VK_N);

    public static final Mode[] modes = {ADD_VERTEX, ADD_EDGE, REMOVE_VERTEX, REMOVE_EDGE, NONE};

    public static final Mode defaultMode = ADD_VERTEX;

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

        public String getName() {
            return name;
        }

        public Integer getHotkey() {
            return hotkey;
        }
    }
}
