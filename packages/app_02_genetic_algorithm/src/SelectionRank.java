import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectionRank<T> implements Selection<T> {
    private final ToDoubleFunction<T> fitness;


    public SelectionRank(ToDoubleFunction<T> fitness) {
        this.fitness = fitness;
    }


    @Override
    public IntStream select(List<T> keep) {
        List<Integer> indexes = IntStream.range(0, keep.size()).boxed().sorted(Comparator.comparingDouble((i) -> fitness.applyAsDouble(keep.get(i)))).collect(Collectors.toList());
        Map<Integer, Integer> weights = IntStream.range(0, indexes.size()).boxed().collect(Collectors.toMap(indexes::get, Function.identity()));

        return new SelectionRoulette<T>((ignore, i) -> weights.get(i) + 1).select(keep);
    }
}
