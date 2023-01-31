import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genetic<T> {
    private final int population;
    private List<T> solutions;


    public Genetic(int population) {
        this.population = population;
    }

    public List<T> getSolutions() {
        return solutions;
    }

    public void cross(Selection<T> selection, Crossover<T> crossover) {
        int rolls = new Double(Math.pow(2, new Double(Math.log(solutions.size()) / Math.log(2)).intValue())).intValue();

//        int rolls = solutions.size() / 10;

        List<Integer> selected = selection.select(solutions).limit(rolls).distinct().boxed().collect(Collectors.toList());
        List<T> parents = solutions;


//        Collections.shuffle(selected);
//
//        if (selected.size() % 2 == 1) {
//            solutions.add(selected.get(selected.size() - 1));
//        }
//
//        for (int i = 0; i < selected.size() / 2; i++) {
//            solutions.addAll(crossover.points(selected.get(i * 2), selected.get(i * 2 + 1)));
//        }

        solutions = new ArrayList<>();


        while (solutions.size() < population) {
            solutions.addAll(crossover.points(
                    parents.get(selected.get(Randoms.index(selected))),
                    parents.get(selected.get(Randoms.index(selected)))
            ));
        }


    }

    public void seed(Supplier<T> supplier) {
        solutions = IntStream.range(0, population).mapToObj((i) -> supplier.get()).collect(Collectors.toList());
    }
}
