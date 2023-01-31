import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class CellularAutomatonTest {

    @Test
    void mapEdges() {

//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 3));
        automaton.fill(0);

        //        when
        automaton.setWarunek(new WarunekPochłaniający<>(1));
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                1, 1, 1,
                1, 0, 1,
                1, 1, 1}, automaton.toList().toArray());
    }

    @Test
    void mapValues() {

//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 3));
        automaton.fill(0);
        automaton.setGame((v, i) -> 1);
        //        when
        automaton.replaceAll();
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                0, 0, 0
                , 0, 1, 0,
                0, 0, 0}, automaton.toList().toArray());
    }


    @Test
    void mapBiggerEdges() {

//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 9));
        automaton.fill(0);
        //        when
        automaton.setWarunek(new WarunekPochłaniający<>(1));
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 0, 0, 0, 0, 0, 0, 0, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1,}, automaton.toList().toArray());
    }

    @Test
    void mapBiggerValues() {

//        given
        CellularAutomaton<Integer> automaton = new CellularAutomaton<>(Collections.nCopies(2, 9));
        automaton.fill(0);
        automaton.setGame((v, i) -> 1);
        //        when
        automaton.replaceAll();
        //        then
        Assertions.assertArrayEquals(new Integer[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,}, automaton.toList().toArray());
    }

}