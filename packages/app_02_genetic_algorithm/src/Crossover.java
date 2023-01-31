import java.util.List;

public interface Crossover<T> {
    List<T> points(T a, T b);
}
