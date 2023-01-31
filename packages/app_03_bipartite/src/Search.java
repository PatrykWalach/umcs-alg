import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class Search<T> implements Iterable<T> {


    private final Graph<T> graph;
    private final Optional<T> first;

    public Search(Graph<T> graph, Optional<T> first) {
        this.graph = graph;
        this.first = first;
    }

    public Graph<T> getGraph() {
        return graph;
    }

    public Optional<T> getFirst() {
        return first;
    }

    public abstract T getNext(Deque<T> queue);


    @Override
    public Iterator<T> iterator() {
        return new SearchIterator<>(this);
    }

    private static class SearchIterator<T> implements Iterator<T> {
        private final Search<T> search;
        private final Deque<T> queue = new ArrayDeque();
        private final Set<T> visited = new LinkedHashSet<>();

        public SearchIterator(Search<T> search) {
            this.search = search;


            search.getFirst().ifPresent(queue::add);


        }

        @Override
        public boolean hasNext() {
            String start = visited.stream().map(Object::toString).collect(Collectors.joining(""));
            if (start.length() > 0) {
                System.out.print(start);
                System.out.println(" na stosie " + queue.stream().map(Object::toString).collect(Collectors.joining("")));
            }
            return queue.size() > 0;
        }


        @Override
        public T next() {

            T head = search.getNext(queue);
            visited.add(head);

            Predicate<T> isVisited = visited::contains;
            Predicate<T> isQueued = queue::contains;


            List<T> adjacent = search.getGraph().getAdjacent(head).stream().filter(isVisited.negate()).filter(isQueued.negate()).collect(Collectors.toList());


            Collections.reverse(adjacent);


            adjacent.forEach(queue::addFirst);


            return head;
        }
    }


}
