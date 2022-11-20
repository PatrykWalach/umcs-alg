import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;


class TuringMachineTest {


    public static TuringMachine createBinaryDecrement(String input) {
        HashSet<String> endStates = new HashSet<>();
        endStates.add("done");

        TuringMachine binaryDecrement = new TuringMachine("q1", '_', input, endStates);
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

    public static TuringMachine createSameAmountOfZerosAndOnes(String input) {
        HashSet<String> endStates = new HashSet<>();
        endStates.add("yes");
        endStates.add("no");

        TuringMachine sameAmountOfZerosAndOnes = new TuringMachine("q1", '_', input, endStates);
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
    public void binaryDecrement(String input, String output) {
        TuringMachine binaryDecrement = createBinaryDecrement(input);

        while (!binaryDecrement.isDone()) {
            binaryDecrement.next();
        }

        Assertions.assertEquals("done", binaryDecrement.getState());
        Assertions.assertEquals(output, binaryDecrement.getTape());
    }

    @ParameterizedTest
    @CsvSource(value = {"1100,yes", "1001,yes", "10,yes", "11,no", "'',yes", "_,yes", "1,no", "0,no"})
    public void sameAmountOfZerosAndOnes(String input, String qk) {
        TuringMachine machine = createSameAmountOfZerosAndOnes(input);

        while (!machine.isDone()) {
            machine.next();
        }

        Assertions.assertEquals(qk, machine.getState());
    }

    @Test
    public void configDirector() {
        Main.Config config = new Main.Config();

        new Main.ConfigDirector(config).buildConfig("Opis: „negacja”\n" +
                "stany: 0,k\n" +
                "alfabet: 0,\\,,1,_\n" +
                "długość słowa: 6\n" +
                "słowo: 011001\n" +
                "stan końcowy: k\n" +
                "stan początkowy: 0\n" +
                "instrukcja:\n" +
                "0:\n" +
                "1;0,1,r;\n" +
                "0;0,\\,,r;\n" +
                "_;k,_,s;\n");


        Assertions.assertEquals("„negacja”", config.getDescription());
        Assertions.assertArrayEquals(new Character[]{'0', ',', '1', '_'}, config.getAlphabet().toArray());
        Assertions.assertEquals(6, config.getInputLength());
        Assertions.assertEquals("011001", config.getInput());
        Assertions.assertArrayEquals(new String[]{"k"}, config.getEndStates().toArray());
        Assertions.assertEquals("0", config.getStartState());

        Assertions.assertEquals("0", config.getInstructions().get(0).getState());
        Assertions.assertEquals('1', config.getInstructions().get(0).getValue());
        Assertions.assertEquals("0", config.getInstructions().get(0).getNextState());
        Assertions.assertEquals('1', config.getInstructions().get(0).getNextValue());
        Assertions.assertEquals(TuringMachine.Direction.right, config.getInstructions().get(0).getDirection());

        Assertions.assertEquals("0", config.getInstructions().get(1).getState());
        Assertions.assertEquals('0', config.getInstructions().get(1).getValue());
        Assertions.assertEquals("0", config.getInstructions().get(1).getNextState());
        Assertions.assertEquals(',', config.getInstructions().get(1).getNextValue());
        Assertions.assertEquals(TuringMachine.Direction.right, config.getInstructions().get(1).getDirection());

        Assertions.assertEquals("0", config.getInstructions().get(2).getState());
        Assertions.assertEquals('_', config.getInstructions().get(2).getValue());
        Assertions.assertEquals("k", config.getInstructions().get(2).getNextState());
        Assertions.assertEquals('_', config.getInstructions().get(2).getNextValue());
        Assertions.assertEquals(TuringMachine.Direction.stay, config.getInstructions().get(2).getDirection());
    }

    @Test
    public void configDirectorStany() {
        Main.Config config = new Main.Config();

        new Main.ConfigDirector(config).buildConfig("stany: 01,k0,:,\"\n" + "Opis: „negacja”\n" +
                "alfabet: 0,1,_\n" +
                "długość słowa: 6\n" +
                "słowo: 011001\n" +
                "stan końcowy: k0\n" +
                "stan początkowy: k0\n" +
                "instrukcja:\n" +
                "k0:\n" +
                "1;k0,1,r;\n" +
                "0;k0,1,r;\n" +
                "_;k0,_,s;\n");


        Assertions.assertArrayEquals(new String[]{"01", "k0", ":", "\""}, config.getStates().toArray());
    }

    @Test
    public void configDirectorInvalidAlfabet() {

        Assertions.assertThrows(Exception.class, () -> {
            Main.Config config = new Main.Config();

            new Main.ConfigDirector(config).buildConfig("alfabet: 01,k0,:,\"\n" + "Opis: „negacja”\n" +
                    "stany: 0,k\n" +

                    "długość słowa: 6\n" +
                    "słowo: 011001\n" +
                    "stan końcowy: k\n" +
                    "stan początkowy: 0\n" +
                    "instrukcja:\n" +
                    "0:\n" +
                    "1;0,1,r;\n" +
                    "0;0,1,r;\n" +
                    "_;k,_,s;\n");
        });
    }

    @Test
    public void configDirectorAlfabet() {
        Main.Config config = new Main.Config();

        new Main.ConfigDirector(config).buildConfig("alfabet: 0, ,:,\"\n" + "Opis: „negacja”\n" +
                "stany: 0,k\n" +

                "długość słowa: 6\n" +
                "słowo: 011001\n" +
                "stan końcowy: k\n" +
                "stan początkowy: 0\n" +
                "instrukcja:\n" +
                "0:\n" +
                "1;0,1,r;\n" +
                "0;0,1,r;\n" +
                "_;k,_,s;\n");


        Assertions.assertArrayEquals(new Character[]{'0', ' ', ':', '\"'}, config.getAlphabet().toArray());
    }

    @Test
    public void configDirectorEmptyAlfabet() {
        Assertions.assertThrows(Exception.class, () -> {
            Main.Config config = new Main.Config();

            new Main.ConfigDirector(config).buildConfig("alfabet:\n" + "Opis: „negacja”\n" +
                    "stany: 0,k\n" +

                    "długość słowa: 6\n" +
                    "słowo: 011001\n" +
                    "stan końcowy: k\n" +
                    "stan początkowy: 0\n" +
                    "instrukcja:\n" +
                    "0:\n" +
                    "1;0,1,r;\n" +
                    "0;0,1,r;\n" +
                    "_;k,_,s;\n");
        });
    }
}