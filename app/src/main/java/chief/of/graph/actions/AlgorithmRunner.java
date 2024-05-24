package chief.of.graph.actions;

import chief.of.graph.models.Vertex;
import chief.of.graph.states.AlgoModes;

import java.util.concurrent.ExecutionException;

public class AlgorithmRunner {
    private final Vertex vertex;

    private AlgorithmRunner(Vertex vertex) {
        this.vertex = vertex;
    }

    public static AlgorithmRunner of(Vertex vertex) {
        return new AlgorithmRunner(vertex);
    }

    public void run() {
        AlgoModes.updateDisplayLabel(AlgoModes.RUNNING.copy());
        try {
            switch(AlgoModes.currentAlgorithm) {
                case DFS:
                case BFS:
                    var traverse = new TraversalTask(vertex);
                    traverse.execute();
                    (new TraversalPainter()).paint(traverse.get());
                    break;
                case DIJKSTRA:
                    var dijkstra = new DijkstraTask(vertex);
                    dijkstra.execute();
                    (new DijkstraPainter()).paint(dijkstra.get());
                    break;
                case PRIM:
                    var prim = new PrimTask(vertex);
                    prim.execute();
                    (new PrimPainter()).paint(prim.get());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + AlgoModes.currentAlgorithm);
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
}
