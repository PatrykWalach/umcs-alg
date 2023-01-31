import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static int ITERATIONS = 50;

    public static void main(String[] args) {
        ToDoubleFunction<List<Integer>> fitness = (solution) ->
                IntStream.range(0, solution.size() / 2).map((i) -> i * 2).map(solution::get).map((s) -> (s - 9)).sum() +
                        IntStream.range(0, solution.size() / 2).map((i) -> i * 2 + 1).map(solution::get).map(s -> (100 - s)).sum();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj wielkość populacji:");
        int population = Integer.parseInt(scanner.nextLine());

        HashMap<String, Selection<List<Integer>>> selections = new HashMap<>();
        selections.put("ranking", new SelectionRank<>(fitness));
        selections.put("turniej", new SelectionTournament<>(population / 10, fitness));
        selections.put("ruletka", new SelectionRoulette<>(fitness));

        Selection<List<Integer>> selection = null;

        while (selection == null) {

            System.out.println("Podaj typ selekcji [" + String.join("|", selections.keySet()) + "]:");
            selection = selections.get(scanner.nextLine());
        }

        HashMap<String, Crossover<List<Integer>>> crossovers = new HashMap<>();
        crossovers.put("jeden", Crossovers.newOnePoint());
        crossovers.put("dwa", Crossovers.newTwoPoint());
        Crossover<List<Integer>> crossover = null;

        while (crossover == null) {
            System.out.println("Podaj typ krosowania [" + String.join("|", crossovers.keySet()) + "]:");
            crossover = crossovers.get(scanner.nextLine());
        }

        Genetic<List<Integer>> genetic = new Genetic<>(population);

        genetic.seed(() -> Randoms.ints(10, 100).limit(50).boxed().collect(Collectors.toList()));


        for (int i = 0; i < ITERATIONS; i++) {
            genetic.cross(selection, crossover);

            List<Integer> bestSolution = genetic.getSolutions().stream().max(Comparator.comparingDouble(fitness)).orElseThrow(RuntimeException::new);

            System.out.println("" + (i + 1) + "/" + ITERATIONS + ": " + bestSolution);
        }


    }
}