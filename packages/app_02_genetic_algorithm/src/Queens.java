import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Queens {


    public static int ITERATIONS = 50;

    public static void main(String[] args) {
        ToDoubleFunction<List<Integer>> fitness = (solution) ->
                57 - IntStream.range(0, solution.size()).map((i) -> new Long(IntStream.range(i, solution.size()).filter((j) -> {
                    int diff = solution.get(i) - solution.get(j);
                    return diff == 0 || Math.abs(i - j) == Math.abs(diff);
                }).count()).intValue()).sum();


        int population = 10_000;

        HashMap<String, Selection<List<Integer>>> selections = new HashMap<>();
        selections.put("ranking", new SelectionRank<>(fitness));
        selections.put("turniej", new SelectionTournament<>(population / 10, fitness));
        selections.put("ruletka", new SelectionRoulette<>(fitness));

        Selection<List<Integer>> selection = selections.get("ranking");


        HashMap<String, Crossover<List<Integer>>> crossovers = new HashMap<>();
        crossovers.put("jeden", Crossovers.newOnePoint());
        crossovers.put("dwa", Crossovers.newTwoPoint());
        Crossover<List<Integer>> crossover = crossovers.get("jeden");


        Genetic<List<Integer>> genetic = new Genetic<>(population);

        genetic.seed(() -> Randoms.ints(0, 8).limit(8).boxed().collect(Collectors.toList()));


        for (int i = 0; i < ITERATIONS; i++) {
            genetic.cross(selection, crossover);

            List<Integer> bestSolution = genetic.getSolutions().stream().max(Comparator.comparingDouble(fitness)).orElseThrow(RuntimeException::new);


            System.out.println("" + (i + 1) + "/" + ITERATIONS + ": ");

            bestSolution.stream()
                    .map((k) -> IntStream.range(0, k).mapToObj((j) -> "| ")
                            .collect(Collectors.joining("")) + "|X|" + IntStream.range(0, 7 - k).mapToObj((j) -> " |")
                            .collect(Collectors.joining(""))).forEach(System.out::println);


        }


    }
}
