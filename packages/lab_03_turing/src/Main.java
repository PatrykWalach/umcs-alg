import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        Config config = new Config();
        new ConfigDirector(config).buildConfig(String.join("\n", Files.readAllLines(Paths.get("./src/config"))));


        TuringMachine<String> machine = new TuringMachine<String>(config.getStartState(), config.getAlphabet().get(config.getAlphabet().size() - 1), config.getInput(), new HashSet<>(config.getEndStates()));


        config.getInstructions().forEach((i) -> {
            machine.addTransition(i.getState(), i.getValue(), i.getNextValue(), i.getNextState(), i.getDirection());
        });

        while (!machine.isDone()) {
            printTape(machine);
            machine.next();
        }

        printTape(machine, 1, 0);
    }

    public static void printTape(TuringMachine machine) throws InterruptedException {
        printTape(machine, 2, 500);
    }

    public static void printTape(TuringMachine<String> machine, int frames, int millis) throws InterruptedException {
        for (int i = 0; i < frames; i++) {
            String[] split = machine.getTape().split("");
            if (i % 2 > 0) {
                split[machine.getPosition()] = "\u001B[47m\u001B[30m" + split[machine.getPosition()] + "\u001B[0m";
            }
//                split[lastPosition] = "\u001B[38;5;146m" + split[lastPosition] + "\u001B[0m";


            int spaces = machine.getStates().stream().mapToInt(String::length).max(

            ).orElseThrow(RuntimeException::new) - machine.getState().length();


            String left = stringOf(spaces, " ") + machine.getState() + " ";
            System.out.print("\r" + left);
            System.out.print("" + String.join("", split));
            Thread.sleep(millis);
        }
    }

    private static String stringOf(int length, String fill) {
        return IntStream.range(0, length).mapToObj((s) -> fill).collect(Collectors.joining());
    }


    public static class ConfigDirector {
        private final Config config;

        public ConfigDirector(Config config) {
            this.config = config;
        }

        private SafeString getString(String input, String regex, String message) {
            Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE).matcher(input);
            if (!matcher.find()) {
                throw new RuntimeException("Config nie zawiera " + message);
            }

            return new SafeString(matcher.group(1).trim());
        }

        private void warn(String message, Exception e) {
            System.out.println("\033[0;33m" + message + "\033[0m");
        }

        private void warn(String message) {
            warn(message, null);
        }

        private void raise(String message, Exception e) {
            throw new RuntimeException(message, e);
        }

        private void raise(String message) {
            raise(message, null);
        }

        public void buildConfig(String input) {

            config.setDescription(getString(input, "^opis:([^\n]*)", "opisu").toString());


            config.setStates((Arrays.stream(getString(input, "^stany:([^\n]*)", "stanów").split(",")).map(SafeString::toString)).collect(Collectors.toList()));

            config.setStartState(getString(input, "^stan początkowy:([^\n]*)", "stanu początkowego").toString());

            if (!config.getStates().contains(config.getStartState())) {
                warn("\"Stan początkowy\" został automatycznie dodany do \"listy stanów\", gdyż nie znajdował się na \"liście stanów\"");
                config.getStates().add(config.getStartState());
            }


            config.setAlphabet(Arrays.stream(getString(input, "^alfabet:([^\n]*)", "alfabetu").split(",")).map((s) -> {
                SafeString escaped = s.escape();
                if (escaped.length() != 1) {
                    raise("Niepoprawny format alfabetu: \"" + s + "\" Alfabet składać się musi z pojedyńczych znaków");
                }
                return escaped.charAt(0);

            }).collect(Collectors.toList()));

            try {

                config.setInputLength(Integer.parseInt(getString(input, "^długość słowa:([^\n]*)", "długości słowa").toString()));
            } catch (NumberFormatException e) {
                warn("\"Długość słowa\" nie jest poprawną liczbą", e);
                config.setInputLength(config.getInput().length());
            }

            if (config.getInputLength() < 0) {
                warn("\"Długość słowa\" mniejsza od 0");
                config.setInputLength(config.getInput().length());
            }

            config.setInput(getString(input, "^słowo:([^\n]*)", "słowa").toString());
            if (config.getInput().length() != config.getInputLength()) {
                warn("Podana \"długość słowa\" nie zgadza się z faktyczną długością \"słowo\"");
                config.setInputLength(config.getInput().length());
            }
            config.setEndStates(Arrays.stream(getString(input, "^stan końcowy:([^\n]*)", "stanu końcowego").split(",")).map(SafeString::toString).collect(Collectors.toList()));

            for (String stan : config.getEndStates()) {
                if (!config.getStates().contains(stan)) {
                    warn("\"Stan końcowy\": \"" + stan + "\" został automatycznie dodany do \"listy stanów\", gdyż nie znajdował się na \"liście stanów\"");
                    config.getStates().add(stan);
                }
            }


            Matcher matcher = Pattern.compile("^instrukcja:([^\n]*)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE).matcher(input);
            if (!matcher.find()) {
                warn("Config nie zawiera żadnych \"instrukcji\"");

                return;
            }


            matcher = Pattern.compile("([^\n]*):([\\s\\S]*?)(([^\n]*):([\\s\\S]*?))?$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS).matcher(input.substring(matcher.end()));

            while (matcher.find()) {


                // klucz
                String state = matcher.group(1).trim();

                if (!config.getStates().contains(state)) {
                    warn("Stan: \"" + state + "\" z \"instrukcji\" został automatycznie dodany do \"listy stanów\", gdyż nie znajdował się na \"liście stanów\"");
                    config.getStates().add(state);
                }

                // wartość
                SafeString[] split = new SafeString(matcher.group(2).trim()).split(";");

//                if (split.length % 2 != 0) {
//                    raise("Niepoprawny format \"instrukcji\": \"" + matcher.group(2).trim() + "\"");
//                }

                for (int i = 0; i < split.length; i += 2) {


                    char value = getChar(split[i].ltrim(), "instrukcji");


                    SafeString[] instruction = split[i + 1].rtrim().split(",");

                    if (instruction.length != 3) {
                        raise("Niepoprawny format \"instrukcji\": \"" + matcher.group(2).trim() + "\"");
                    }


                    if (!config.getStates().contains(instruction[0])) {
                        warn("Stan: \"" + instruction[0] + "\" z \"instrukcji\" został automatycznie dodany do \"listy stanów\", gdyż nie znajdował się na \"liście stanów\"");
                        config.getStates().add(instruction[0].toString());
                    }

                    TuringMachine.Direction direction = TuringMachine.Direction.stay;

                    if (Objects.equals(instruction[2], "r")) {
                        direction = TuringMachine.Direction.right;
                    } else if (Objects.equals(instruction[2], "l")) {
                        direction = TuringMachine.Direction.left;
                    } else if (!Objects.equals(instruction[2], "s")) {
                        warn("Niepoprawny kierunek: \"" + instruction[2] + "\", zinterpretowano jako \"s\"");
                    }


                    config.addInstruction(getChar(instruction[1], "instrukcji"), instruction[0].toString(), state, direction, value);
                }


            }


        }

        private char getChar(SafeString string, String message) {
            SafeString escaped = (string).escape();

            if (escaped.length() != 1) {
                raise("Niepoprawny format znaku: \"" + string + "\" w \"" + message + "\" Instrukcje składać się muszą z pojedyńczych znaków");
            }
            char c = escaped.charAt(0);

            if (!config.getAlphabet().contains(c)) {
                warn("Znak: \"" + c + "\" z \"" + message + "\" został automatycznie dodany do \"alfabetu\", gdyż nie znajdował się w \"alfabecie\"");
                config.getAlphabet().add(c);
            }

            return c;
        }

        private static class SafeString {
            private final String value;

            public SafeString(String value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                return value.equals(obj);
            }

            public int length() {
                return value.length();
            }

            public char charAt(int index) {
                return value.charAt(index);
            }

            @Override
            public String toString() {
                return value;
            }

            public SafeString[] split(String str) {

                return Arrays.stream((this.value.split("(?<=(?<!\\\\)(?:\\\\\\\\){0,999})" + str))).map(SafeString::new).toArray(SafeString[]::new);
            }

            public SafeString escape() {

                return new SafeString(value.replaceAll("\\\\([\\s\\S])", "$1"));
            }

            public SafeString ltrim() {

                return new SafeString(value.replaceAll("^\\s+", ""));
            }

            public SafeString rtrim() {

                return new SafeString(value.replaceAll("\\s+$", ""));
            }
        }
    }

    public static class Config {

        private final List<Instruction> instructions = new ArrayList<>();
        private String description;
        private List<String> states;
        private List<Character> alphabet;
        private int inputLength;
        private String input;
        private List<String> endStates;
        private String startState;

        public List<Character> getAlphabet() {
            return alphabet;
        }

        public void setAlphabet(List<Character> alphabet) {
            this.alphabet = alphabet;
        }

        public int getInputLength() {
            return inputLength;
        }

        public void setInputLength(int inputLength) {
            this.inputLength = inputLength;
        }

        public List<String> getEndStates() {
            return endStates;
        }

        public void setEndStates(List<String> endStates) {
            this.endStates = endStates;
        }

        public List<String> getStates() {
            return states;
        }

        public void setStates(List<String> states) {
            this.states = states;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getStartState() {
            return startState;
        }

        public void setStartState(String startState) {
            this.startState = startState;
        }

        public List<Instruction> getInstructions() {
            return (instructions);
        }

        public void addInstruction(char nextValue, String nextState, String state, TuringMachine.Direction direction, char value) {
            instructions.add(new Instruction(nextValue, nextState, state, direction, value));
        }

        public static class Instruction {
            private final char nextValue;
            private final String nextState;
            private final String state;
            private final TuringMachine.Direction direction;
            private final char value;

            public Instruction(char nextValue, String nextState, String state, TuringMachine.Direction direction, char value) {
                this.nextValue = nextValue;
                this.nextState = nextState;
                this.state = state;
                this.direction = direction;
                this.value = value;
            }

            public char getNextValue() {
                return nextValue;
            }

            public String getNextState() {
                return nextState;
            }

            public String getState() {
                return state;
            }

            public TuringMachine.Direction getDirection() {
                return direction;
            }

            public char getValue() {
                return value;
            }
        }
    }


}