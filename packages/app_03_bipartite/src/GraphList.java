import java.util.*;

public class GraphList<T> implements Graph<T> {

    private final Map<T, Set<T>> edges = new HashMap<>();

    @Override
    public void addEdge(T from, T to) {
        Set<T> adjacent = edges.getOrDefault(from, new HashSet<>());
        adjacent.add(to);
        edges.put(from, adjacent);
    }

    @Override
    public List<T> getAdjacent(T node) {
        return new ArrayList<>(edges.getOrDefault(node, new HashSet<>()));
    }
}
