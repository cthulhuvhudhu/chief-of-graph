package chief.of.graph.containers;

import chief.of.graph.SwingUtil;
import chief.of.graph.models.Modes;
import chief.of.graph.models.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Arrays;

import static chief.of.graph.containers.MainFrame.modeLabel;
import chief.of.graph.models.Edge;

public class Graph extends JPanel {

    private static Vertex selectedVertex = null;
    private static Graph instance = null;
    public static final String GRAPH_NAMEFIX = "Graph";

    public static Graph getInstance() {
        if (instance != null) {
            return instance;
        }
        Graph graph = new Graph();
        graph.setLayout(null);
        graph.setName("Graph");
        graph.setBackground(SwingUtil.CREAM_COLOR);
        graph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modeLabel.getText().equals(Modes.ADD_VERTEX.getName()) && isValidLocation(e.getX(), e.getY())) {
                    Vertex newVertex = Vertex.prompt(graph, e.getX(), e.getY());
                    if (newVertex != null && Arrays.stream(instance.getComponents())
                            .noneMatch(c -> c.getName().equals(newVertex.getName()))) {
                        graph.add(newVertex);
                        graph.revalidate();
                        graph.repaint();
                    }
                } else if (modeLabel.getText().equals(Modes.ADD_EDGE.getName())) {
                    addEdge(e);
                } else if (modeLabel.getText().equals(Modes.REMOVE_VERTEX.getName())) {
                    removeVertex(instance.getComponentAt(e.getX(), e.getY()));
                } else if (modeLabel.getText().equals(Modes.REMOVE_EDGE.getName())) {
                    removeEdge(e.getX(), e.getY());
                }
            }
        });
        instance = graph;
        return graph;
    }

    public static void reset() {
        selectedVertex = null;
    }

    private static boolean isValidLocation(int x, int y) {
        Rectangle footprint = Vertex.getFootprint(x, y);
        return Arrays.stream(instance.getComponents())
//                .filter(c -> c.getName().contains(VERTEX_NAMEFIX)) // so we match edges too, for HS tests
                .noneMatch(c -> c.getBounds().intersects(footprint));
    }

    private static Vertex findVertex(int x, int y) {
        Component component = instance.getComponentAt(x, y);
        if (!(component instanceof Vertex)) {
            if (!(component instanceof JLabel) && component.getName().startsWith("VertexLabel")) {
                return null;
            } else {
                return (Vertex) component.getParent();
            }
        } else {
            return (Vertex) component;
        }
    }

    private static void addEdge(MouseEvent e) {
        Vertex vertex = findVertex(e.getX(), e.getY());
        if (vertex == null) {
            return;
        }

        vertex.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        if (selectedVertex == null) {
            selectedVertex = vertex;
            return;
        }
        Edge edge = Edge.prompt(instance, selectedVertex, vertex);
        if (edge != null) {
            instance.add(edge);
            instance.add(edge.getLabel());
            instance.add(Edge.of(edge.getTarget(), edge.getSource(), edge.getWeight()));

            // Reset selections
            selectedVertex.setBorder(null);
            vertex.setBorder(null);
            selectedVertex = null;
            instance.revalidate();
            instance.repaint();
        }
    }

    private static void removeVertex(Component component) {
        if (component instanceof Vertex) {
            Arrays.stream(instance.getComponents()).filter(c -> c instanceof Edge)
                    .map(c -> (Edge) c)
                    .filter(c -> c.getSource().equals(component) || c.getTarget().equals(component))
                    .forEach(edge -> {
                        instance.remove(edge.getLabel());
                        instance.remove(edge);
                    });
            instance.remove(component);
            instance.revalidate();
            instance.repaint();
        }
    }


    // TODO: FEAT: This would be better using the click two vertices method; but HS tests on edge click
    private static void removeEdge(int x, int y) {
        var component = Arrays.stream(instance.getComponents()).filter(c -> c instanceof Edge)
                .map(c -> (Edge) c)
                .filter(edge -> Line2D.ptSegDistSq(edge.getX(), edge.getY(), edge.getX() + edge.getWidth(), edge.getY() + edge.getHeight(), x, y) < 20)
                .findFirst();
        if (component.isPresent()) {
            Arrays.stream(instance.getComponents())
                    .filter(c -> c instanceof Edge)
                    .filter(edge -> ((Edge) edge).getTarget().equals((component.get()).getSource()))
                    .filter(edge -> ((Edge) edge).getSource().equals((component.get()).getTarget()))
                    .forEach(c -> {
                        instance.remove(((Edge) c).getLabel());
                        instance.remove(c);
                    });
            instance.remove(component.get());
            instance.remove(component.get().getLabel());
            instance.revalidate();
            instance.repaint();
        }
    }
}
