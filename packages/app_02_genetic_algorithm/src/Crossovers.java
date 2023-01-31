import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Crossovers {


    private static <T> List<List<T>> ints(int point, List<T> a, List<T> b) {


        List<T> c = new ArrayList<>(a.subList(0, point));
        List<T> d = new ArrayList<>(b.subList(0, point));

        c.addAll(b.subList(point, b.size()));
        d.addAll(a.subList(point, a.size()));


        return Stream.of(c, d).collect(Collectors.toList());

    }

    public static <T> Crossover<List<T>> newTwoPoint() {
        return (a, b) -> {

            int first = Randoms.nextInt(0, a.size() - 2);


            List<List<T>> result = Crossovers.ints(first, a, b);

            int second = Randoms.nextInt(Math.max(first - a.size() / 2, 0), Math.min(first + a.size() / 2, a.size() - 2));

            return Crossovers.ints(second, result.get(0), result.get(1));
        };
    }

    public static <T> Crossover<List<T>> newOnePoint() {
        return (a, b) -> Crossovers.ints(Randoms.index(a), a, b);
    }


}
