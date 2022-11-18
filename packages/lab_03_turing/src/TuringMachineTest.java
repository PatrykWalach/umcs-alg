import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;

class TuringMachineTest {


    public static TuringMachine createBinaryDecrement(String input) throws InfiniteTransitionException {
        HashSet<String> endStates = new HashSet<>();
        endStates.add("done");

        TuringMachine binaryDecrement = new TuringMachine("q1", "_", input, endStates);
        binaryDecrement.addTransition("q1", '0', '0', "q1", TuringMachine.Direction.right);
        binaryDecrement.addTransition("q1", '1', '1', "q1", TuringMachine.Direction.right);
        binaryDecrement.addTransition("q1", '_', '_', "q2", TuringMachine.Direction.left);

        binaryDecrement.addTransition("q2", '0', '0', "q2", TuringMachine.Direction.left);
        binaryDecrement.addTransition("q2", '1', '0', "q3", TuringMachine.Direction.right);
        binaryDecrement.addTransition("q2", '_', '_', "done", TuringMachine.Direction.left);

        binaryDecrement.addTransition("q3", '0', '1', "q3", TuringMachine.Direction.right);
        binaryDecrement.addTransition("q3", '1', '1', "done", TuringMachine.Direction.left);
        binaryDecrement.addTransition("q3", '_', '_', "done", TuringMachine.Direction.left);

        return binaryDecrement;
    }

    public static TuringMachine createSameAmountOfZerosAndOnes(String input) throws InfiniteTransitionException {
        HashSet<String> endStates = new HashSet<>();
        endStates.add("yes");
        endStates.add("no");

        TuringMachine sameAmountOfZerosAndOnes = new TuringMachine("q1", "_", input, endStates);
        sameAmountOfZerosAndOnes.addTransition("q1", '0', '_', "q3", TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q1", '#', TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q1", '_', '_', "yes", TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q1", '1', '_', "q2", TuringMachine.Direction.right);

        sameAmountOfZerosAndOnes.addTransition("q2", '#', TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q2", '1', TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q2", '0', '#', "q4", TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q2", '_', '_', "no", TuringMachine.Direction.left);

        sameAmountOfZerosAndOnes.addTransition("q3", '#', TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q3", '0', TuringMachine.Direction.right);
        sameAmountOfZerosAndOnes.addTransition("q3", '1', '#', "q4", TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q3", '_', '_', "no", TuringMachine.Direction.left);

        sameAmountOfZerosAndOnes.addTransition("q4", '#', TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q4", '0', TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q4", '1', TuringMachine.Direction.left);
        sameAmountOfZerosAndOnes.addTransition("q4", '_', '_', "q1", TuringMachine.Direction.right);

        return sameAmountOfZerosAndOnes;
    }


    @ParameterizedTest
    @CsvSource(value = {"110,101_", "1,0_", "10,01_", "11,10_"})
    public void binaryDecrement(String input, String output) throws InfiniteTransitionException {
        TuringMachine binaryDecrement = createBinaryDecrement(input);

        while (!binaryDecrement.isDone()) {
            binaryDecrement.next();
        }

        Assertions.assertEquals(binaryDecrement.getState(), "done");
        Assertions.assertEquals(binaryDecrement.getTape(), output);
    }

    @ParameterizedTest
    @CsvSource(value = {"1100,yes", "1001,yes", "10,yes", "11,no", "'',yes", "_,yes", "1,no", "0,no"})
    public void sameAmountOfZerosAndOnes(String input, String qk) throws InfiniteTransitionException {
        TuringMachine machine = createSameAmountOfZerosAndOnes(input);

        while (!machine.isDone()) {
            machine.next();
        }

        Assertions.assertEquals(machine.getState(), qk);
    }

    @Test
    public void throwsInfiniteTransitionException() {
        Assertions.assertThrows(InfiniteTransitionException.class, () -> {
            TuringMachine machine = new TuringMachine("q0", "_", "_", new String[]{"qk"});
            machine.addTransition("q0", '_', TuringMachine.Direction.stay);

            while (!machine.isDone()) {
                machine.next();
            }
        });
    }

}