import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectionTournament<T> implements Selection<T> {
    private final ToDoubleFunction<T> fitness;

    private final int size;


    public SelectionTournament(int size, ToDoubleFunction<T> fitness) {
        this.size = size;
        this.fitness = fitness;
    }

    @Override
    public IntStream select(List<T> keep) {


        List<Double> weights = keep.stream().map(fitness::applyAsDouble).collect(Collectors.toList());


        return IntStream.generate(() -> 0).flatMap((ignore) -> Randoms.ints(0, keep.size())
                .limit(size).boxed().max(Comparator.comparingDouble(weights::get)).map(IntStream::of).orElseGet(IntStream::empty))
                ;


    }
}
