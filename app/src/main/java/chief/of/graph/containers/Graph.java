package chief.of.graph.containers;

import chief.of.graph.SwingUtil;
import chief.of.graph.actions.AlgorithmRunner;
import chief.of.graph.models.Model;
import chief.of.graph.models.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.Collection;

import chief.of.graph.models.Edge;
import chief.of.graph.states.AlgoModes;
import chief.of.graph.states.EditModes;

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
        graph.setName(GRAPH_NAMEFIX);
        graph.setBackground(SwingUtil.CREAM_COLOR);
        graph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (EditModes.LABEL.getText().equals(EditModes.ADD_VERTEX.getCopy()) && isValidLocation(e.getX(), e.getY())) {
                    Vertex newVertex = Vertex.prompt(graph, e.getX(), e.getY());
                    if (newVertex != null && Arrays.stream(instance.getComponents())
                            .noneMatch(c -> c.getName().equals(newVertex.getName()))) {
                        graph.add(newVertex);
                        graph.revalidate();
                        graph.repaint();
                    }
                } else if (EditModes.LABEL.getText().equals(EditModes.ADD_EDGE.getCopy())) {
                    addEdge(e);
                } else if (EditModes.LABEL.getText().equals(EditModes.REMOVE_VERTEX.getCopy())) {
                    removeVertex(instance.getComponentAt(e.getX(), e.getY()));
                } else if (EditModes.LABEL.getText().equals(EditModes.REMOVE_EDGE.getCopy())) {
                    removeEdge(e.getX(), e.getY());
                } else if (EditModes.LABEL.getText().equals(EditModes.NONE.getCopy()) && AlgoModes.currentAlgorithm != AlgoModes.Algorithm.NOT_SELECTED) {
                    AlgorithmRunner.of(findVertex(e.getX(), e.getY())).run();
                }
            }
        });
        instance = graph;
        return graph;
    }

    public static Collection<Vertex> getVertices() {
        return Arrays.stream(instance.getComponents())
                .filter(c -> c instanceof Vertex)
                .map(c -> (Vertex) c)
                .toList();
    }

    public static Collection<Edge> getEdges() {
        return Arrays.stream(instance.getComponents())
                .filter(c -> c instanceof Edge)
                .map(c -> (Edge) c)
                .toList();
    }

    public static Edge findEdge(Vertex source, Vertex target) {
        return getEdges().stream()
                .filter(e -> e.getSource().equals(source))
                .filter(e -> e.getTarget().equals(target))
                .findFirst().orElse(null);
    }

    public static void refresh() {
        instance.revalidate();
        instance.repaint();
    }

    public static void reset() {
        selectedVertex = null;
        Arrays.stream(instance.getComponents())
                .filter(c -> c instanceof Model)
                .map(c -> (Model) c)
                .forEach(Model::unvisit);
    }

    private static boolean isValidLocation(int x, int y) {
        Rectangle footprint = Vertex.getFootprint(x, y);
        return Arrays.stream(instance.getComponents())
//                .filter(c -> c.getName().contains(VERTEX_NAMEFIX)) // so we match edges too, for HS tests
                .noneMatch(c -> c.getBounds().intersects(footprint));
    }

    private static Vertex findVertex(int x, int y) {
        Component component = instance.getComponentAt(x, y);
        if (component != null && !(component instanceof Vertex)) {
            if (!(component instanceof JLabel) && component.getName().startsWith(Vertex.VERTEX_LABEL_NAMEFIX)) {
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
            Edge ghostEdge = Edge.of(edge.getTarget(), edge.getSource(), edge.getWeight());
            ghostEdge.setVisible(false);
            instance.add(ghostEdge);

            // Reset selections
            selectedVertex.setBorder(null);
            vertex.setBorder(null);
            selectedVertex = null;
            refresh();
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
