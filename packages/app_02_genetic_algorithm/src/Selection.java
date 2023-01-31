import java.util.List;
import java.util.stream.IntStream;

public interface Selection<T> {

    IntStream select(List<T> solutions);
}
