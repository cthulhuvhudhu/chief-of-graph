package chief.of.graph.actions;

import chief.of.graph.containers.Graph;
import chief.of.graph.models.Edge;
import chief.of.graph.models.Vertex;

import javax.swing.*;
import java.util.*;

public class DijkstraTask extends SwingWorker<Map<Vertex, Integer>, Map.Entry<Vertex, Integer>> {

    private final Vertex startVertex;

    public DijkstraTask(Vertex vertex) {
        this.startVertex = vertex;
    }

    @Override
    protected Map<Vertex, Integer> doInBackground() {
        Collection<Edge> edges = Graph.getEdges();
        var djikstraMap = new HashMap<Vertex, Integer>();

        ArrayDeque<Map.Entry<Edge, Integer>> toVisit = new ArrayDeque<>();

        edges.stream()
                .filter(e -> e.getSource().equals(startVertex))
                .sorted(Edge.ascWeightComparator.reversed())
                .map(e -> Map.entry(e, e.getWeight()))
                .forEach(toVisit::offerFirst);

        while (!toVisit.isEmpty()) {
            var current = toVisit.removeFirst();
            var target = current.getKey().getTarget();
            if (target == startVertex) continue;

            djikstraMap.compute(target, (key, existingWeight) -> {
                if (existingWeight == null || current.getValue() < existingWeight) {
                    return current.getValue();
                } else {
                    return existingWeight;
                }
            });

            edges.stream()
                    .filter(e -> e.getSource().equals(target))
                    .map(e -> Map.entry(e, e.getWeight() + current.getValue()))
                    .filter(e -> e.getValue() < djikstraMap.getOrDefault(e.getKey().getTarget(), Integer.MAX_VALUE))
                    .forEach(toVisit::offerLast);
        }
        return djikstraMap;
    }
}
