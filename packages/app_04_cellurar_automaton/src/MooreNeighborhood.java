import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MooreNeighborhood<T> implements BiFunction<T, Vertex<Integer>, T> {


    private final CellularAutomaton<T> automaton;
    private final BiFunction<T, List<T>, T> fn;

    public MooreNeighborhood(CellularAutomaton<T> automaton, BiFunction<T, List<T>, T> fn) {
        this.automaton = automaton;
        this.fn = fn;
    }

    @Override
    public T apply(T t, Vertex<Integer> integerVertex) {


        return fn.apply(t, getNeighbors(integerVertex));
    }


    private List<T> getNeighbors(Vertex<Integer> node) {
        int i = Vertex.toIndex(node.toList(), automaton.getShape());
        int size = automaton.getShape().get(0);

        return Stream.of(
                i - size - 1, i - size, i - size + 1,
                i - 1, i + 1,
                i + size - 1, i + size, i + size + 1).map(automaton.toList()::get).collect(Collectors.toList());

//        return IntStream.range(0, 1 + 1 << automaton.getShape().size()).boxed().map((j) ->
//                Vertex.toIndex(Vertex.fromIndex(j, automaton.getShape()).zipMap(node, Integer::sum).toList(), automaton.getShape())
//        ).map(automaton.toList()::get).collect(Collectors.toList());


    }
}