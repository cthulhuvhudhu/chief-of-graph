package chief.of.graph.actions;

import java.util.*;

import chief.of.graph.containers.Graph;
import chief.of.graph.models.Edge;
import chief.of.graph.models.Vertex;

import javax.swing.*;

public class PrimTask extends SwingWorker<List<Edge>, Edge> {

    private final Vertex startVertex;
    private final List<Edge> path = new ArrayList<>();

    public PrimTask(Vertex vertex) {
        this.startVertex = vertex;
    }

    @Override
    protected List<Edge> doInBackground() {
        Collection<Edge> myEdges = Graph.getEdges();
        Collection<Vertex> vertices = Graph.getVertices();
        var visited = new HashSet<Vertex>();
        visited.add(startVertex);
        var newChild = startVertex;
        var toVisit = new PriorityQueue<>(Edge.ascWeightComparator);
        toVisit.addAll(myEdges.stream()
                .filter(e -> e.getTarget().equals(newChild))
                .toList());

        while (!toVisit.isEmpty() && !visited.containsAll(vertices)) {
            var current = toVisit.poll();
            if (visited.contains(current.getSource())) {
                continue;
            }
            visited.add(current.getSource());
            path.add(current);
            toVisit.addAll(myEdges.stream()
                    .filter(e -> e.getTarget().equals(current.getSource()))
                    .filter(e -> !visited.contains(e.getSource()))
                    .toList());
        }
        return path;
    }
}
