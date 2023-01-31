import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class WarunekPrzenikajacyTest {

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
        automaton.setWarunek(new WarunekPrzenikajacy<>(automaton));
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                4, 3, 4, 3,
                2, 1, 2, 1,
                4, 3, 4, 3,
                2, 1, 2, 1,
        }, automaton.toList().toArray());
    }


    @Test
    void applyBigger() {

//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 9));

        automaton.setValues(new Integer[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 2, 3, 4, 5, 6, 7, 0,
                0, 11, 12, 13, 14, 15, 16, 17, 0,
                0, 21, 22, 23, 24, 25, 26, 27, 0,
                0, 31, 32, 33, 34, 35, 36, 37, 0,
                0, 41, 42, 43, 44, 45, 46, 47, 0,
                0, 51, 52, 53, 54, 55, 56, 57, 0,
                0, 61, 62, 63, 64, 65, 66, 67, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        });

        //        when
        automaton.setWarunek(new WarunekPrzenikajacy<>(automaton));
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                67, 61, 62, 63, 64, 65, 66, 67, 61,
                7, 1, 2, 3, 4, 5, 6, 7, 1,
                17, 11, 12, 13, 14, 15, 16, 17, 11,
                27, 21, 22, 23, 24, 25, 26, 27, 21,
                37, 31, 32, 33, 34, 35, 36, 37, 31,
                47, 41, 42, 43, 44, 45, 46, 47, 41,
                57, 51, 52, 53, 54, 55, 56, 57, 51,
                67, 61, 62, 63, 64, 65, 66, 67, 61,
                7, 1, 2, 3, 4, 5, 6, 7, 1,
        }, automaton.toList().toArray());
    }

    @Test
    void applyGame() {
        CellularAutomaton<Main.State> automaton = new CellularAutomaton<>(Collections.nCopies(2, 9));
        WarunekPrzenikajacy<Main.State> warunek = new WarunekPrzenikajacy<>(automaton);
        automaton.fill(Main.State.DEAD);

        automaton.set(12, Main.State.ALIVE);
        automaton.set(13, Main.State.ALIVE);
        automaton.set(14, Main.State.ALIVE);
//        when
        automaton.setWarunek(warunek);
        //        then
        Assertions.assertArrayEquals(new Main.State[]{
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.ALIVE, Main.State.ALIVE, Main.State.ALIVE, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
                Main.State.DEAD, Main.State.DEAD, Main.State.DEAD, Main.State.ALIVE, Main.State.ALIVE, Main.State.ALIVE, Main.State.DEAD, Main.State.DEAD, Main.State.DEAD,
        }, automaton.toList().toArray());
    }
}