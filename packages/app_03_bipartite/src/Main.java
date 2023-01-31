import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static String PATH = "input2.txt";

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(new File(PATH));

        int nodesCount = in.nextInt();
        int edgesCount = in.nextInt();

        StringBuilder data = new StringBuilder();

        while (in.hasNextLine()) {
            data.append(in.nextLine());
        }

        String[] edges = data.toString().trim().split(",");

        if (edges.length != edgesCount) {
            System.out.println("Zła ilość krawędzi w \"" + PATH + "\" oczekiwano: " + edges.length);
        }

        List<Integer> parsed = new ArrayList<>();

        for (String edge : edges) {
            Matcher matcher = Pattern.compile("^\\s*(\\S+)\\s+(\\S+)\\s*$").matcher(edge);
            if (!matcher.find()) {
                throw new RuntimeException("Expected edge to be in format \"1 \", got: \"" + edge + "\" instead");
            }


            Stream.of(1, 2).map(matcher::group).mapToInt(Integer::parseInt).forEach(parsed::add);
        }


        Graph<Integer> graph = (new GraphList<>());


        IntStream.range(0, parsed.size() / 2).forEach((edge) -> {
            graph.addEdge(parsed.get(edge * 2), parsed.get(edge * 2 + 1));
        });

        IntStream.range(1, nodesCount + 1).forEach(node -> {
            System.out.println("wierzcholek " + (node) + " sasiedzi " + graph.getAdjacent(node).stream().sorted(Comparator.reverseOrder()).map(Object::toString).collect(Collectors.joining("")));
        });


        Tuple<List<Integer>> tuple = Graphs.bipartite(new SearchDepthFirst<>(graph, Optional.of(1))).orElseThrow(RuntimeException::new);


        System.out.println(Stream.concat(
                tuple.getLeft().stream().map((c) -> "wierzcholek " + c + " ma kolor czerwony"),
                tuple.getRight().stream().map((c) -> "wierzcholek " + c + " ma kolor niebieski")
        ).sorted().collect(Collectors.joining("\n")));


        tuple = Graphs.bipartite(new SearchBreadthFirst<>(graph, Optional.of(1))).orElseThrow(RuntimeException::new);

        System.out.println(Stream.concat(
                tuple.getLeft().stream().map((c) -> "wierzcholek " + c + " ma kolor czerwony"),
                tuple.getRight().stream().map((c) -> "wierzcholek " + c + " ma kolor niebieski")
        ).sorted().collect(Collectors.joining("\n")));
    }
}