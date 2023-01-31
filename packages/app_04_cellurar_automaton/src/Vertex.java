import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Vertex<T> {


    private final List<T> values;

    public Vertex(List<T> values) {
        this.values = values;
    }

    public static Vertex<Integer> fromIndex(int flatIndex, List<Integer> shape) {
        ArrayList<Integer> vertex = new ArrayList<>(shape.size());

        for (Integer integer : shape) {
            vertex.add(flatIndex % integer);
            flatIndex /= integer;
        }

        Collections.reverse(vertex);

        return new Vertex<>(vertex);
    }

    public static Integer toIndex(List<Integer> vertex, List<Integer> shape) {
        return IntStream.range(0, Math.min(shape.size(), vertex.size())).reduce(0, (acc, i) -> acc * shape.get(i) + vertex.get(i));
    }

    public List<T> toList() {
        return values;
    }

    public <R, U> Stream<R> zipMap(Vertex<U> vertex, BiFunction<T, U, R> fn) {
        return IntStream.range(0, Math.min(size(), vertex.size())).boxed().map((i) -> fn.apply(values.get(i), vertex.get(i)));
    }

    public T get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }


    public Object[] toArray() {
        return values.toArray();
    }

    public Stream<T> stream() {
        return values.stream();
    }
}
