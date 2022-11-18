import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;


public class TuringMachine {
    private final LinkedList<String> tape = new LinkedList<>();
    private final String blank;
    private final Set<String> endStates;
    private final HashMap<String, State> states = new HashMap<>();
    private int position = 0;
    private String state;


    public TuringMachine(String state, String blank, String input, String[] endStates) {
        this(state, blank, input, Arrays.stream(endStates).collect(Collectors.toSet()));
    }

    public TuringMachine(String state, String blank, String input, Set<String> endStates) {
        this.state = state;
        this.blank = blank;
        this.endStates = endStates;
        endStates.forEach((s) -> states.put(s, new State()));
        ((Arrays.stream(input.split("")).map(s -> {
            if (s.isEmpty()) {
                return blank;
            }
            return s;

        }))).forEach(tape::add);


    }

    public Set<String> getStates() {
        return states.keySet();
    }

    public String getState() {
        return state;
    }

    public void addTransition(String state, String value, String nextValue, String nextState, Direction direction) throws InfiniteTransitionException {

        states.put(state, states.getOrDefault(state, new State()).addTransition(value, nextValue, nextState, direction));
    }

    public void addTransition(String state, String value, Direction direction) throws InfiniteTransitionException {
        addTransition(state, value, value, state, direction);
    }

    public boolean isDone() {
        return endStates.contains(state);
    }

    public void next() {
        String value = tape.get(position);

        if (isDone()) {
            return;
        }


        State gotState = states.get(state);

        if (gotState == null) {
            throw new RuntimeException("State \"" + state + "\" not implemented");

        }

        Transition transition = gotState.getTransition(value);


        tape.set(position, transition.getNextValue());
        state = transition.getNextState();

        switch (transition.getDirection()) {
            case right:
                position++;

                if (tape.size() <= position) {
                    tape.add(blank);
                }

                break;

            case left:
                if (position <= 0) {
                    tape.add(0, blank);
                    break;
                }
                position--;
                break;

        }

    }

    public String getTape() {
        return String.join("", tape);
    }

    public int getPosition() {
        return position;
    }

    public enum Direction {
        left,
        right,
        stay
    }

    private static class Transition {
        private final String nextValue;
        private final String nextState;
        private final Direction direction;

        public Transition(String nextValue, String nextState, Direction direction) {
            this.nextValue = nextValue;
            this.nextState = nextState;
            this.direction = direction;
        }

        public String getNextValue() {
            return nextValue;
        }

        public String getNextState() {
            return nextState;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    private static class State {
        private final HashMap<String, Transition> transitions = new HashMap<>();


        public Transition getTransition(String value) {
            return transitions.get(value);
        }

        public State addTransition(String value, String nextValue, String nextState, Direction direction) throws InfiniteTransitionException {
            if (value.equals(nextValue) && direction.equals(Direction.stay)) {
                throw new InfiniteTransitionException();
            }
            transitions.put(value, new Transition(nextValue, nextState, direction));
            return this;
        }
    }
}