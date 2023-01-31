import java.util.List;

public class GraphBidirected<T> implements Graph<T> {
    private final Graph<T> graph;

    public GraphBidirected(Graph<T> graph) {
        this.graph = graph;
    }

    @Override
    public void addEdge(T a, T b) {
        graph.addEdge(a, b);
        graph.addEdge(b, a);
    }

    @Override
    public List<T> getAdjacent(T node) {
        return graph.getAdjacent(node);
    }
}
