package chief.of.graph.actions;

import chief.of.graph.containers.Graph;
import chief.of.graph.models.Edge;
import chief.of.graph.models.Model;
import chief.of.graph.states.AlgoModes;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import chief.of.graph.SwingUtil;

public class TraversalPainter implements ActionListener {
    private Timer timer;
    private final Queue<Model> traverseModels = new ArrayDeque<>();
    private final List<Edge> path = new ArrayList<>();

    protected void paint(List<Edge> traverseEdges) {
        if (traverseEdges.isEmpty()) {
            return;
        }

        path.addAll(traverseEdges);

        traverseModels.add(traverseEdges.getFirst().getSource());
        for (Edge edge : traverseEdges) {
            traverseModels.add(edge);
            traverseModels.add(Graph.findEdge(edge.getTarget(), edge.getSource()));
            traverseModels.add(edge.getTarget());
        }

        timer = new Timer(SwingUtil.DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (traverseModels.isEmpty()) {
            timer.stop();
            AlgoModes.updateDisplayLabel(toPrint(path));
            return;
        }
        traverseModels.remove().visit();
        Graph.refresh();
    }

    private String toPrint(Collection<Edge> edges) {
        StringBuilder sb = new StringBuilder();
        for (var edge : edges) {
            if (sb.isEmpty()) {
                sb.append(AlgoModes.currentAlgorithm.name())
                        .append(" : ")
                        .append(edge.getSource().getId());
            }
            sb.append(" -> ").append(edge.getTarget().getId());
        }
        return sb.toString();
    }
}
