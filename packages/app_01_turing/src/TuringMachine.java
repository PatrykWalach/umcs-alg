import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


public class TuringMachine<T> {
    private final StringBuilder tape = new StringBuilder();
    private final char blank;
    private final Set<T> endStates;
    private final HashMap<T, State<T>> states = new HashMap<>();

    private int position = 0;
    private T state;

    public TuringMachine(T state, char blank, String input, T[] endStates) {
        this(state, blank, input, Arrays.stream(endStates).collect(Collectors.toSet()));
    }

    public TuringMachine(T state, char blank, String input, Set<T> endStates) {
        this.state = state;
        this.blank = blank;
        this.endStates = endStates;
        endStates.forEach((s) -> states.put(s, new State<>()));

        tape.append(input);
        tape.append(blank);

    }

    public Set<T> getStates() {
        return states.keySet();
    }

    public T getState() {
        return state;
    }

    public void addTransition(T state, char value, char nextValue, T nextState, Direction direction) {

        states.put(state, states.getOrDefault(state, new State<>()).addTransition(value, nextValue, nextState, direction));
    }

    public void addTransition(T state, char value, Direction direction) {
        addTransition(state, value, value, state, direction);
    }

    public boolean isDone() {
        return endStates.contains(state);
    }


    public void next() {


        char value = tape.charAt(position);


        if (isDone()) {
            return;
        }


        State<T> gotState = states.get(state);

        if (gotState == null) {
            throw new RuntimeException("State \"" + state + "\" not implemented");

        }

        Transition<T> transition = gotState.getTransition(value);

        tape.setCharAt(position, transition.getNextValue());
        state = transition.getNextState();

        switch (transition.getDirection()) {
            case right:
                position++;

                if (tape.length() > position) {
                    break;
                }


                tape.append(blank);


            case left:
                if (position <= 0) {

                    tape.insert(0, blank);
                    break;
                }
                position--;
                break;

        }

    }

    public String getTape() {
        return tape.toString();
    }

    public int getPosition() {
        return position;
    }

    public enum Direction {
        left,
        right,
        stay
    }


    private static class Transition<T> {
        private final char nextValue;
        private final T nextState;
        private final Direction direction;

        public Transition(char nextValue, T nextState, Direction direction) {
            this.nextValue = nextValue;
            this.nextState = nextState;
            this.direction = direction;
        }

        public char getNextValue() {
            return nextValue;
        }

        public T getNextState() {
            return nextState;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    private static class State<T> {
        private final HashMap<Character, Transition<T>> transitions = new HashMap<>();


        public Transition<T> getTransition(char value) {
            return transitions.get(value);
        }

        public State<T> addTransition(char value, char nextValue, T nextState, Direction direction) {

            transitions.put(value, new Transition<>(nextValue, nextState, direction));
            return this;
        }
    }
}