import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GameOfLifeTest {

    @Test
    void apply() {
//        given
        Set<Integer> S = Stream.of(2, 3).collect(Collectors.toSet());
        Set<Integer> B = Stream.of(3).collect(Collectors.toSet());
        CellularAutomaton<Main.State> automaton = new CellularAutomaton<>(Collections.nCopies(2, 5));
        automaton.setGame(new GameOfLife(automaton, B, S));
        automaton.fill(Main.State.DEAD);
        automaton.set(6, Main.State.ALIVE);
        automaton.set(7, Main.State.ALIVE);
        automaton.set(8, Main.State.ALIVE);
        Assertions.assertArrayEquals(new Main.State[]{
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.ALIVE, Main.State.ALIVE, Main.State.ALIVE, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,

        }, automaton.toList().toArray());
        automaton.setWarunek(new WarunekPochłaniający<>(Main.State.DEAD));
//        when
        automaton.replaceAll();
//        then
        Assertions.assertArrayEquals(new Main.State[]{
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.ALIVE, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.ALIVE, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
        }, automaton.toList().toArray());


    }

    @Test
    void dead() {
//        given
        Set<Integer> S = Stream.of(2, 3).collect(Collectors.toSet());
        Set<Integer> B = Stream.of(3).collect(Collectors.toSet());
        CellularAutomaton<Main.State> automaton = new CellularAutomaton<>(Collections.nCopies(2, 5));
        automaton.setGame(new GameOfLife(automaton, B, S));
        automaton.fill(Main.State.DEAD);
        automaton.setWarunek(new WarunekPochłaniający<>(Main.State.DEAD));
//        when
        automaton.replaceAll();
//        then
        Assertions.assertArrayEquals(new Main.State[]{
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
        }, automaton.toList().toArray());


    }
}