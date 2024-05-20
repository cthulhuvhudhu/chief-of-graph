package chief.of.graph.models;

import chief.of.graph.SwingUtil;

import javax.swing.*;
import java.awt.*;

public class Vertex extends JPanel implements Model{
    public static final int VERTEX_SIZE = 50;
    private final char id;
    private final JLabel label;

    private static final Color VERTEX_COLOR = SwingUtil.SAGE_GRN_COLOR;
    private static final Color LABEL_COLOR = SwingUtil.CREAM_COLOR;
    private static final Color VISITED_COLOR = SwingUtil.BT_GRN_COLOR;
    public static final String VERTEX_NAMEFIX = "Vertex ";
    public static final String VERTEX_LABEL_NAMEFIX = "VertexLabel ";

    private Vertex(JLabel label, char id) {
        this.label = label;
        this.id = id;
    }

    public static Rectangle getFootprint(int x, int y) {
        return  new Rectangle(x - Vertex.diameter, y - Vertex.diameter, Vertex.VERTEX_SIZE, Vertex.VERTEX_SIZE);
    }

    public char getId() {
        return id;
    }

    public static final int diameter = VERTEX_SIZE / 2;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillOval(0, 0, VERTEX_SIZE, VERTEX_SIZE);
    }

    public static Vertex of(char id, int x, int y) {
        Vertex vertex = new Vertex(createLabel(id), id);
        vertex.setName(VERTEX_NAMEFIX + id);
        vertex.setPreferredSize(new Dimension(VERTEX_SIZE, VERTEX_SIZE));
        vertex.setLayout(new GridBagLayout());
        vertex.add(vertex.label);
        vertex.setOpaque(false);
        vertex.setBackground(VERTEX_COLOR);
        vertex.setBounds(x - diameter, y - diameter, VERTEX_SIZE, VERTEX_SIZE);
        return vertex;
    }

    private static JLabel createLabel(char id) {
        JLabel jLabel = new JLabel(String.valueOf(id), SwingConstants.CENTER);
        jLabel.setName(VERTEX_LABEL_NAMEFIX + id);
        jLabel.setForeground(LABEL_COLOR);
        jLabel.setOpaque(false);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        return jLabel;
    }

    public static Vertex prompt(JComponent c, int x, int y) {
        while (true) {
            String id = JOptionPane.showInputDialog(c, "Enter vertex id:", "Vertex", JOptionPane.PLAIN_MESSAGE);
            if (id != null) {
                if (id.length() == 1) {
                    return Vertex.of(id.charAt(0), x, y);
                }
                // Commenting out because I want this behavior, but breaks HyperSkill Tests: Error dialog
                //          JOptionPane.showMessageDialog(graph, "Invalid input. Please enter a single character.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                return null;
            }
        }
    }

    public boolean isVisited() {
        return getBackground().equals(VISITED_COLOR);
    }

    public void visit() {
        setBackground(VISITED_COLOR);
    }

    @Override
    public void unvisit() {
        setBackground(VERTEX_COLOR);
    }
}
