import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class WarunekOdbijajacy<T> implements Warunek<T> {
    private final CellularAutomaton<T> automaton;

    private final UnaryOperator<T> reverse;

    public WarunekOdbijajacy(CellularAutomaton<T> automaton, UnaryOperator<T> reverse) {
        this.automaton = automaton;
        this.reverse = reverse;
    }

    @Override
    public T apply(T t, Vertex<Integer> position) {

        List<Integer> vertex = position.zipMap(new Vertex<>(automaton.getShape()), (i, size) -> {
            if (i == size - 1) {
                return i - 1;
            }

            if (i == 0) {
                return i + 1;
            }

            return i;
        }).collect(Collectors.toList());


        return reverse.apply(automaton.toList().get(Vertex.toIndex(vertex, automaton.getShape())));
    }
}
