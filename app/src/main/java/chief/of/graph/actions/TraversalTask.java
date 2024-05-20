package chief.of.graph.actions;

import chief.of.graph.containers.Graph;
import chief.of.graph.models.Edge;
import chief.of.graph.models.Vertex;
import chief.of.graph.states.AlgoModes;

import javax.swing.*;
import java.util.*;

public class TraversalTask extends SwingWorker<List<Edge>, Edge> {

    private final Vertex startVertex;
    private final List<Edge> path = new ArrayList<>();

    public TraversalTask(Vertex vertex) {
        this.startVertex = vertex;
    }

    @Override
    protected List<Edge> doInBackground() {
        boolean isDFS = AlgoModes.currentAlgorithm == AlgoModes.Algorithm.DFS;
        Collection<Edge> edges = Graph.getEdges();

        var toVisit = new ArrayDeque<Edge>();
        var visited = new HashSet<Vertex>();

        edges.stream()
                .filter(e -> e.getSource().equals(startVertex))
                .sorted(Edge.ascWeightComparator.reversed())
                .forEach(toVisit::offerFirst);

        while (!toVisit.isEmpty()) {
            var current = toVisit.removeFirst();

            if (visited.contains(current.getTarget())) {
                continue;
            }
            visited.add(current.getSource());
            visited.add(current.getTarget());
            path.add(current);

            edges.stream()
                    .filter(e -> e.getSource().equals(current.getTarget()))
                    .filter(e -> !visited.contains(e.getTarget()))
                    // DFS : Push weights onto stack, from largest to smallest
                    // BFS : Add weights to the queue, from smallest to largest
                    .sorted(isDFS ? Edge.ascWeightComparator.reversed() : Edge.ascWeightComparator)
                    .forEach( isDFS ? toVisit::offerFirst : toVisit::offerLast);
        }
        return path;
    }
}
