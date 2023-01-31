import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class WarunekOdbijajacyTest {

    @Test
    void apply() {


//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 4));
        automaton.setValues(new Integer[]{
                0, 0, 0, 0,
                0, 1, 2, 0,
                0, 3, 4, 0,
                0, 0, 0, 0
        });

        //        when
        automaton.setWarunek(new WarunekOdbijajacy<>(automaton, (v) -> v + 1));
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                2, 2, 3, 3,
                2, 1, 2, 3,
                4, 3, 4, 5,
                4, 4, 5, 5,
        }, automaton.toList().toArray());
    }


}