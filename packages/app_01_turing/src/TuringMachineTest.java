import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;


class TuringMachineTest {


    public static TuringMachine<String> createFromFile(String path, String input) throws IOException {


        Main.Config config = new Main.Config();
        new Main.ConfigDirector(config).buildConfig(String.join("\n", Files.readAllLines(Paths.get(path))));


        TuringMachine<String> machine = new TuringMachine<>(config.getStartState(), config.getAlphabet().get(config.getAlphabet().size() - 1), input, new HashSet<>(config.getEndStates()));


        config.getInstructions().forEach((i) -> machine.addTransition(i.getState(), i.getValue(), i.getNextValue(), i.getNextState(), i.getDirection()));


        return machine;
    }


    @ParameterizedTest
    @CsvSource(value = {"110,101", "1,0", "10,01", "11,10"})
    public void binaryDecrement(String input, String output) throws IOException {
        TuringMachine<String> binaryDecrement = createFromFile("./src/decrement", input);

        while (!binaryDecrement.isDone()) {
            binaryDecrement.next();
        }

        Assertions.assertEquals("done", binaryDecrement.getState());
        Assertions.assertEquals(output, binaryDecrement.getTape().replaceAll("_", ""));
    }

    @ParameterizedTest
    @CsvSource(value = {"110,111", "0,1", "1,10", "10,11", "11,100"})
    public void binaryIncrement(String input, String output) throws IOException {
        TuringMachine<String> binaryDecrement = createFromFile("./src/increment", input);

        while (!binaryDecrement.isDone()) {
            binaryDecrement.next();
        }

        Assertions.assertEquals("done", binaryDecrement.getState());
        Assertions.assertEquals(output, binaryDecrement.getTape().replaceAll("_", ""));
    }

    @ParameterizedTest
    @CsvSource(value = {"1100,yes", "1001,yes", "10,yes", "11,no", "'',yes", "_,yes", "1,no", "0,no"})
    public void sameAmountOfZerosAndOnes(String input, String qk) throws IOException {
        TuringMachine<String> machine = createFromFile("./src/pairs", input);

        while (!machine.isDone()) {
            machine.next();
        }

        Assertions.assertEquals(qk, machine.getState().replaceAll("_", ""));
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
                "1:\n" +
                "0;0,\\,,r;\n");


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

        Assertions.assertEquals("1", config.getInstructions().get(1).getState());
        Assertions.assertEquals('0', config.getInstructions().get(1).getValue());
        Assertions.assertEquals("0", config.getInstructions().get(1).getNextState());
        Assertions.assertEquals(',', config.getInstructions().get(1).getNextValue());
        Assertions.assertEquals(TuringMachine.Direction.right, config.getInstructions().get(1).getDirection());

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

        new Main.ConfigDirector(config).buildConfig("alfabet: 0,1,_, ,:,\"\n" + "Opis: „negacja”\n" +
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


        Assertions.assertArrayEquals(new Character[]{'0', '1', '_', ' ', ':', '\"'}, config.getAlphabet().toArray());
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