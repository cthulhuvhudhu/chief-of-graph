package chief.of.graph.actions;

import chief.of.graph.models.Vertex;
import chief.of.graph.states.AlgoModes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.stream.Collectors;

import static chief.of.graph.SwingUtil.DELAY;

public class DijkstraPainter implements ActionListener {
    private Timer timer;
    private Map<Vertex, Integer> pathMap = Map.of();

    protected void paint(Map<Vertex, Integer> pathMap) {
        this.pathMap = pathMap;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();

        String toPrint = pathMap.entrySet().stream()
                .map(m -> m.getKey().getId() + "=" + m.getValue())
                .sorted()
                .collect(Collectors.joining(", "));

        AlgoModes.updateDisplayLabel(toPrint);
    }
}
