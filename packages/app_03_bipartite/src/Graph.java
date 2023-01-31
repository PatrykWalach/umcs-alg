import java.util.List;

public interface Graph<N> {
    void addEdge(N from, N to);

    List<N> getAdjacent(N node);
}

