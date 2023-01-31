import java.util.function.BiFunction;

public interface Warunek<T> extends BiFunction<T, Vertex<Integer>, T> {
}
