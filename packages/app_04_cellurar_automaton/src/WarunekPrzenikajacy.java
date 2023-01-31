import java.util.List;
import java.util.stream.Collectors;

public class WarunekPrzenikajacy<T> implements Warunek<T> {
    private final CellularAutomaton<T> automaton;

    public WarunekPrzenikajacy(CellularAutomaton<T> automaton) {
        this.automaton = automaton;
    }

    @Override
    public T apply(T t, Vertex<Integer> position) {

        List<Integer> vertex = position.zipMap(new Vertex<>(automaton.getShape()), (i, size) -> {
            if (i == size - 1) {
                return 1;
            }

            if (i == 0) {
                return size - 2;
            }
            return i;
        }).collect(Collectors.toList());


        return automaton.toList().get(Vertex.toIndex(vertex, automaton.getShape()));
    }
}
