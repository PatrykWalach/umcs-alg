import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MooreNeighborhoodTest {

    @Test
    void apply() {
        //        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 4));
        automaton.setValues(new Integer[]{
                0, 1, 2, 3,
                4, 5, 6, 7,
                8, 9, 10, 11,
                12, 13, 14, 15
        });
        List<List<Integer>> results = new ArrayList<>();
        automaton.setGame(new MooreNeighborhood<>(automaton, (v, list) -> {
            results.add(list);
            return v;
        }));
        //        when
        automaton.replaceAll();
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                0, 1, 2,
                4, 6,
                8, 9, 10}, results.get(0).toArray());

        Assertions.assertArrayEquals(new Integer[]{
                1, 2, 3,
                5, 7,
                9, 10, 11}, results.get(1).toArray());

        Assertions.assertArrayEquals(new Integer[]{
                4, 5, 6,
                8, 10,
                12, 13, 14
        }, results.get(2).toArray());
        Assertions.assertArrayEquals(new Integer[]{
                5, 6, 7,
                9, 11,
                13, 14, 15}, results.get(3).toArray());
    }
}