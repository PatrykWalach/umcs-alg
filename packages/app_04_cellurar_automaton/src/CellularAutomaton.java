import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CellularAutomaton<T> {
    private final List<Integer> shape;
    private List<T> values;

    private Warunek<T> warunek = (v, i) -> v;
    private BiFunction<T, Vertex<Integer>, T> game = (v, i) -> v;

    public CellularAutomaton(List<Integer> shape) {
        this.shape = shape;
    }

    public void setWarunek(Warunek<T> warunek) {
        this.warunek = warunek;
        mapEdges();
    }

    public List<Integer> getShape() {
        return shape;
    }

    public void fill(T initialValue) {
        values = Collections.synchronizedList((Collections.nCopies(shape.stream().reduce(1, (a, b) -> a * b), initialValue)));
        mapEdges();
    }

    private void mapEdges() {
        values = Collections.synchronizedList(IntStream.range(0, values.size()).sequential().boxed().map((i) -> {
            Vertex<Integer> pos = Vertex.fromIndex(i, shape);

            if (pos.stream().anyMatch((j) -> j == 0 || shape.contains(j + 1))) {
                return warunek.apply(values.get(i), pos);
            }

            return (values.get(i));
        }).collect(Collectors.toList()));

    }

    public void setGame(BiFunction<T, Vertex<Integer>, T> game) {
        this.game = game;
    }

    public void replaceAll() {


        values = Collections.synchronizedList(IntStream.range(0, values.size()).sequential().boxed().map((i) -> {
            Vertex<Integer> pos = Vertex.fromIndex(i, shape);

            if (pos.stream().anyMatch((j) -> j == 0 || shape.contains(j + 1))) {
                return (values.get(i));
            }

            return game.apply(values.get(i), pos);
        }).collect(Collectors.toList()));


        mapEdges();

    }


    public List<T> toList() {
        return Collections.unmodifiableList(values);
    }


    public void setValues(T[] values) {
        this.values = Collections.synchronizedList(Arrays.asList(values));
        mapEdges();
    }

    public void set(List<Integer> pos, T value) {
        Integer i = Vertex.toIndex(pos, shape);
        this.values.set(i, value);
        mapEdges();
    }

    public void set(int i, T value) {
        this.values.set(i, value);
        mapEdges();
    }
}
