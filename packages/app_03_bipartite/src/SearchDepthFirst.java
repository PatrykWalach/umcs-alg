import java.util.Deque;
import java.util.Optional;

public class SearchDepthFirst<T> extends Search<T> {

    public SearchDepthFirst(Graph<T> graph, Optional<T> first) {
        super(graph, first);
    }

    @Override
    public T getNext(Deque<T> queue) {
        return queue.poll();
    }
}
