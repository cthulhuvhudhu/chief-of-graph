package chief.of.graph.actions;

import chief.of.graph.containers.Graph;
import chief.of.graph.models.Edge;
import chief.of.graph.models.Model;
import chief.of.graph.states.AlgoModes;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

import chief.of.graph.SwingUtil;

public class PrimPainter implements ActionListener {
    private Timer timer;
    private final Queue<Model> traverseModels = new ArrayDeque<>();
    private final List<Edge> path = new ArrayList<>();

    protected void paint(List<Edge> traverseEdges) {
        if (traverseEdges.isEmpty()) {
            return;
        }

        path.addAll(traverseEdges);

        traverseModels.add(traverseEdges.get(0).getTarget());
        for (Edge myEdge : traverseEdges) {
            traverseModels.add(myEdge);
            traverseModels.add(Graph.findEdge(myEdge.getTarget(), myEdge.getSource()));
            traverseModels.add(myEdge.getSource());
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

    private String toPrint(Collection<Edge> myEdges) {
        return myEdges.stream()
                .map(e -> e.getSource().getId() + "=" + e.getTarget().getId())
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
