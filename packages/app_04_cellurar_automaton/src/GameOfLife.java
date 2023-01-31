import java.util.Set;
import java.util.function.BiFunction;

public class GameOfLife implements BiFunction<Main.State, Vertex<Integer>, Main.State> {
    private final MooreNeighborhood<Main.State> gameOfLife;

    public GameOfLife(CellularAutomaton<Main.State> automaton, Set<Integer> B, Set<Integer> S) {

        this.gameOfLife = new MooreNeighborhood<>(automaton, (v, neighbors) -> {
            Long sum = neighbors.stream().filter((a) -> a.equals(Main.State.ALIVE)).count();

            if (v.equals(Main.State.DEAD) && B.contains(sum.intValue())) {
                return Main.State.ALIVE;
            }

            if (v.equals(Main.State.DEAD) || S.contains(sum.intValue())) {
                return v;
            }

            return Main.State.DEAD;
        });
    }

    @Override
    public Main.State apply(Main.State state, Vertex<Integer> integerVertex) {
        return gameOfLife.apply(state, integerVertex);
    }


}
