import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectionRoulette<T> implements Selection<T> {


    private final ToDoubleBiFunction<T, Integer> fitness;


    public SelectionRoulette(ToDoubleFunction<T> fitness) {
        this((v, i) -> fitness.applyAsDouble(v));
    }


    public SelectionRoulette(ToDoubleBiFunction<T, Integer> fitness) {
        this.fitness = fitness;
    }

    private static <T, V, K> Collector<T, TreeMap<K, V>, TreeMap<K, V>> toTreeMap(Function<T, K> getKey, Function<T, V> getValue, BinaryOperator<K> combineKey) {
        return Collector.of(TreeMap::new, (map, value) -> {
            Optional<K> lastKey = Optional.ofNullable(map.lastEntry()).map(Map.Entry::getKey);
            K key = getKey.apply(value);
            map.put(lastKey.map(acc -> combineKey.apply(acc, key)).orElse(key), getValue.apply(value));
        }, (TreeMap<K, V> a, TreeMap<K, V> b) -> {
            Optional<K> lastKey = Optional.ofNullable(a.lastEntry()).map(Map.Entry::getKey);
            a.putAll(b.entrySet().stream().collect(Collectors.toMap((entry) -> lastKey.map(acc -> combineKey.apply(acc, entry.getKey())).orElse(entry.getKey()), Map.Entry::getValue)));
            return a;
        });
    }

    @Override
    public IntStream select(List<T> keep) {


        TreeMap<Double, Integer> map = IntStream.range(0, keep.size()).boxed().collect(toTreeMap((i) -> fitness.applyAsDouble(keep.get(i), i), Function.identity(), Double::sum));


        Optional<Double> total = Optional.of(map.lastEntry()).map(Map.Entry::getKey).filter(p -> p > 0);


        return total.map(aDouble -> Randoms.doubles(0, aDouble)
                .map(map::ceilingKey).mapToInt(map::get)
        ).orElseGet(IntStream::empty);


    }


}
