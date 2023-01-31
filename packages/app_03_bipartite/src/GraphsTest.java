import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class GraphsTest {

    private static <T> Search<T> getSearch(String strategy, Graph<T> graph, Optional<T> first) {
        Search<T> search = new SearchBreadthFirst<>(graph, (first));
        if (Objects.equals(strategy, "DepthFirst")) {
            search = new SearchDepthFirst<>(graph, (first));
        }

        return search;
    }

    @ParameterizedTest
    @CsvSource({"DepthFirst", "BreadthFirst"})
    public void empty(String strategy) {
        GraphList<Object> graph = new GraphList<>();

        Search<Object> search = getSearch(strategy, graph, Optional.empty());
        Tuple<List<Object>> tuple = Graphs.bipartite(search).orElseThrow(RuntimeException::new);


        Assertions.assertArrayEquals(new Object[]{}, tuple.getLeft().toArray());
        Assertions.assertArrayEquals(new Object[]{}, tuple.getRight().toArray());

    }

    @ParameterizedTest
    @CsvSource({"DepthFirst", "BreadthFirst"})
    public void bipartite(String strategy) {
        Graph<Character> graph = new GraphBidirected<>(new GraphList<>());

        graph.addEdge('1', 'A');
        graph.addEdge('1', 'E');

        graph.addEdge('2', 'C');

        graph.addEdge('3', 'A');
        graph.addEdge('3', 'B');

        graph.addEdge('4', 'C');
        graph.addEdge('4', 'E');

        graph.addEdge('5', 'E');

        Search<Character> search = getSearch(strategy, graph, Optional.of('1'));
        Tuple<List<Character>> tuple = Graphs.bipartite(search).orElseThrow(RuntimeException::new);


        Assertions.assertArrayEquals(new Character[]{'1', '2', '3', '4', '5'}, tuple.getLeft().toArray());
        Assertions.assertArrayEquals(new Character[]{'A', 'B', 'C', 'E'}, tuple.getRight().toArray());

    }
}