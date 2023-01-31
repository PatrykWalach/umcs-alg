import java.util.Deque;
import java.util.Optional;

public class SearchBreadthFirst<T> extends Search<T> {

    public SearchBreadthFirst(Graph<T> graph, Optional<T> first) {
        super(graph, first);
    }

    @Override
    public T getNext(Deque<T> queue) {
        return queue.pollLast();
    }
}
