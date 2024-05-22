package chief.of.graph;

import javax.swing.*;
import java.awt.*;

public class SwingUtil {

    public static final Color CREAM_COLOR = new Color(251 , 250, 218);
    public static final Color BT_GRN_COLOR = new Color(73, 160, 27);
    public static final Color SAGE_GRN_COLOR = new Color(67, 104, 80);
    public static final Color DK_GRN_COLOR = new Color(3, 38, 26);

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int DELAY = 500;

    public static JLabel label(String text) {
        JLabel jLabel = new JLabel(text, SwingConstants.CENTER);
        Font labelFont = jLabel.getFont();
        jLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 18));
        jLabel.setOpaque(false);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        return jLabel;
    }
}
