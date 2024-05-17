package chief.of.graph.models;

import chief.of.graph.SwingUtil;

import javax.swing.*;
import java.awt.*;

public class Edge extends JPanel {

    private final Vertex source;
    private final Vertex target;
    private final JLabel label;
    private final int weight;
    private final String id;

    private static final Color EDGE_COLOR = SwingUtil.SAGE_GRN_COLOR;
    private static final Color MODE_LABEL = SwingUtil.DK_GRN_COLOR;
    public static final String EDGE_NAMEFIX = "Edge ";
    public static final String EDGE_LABEL_NAMEFIX = "EdgeLabel ";


    public Edge(Vertex source, Vertex target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;

        id = String.format("<%s -> %s>", source.getId(), target.getId());
        label = SwingUtil.label(String.valueOf(weight));
        label.setName(EDGE_LABEL_NAMEFIX + id);
        label.setForeground(MODE_LABEL);
    }

    public JLabel getLabel() {
        return label;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    public static Edge of(Vertex source, Vertex target, int weight)  {
        Edge edge = new Edge(source, target, weight);
        edge.setName(EDGE_NAMEFIX + edge.id);
        edge.setLayout(new GridBagLayout());
        edge.setVisible(true);
        edge.setOpaque(false);
        edge.setEdgeBounds();
        edge.setLabelBounds();

        return edge;
    }

    private void setLabelBounds() {

        int xDiff = target.getX() - source.getX();
        int yDiff = target.getY() - source.getY();

        int width = label.getPreferredSize().width;
        int height = label.getPreferredSize().height;

        // Normalize the perpendicular vector
        double length = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        int padX = (int) (-yDiff * height / length);
        int padY = (int) (xDiff * height / length);


        // offset the label
        int x = (source.getX() + target.getX() + Vertex.diameter) / 2 + padX - width / 2;
        int y = (source.getY() + target.getY() + Vertex.diameter) / 2 + padY - height / 2;

        label.setBounds(x - width / 2, y - width / 2, width, height);
    }

    private void setEdgeBounds() {
        int x = Math.min(source.getX(), target.getX()) + Vertex.diameter;
        int y = Math.min(source.getY(), target.getY()) + Vertex.diameter;
        int width = Math.abs(source.getX() - target.getX());
        int height = Math.abs(source.getY() - target.getY());
        this.setBounds(x, y, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        Point sourcePoint = SwingUtilities.convertPoint(source, new Point(0, 0), this);
        Point targetPoint = SwingUtilities.convertPoint(target, new Point(0, 0), this);
        g.setColor(EDGE_COLOR);
        g.drawLine(sourcePoint.x + Vertex.diameter, sourcePoint.y + Vertex.diameter,
                targetPoint.x + Vertex.diameter, targetPoint.y + Vertex.diameter);
    }


    public static Edge prompt(JComponent c, Vertex source, Vertex target) {
        while (true) {
            String weight = JOptionPane.showInputDialog(c, "Enter edge weight:", "Weight", JOptionPane.PLAIN_MESSAGE);
            if (weight != null) {
                try {
                    return Edge.of(source, target, Integer.parseInt(weight));
                } catch (NumberFormatException e) {
                    // Commenting out because I want this behavior, but breaks HyperSkill Tests: Error dialog
//                JOptionPane.showMessageDialog(edge, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return null;
            }
        }
    }
}
