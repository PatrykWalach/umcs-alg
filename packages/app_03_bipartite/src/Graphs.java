import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Graphs {

    public static <N> Optional<Tuple<List<N>>> bipartite(Search<N> search) {
        Map<N, Boolean> colors = new HashMap<>();


//        if(!search.iterator().hasNext()){
//            return Optional.of(new Tuple<>(new ArrayList<>(),new ArrayList<>()));
//        }
//
//        N first = search.iterator().next();
//        colors.put(first, true);
//        graph.getAdjacent(first).forEach(c->colors.put(c,false));

        int i = 0;

        for (N node : search) {

            System.out.println("\nkrok :" + ++i);

            Boolean color = colors.getOrDefault(node, true);


            Predicate<N> hasColor = colors::containsKey;

            search.getGraph().getAdjacent(node).stream().filter(hasColor.negate()).forEach((c) -> {
                colors.put(c, !color);
                System.out.println("Ustawiam kolor wierzcholka " + c + " na kolor " + (!color ? "czerwony" : "niebieski"));
            });

            for (N adjacent : search.getGraph().getAdjacent(node)) {
                if (colors.get(adjacent) != color) {
                    continue;
                }

                System.out.println("brak dwudzielności dla " + adjacent + " jego color to " + (color ? "czerwony" : "niebieski"));
//                return Optional.empty();
            }

        }

        Predicate<Map.Entry<N, Boolean>> getValue = Map.Entry::getValue;


        return Optional.of(new Tuple<>(
                colors.entrySet().stream().filter(getValue).map(Map.Entry::getKey).collect(Collectors.toList()),
                colors.entrySet().stream().filter(getValue.negate()).map(Map.Entry::getKey).collect(Collectors.toList())
        ));
    }
}
