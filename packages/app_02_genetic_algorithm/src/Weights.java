import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Weights {


    public static int ITERATIONS = 50;

    public static void main(String[] args) {
//      x1*4 + x2*-2 + x3*3.5 + x4*5 + x5*-11 + x6*-4.7 = 44

        List<Double> weights = Arrays.asList(4.0, -2.0, 3.5, 5.0, -11.0, -4.7);

        ToDoubleFunction<List<Double>> f = (solution) -> IntStream.range(0, solution.size()).mapToDouble(
                i -> solution.get(i) * weights.get(i)
        ).sum();

        ToDoubleFunction<List<Double>> fitness = (solution) ->
                1 / Math.abs(44 - f.applyAsDouble(solution));


        int population = 10_000;

        HashMap<String, Selection<List<Double>>> selections = new HashMap<>();
        selections.put("ranking", new SelectionRank<>(fitness));
        selections.put("turniej", new SelectionTournament<>(population / 10, fitness));
        selections.put("ruletka", new SelectionRoulette<>(fitness));

        Selection<List<Double>> selection = selections.get("ranking");


        HashMap<String, Crossover<List<Double>>> crossovers = new HashMap<>();
        crossovers.put("jeden", Crossovers.newOnePoint());
        crossovers.put("dwa", Crossovers.newTwoPoint());
        Crossover<List<Double>> crossover = crossovers.get("jeden");


        Genetic<List<Double>> genetic = new Genetic<>(population);

        genetic.seed(() -> Randoms.doubles(-10, 10).limit(weights.size()).boxed().collect(Collectors.toList()));


        for (int i = 0; i < ITERATIONS; i++) {
            genetic.cross(selection, crossover);

            List<Double> bestSolution = genetic.getSolutions().stream().max(Comparator.comparingDouble(fitness)).orElseThrow(RuntimeException::new);


            System.out.println("" + (i + 1) + "/" + ITERATIONS + ": " + bestSolution);

            System.out.println(IntStream.range(0, bestSolution.size()).mapToObj(
                    k -> "" + String.format("%.2f", bestSolution.get(k)) + "*" + String.format("%.2f", weights.get(k))
            ).collect(Collectors.joining(" + ")) + " = " + (f.applyAsDouble(bestSolution)));


        }


    }
}
