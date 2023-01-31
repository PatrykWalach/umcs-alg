import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static javax.swing.JList.HORIZONTAL_WRAP;

public class Main {
    private static final String DEFAULT_WARUNEK = "Pochłaniający";
    private static final Set<Integer> DEFAULT_S = Stream.of(2, 3).collect(Collectors.toSet());
    private static final Set<Integer> DEFAULT_B = Stream.of(3).collect(Collectors.toSet());

    public static void main(String[] args) {
        int SIZE = 20;

        CellularAutomaton<State> automaton = new CellularAutomaton<>(Collections.nCopies(2, SIZE + 2));


        GameOfLife gameOfLife = new GameOfLife(automaton, DEFAULT_B, DEFAULT_S);

        automaton.setGame(gameOfLife);

        HashMap<String, Warunek<State>> warunkiMap = new HashMap<>();
        warunkiMap.put("Pochłaniający", new WarunekPochłaniający<>(State.DEAD));
        warunkiMap.put("Przenikajacy", new WarunekPrzenikajacy<>(automaton));
        warunkiMap.put("Odbijający", new WarunekOdbijajacy<>(automaton, s -> s.equals(State.ALIVE) ? State.DEAD : State.ALIVE));

        automaton.fill(State.DEAD);
        automaton.setWarunek(warunkiMap.get(DEFAULT_WARUNEK));


        HashMap<String, List<List<Integer>>> presetsMap = new HashMap<>();

        presetsMap.put("Blinker", Arrays.asList(Arrays.asList(10, 10), Arrays.asList(10, 11), Arrays.asList(10, 12)));
        presetsMap.put("Acorn", Arrays.asList(Arrays.asList(9, 10),
                Arrays.asList(9, 11),
                Arrays.asList(10, 9),
                Arrays.asList(10, 10),
                Arrays.asList(11, 10)));

        presetsMap.put("Glider", Arrays.asList(
                Arrays.asList(10, 10),
                Arrays.asList(11, 11),
                Arrays.asList(12, 9),
                Arrays.asList(12, 10),
                Arrays.asList(12, 11)
        ));


        presetsMap.put("Block", Arrays.asList(
                Arrays.asList(10, 10),
                Arrays.asList(10, 11),
                Arrays.asList(11, 10),
                Arrays.asList(11, 11)
        ));

        presetsMap.put("Penta-decathlon", Arrays.asList(
                Arrays.asList(6, 10),
                Arrays.asList(7, 10),
                Arrays.asList(8, 9),
                Arrays.asList(8, 11),
                Arrays.asList(9, 10),
                Arrays.asList(10, 10),
                Arrays.asList(11, 10),
                Arrays.asList(12, 10),
                Arrays.asList(13, 9),
                Arrays.asList(13, 11),
                Arrays.asList(14, 10),
                Arrays.asList(15, 10)
        ));


        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


            JPanel container = new JPanel(new GridBagLayout());


            JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));
            JLabel label = new JLabel("Idle", SwingConstants.CENTER);


            List<JButton> buttons = IntStream.range(0, SIZE * SIZE).sequential().boxed().map((i) -> {
                JButton btn = new JButton();
                panel.add(btn);
                return btn;
            }).collect(Collectors.toList());

            Runnable refresh = () -> IntStream.range(0, SIZE).parallel().forEach((i) -> IntStream.range(0, SIZE).parallel().forEach((j) -> {
                Integer btn = Vertex.toIndex(Arrays.asList(i, j), Collections.nCopies(2, SIZE));
                Integer auto = Vertex.toIndex(Arrays.asList(i + 1, j + 1), Collections.nCopies(2, SIZE + 2));
                buttons.get(btn).setBackground(
                        automaton.toList().get(auto) == State.ALIVE ? Color.RED : null
                );
            }));


            Runnable nextStep = () -> {
                automaton.replaceAll();
                refresh.run();
            };

            Stoppable automatonThread = new Stoppable(() -> {
                label.setText("Running...");
                nextStep.run();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                label.setText("Idle");
            });


            IntStream.range(0, buttons.size()).forEachOrdered((i) -> {
                JButton btn = buttons.get(i);

                btn.addActionListener((e) -> {
                    if (automatonThread.isRunning()) {
                        return;
                    }

                    Vertex<Integer> j = Vertex.fromIndex(i, Collections.nCopies(2, SIZE));
                    List<Integer> index = j.stream().map(v -> v + 1).collect(Collectors.toList());
                    btn.setBackground(btn.getBackground().equals(Color.RED) ? null : Color.RED);
                    State nextState = automaton.toList().get(Vertex.toIndex(index, automaton.getShape())).equals(State.ALIVE) ? State.DEAD : State.ALIVE;
                    automaton.set(index, nextState);


                    List<State> states = automaton.toList();

                    System.out.println(IntStream.range(0, states.size()).boxed().filter((in) -> states.get(in).equals(State.ALIVE))

                            .map((in) -> "automaton.set(Arrays.asList(" + Vertex.fromIndex(in, automaton.getShape()).stream().map(Object::toString).collect(Collectors.joining(",")) + "), State.ALIVE);").collect(Collectors.joining("\n"))

                    );

                    System.out.println("=======");

                });

            });


//            automatonThread.start();
            JPanel controls = new JPanel(new GridLayout(1, 4));
            JButton next = new JButton("Next");
            controls.add(next);
            JButton start = new JButton("Start");
            controls.add(start);
            JButton stop = new JButton("Stop");
            controls.add(stop);
            controls.add(label);


            JPanel warunki = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            warunki.add(new JLabel("Warunek: "), gbc);
            DefaultListModel<String> warunekModel = new DefaultListModel<>();
            warunkiMap.keySet().forEach(warunekModel::addElement);
            JList<String> warunekList = new JList<>(warunekModel);
            warunekList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            warunekList.setSelectedValue(DEFAULT_WARUNEK, true);
            warunekList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            warunekList.setVisibleRowCount(-1);

            gbc.gridx++;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            warunki.add(warunekList, gbc);


            JPanel presets = new JPanel(new GridLayout(1, presetsMap.size() + 1));


            presets.add(new JLabel("Preset: "));

            presetsMap.forEach((key, value) -> {
                JButton btn = new JButton(key);

                btn.addActionListener((e) -> {
                    if (automatonThread.isRunning()) {
                        return;
                    }
                    automaton.fill(State.DEAD);
                    value.forEach(point -> automaton.set(point, State.ALIVE));
                    refresh.run();
                });


                presets.add(btn);

            });


            JPanel models = new JPanel(new GridBagLayout());
            DefaultListModel<Integer> sModel = new DefaultListModel<>();
            IntStream.range(1, 9).forEach(sModel::addElement);
            DefaultListModel<Integer> bModel = new DefaultListModel<>();
            IntStream.range(1, 9).forEach(bModel::addElement);

            JList<Integer> sList = new JList<>(sModel);
            sList.setSelectedIndices(DEFAULT_S.stream().mapToInt(v -> v - 1).toArray());
            JList<Integer> bList = new JList<>(bModel);
            bList.setSelectedIndices(DEFAULT_B.stream().mapToInt(v -> v - 1).toArray());
            sList.setLayoutOrientation(HORIZONTAL_WRAP
            );
            sList.setVisibleRowCount(-1);
            bList.setLayoutOrientation(HORIZONTAL_WRAP
            );
            bList.setVisibleRowCount(-1);
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            models.add(new JLabel("B: ", SwingConstants.RIGHT), gbc);
            gbc.gridx = 2;
            models.add(new JLabel("S: ", SwingConstants.RIGHT), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            models.add(bList, gbc);
            gbc.gridx = 3;
            models.add(sList, gbc);


            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1;
            gbc.weightx = 1;
            container.add(panel, gbc);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy = 1;
            gbc.gridx = 0;
            container.add(controls, gbc);
            gbc.gridy++;
            container.add(warunki, gbc);
            gbc.gridy++;
            container.add(presets, gbc);
            gbc.gridy++;
            container.add(models, gbc);


            sList.addListSelectionListener((e) -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                automaton.setGame(new GameOfLife(automaton, new HashSet<>(bList.getSelectedValuesList()), new HashSet<>(sList.getSelectedValuesList())));
                System.out.println(bList.getSelectedValuesList());
                System.out.println(sList.getSelectedValuesList());
            });

            bList.addListSelectionListener((e) -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                automaton.setGame(new GameOfLife(automaton, new HashSet<>(bList.getSelectedValuesList()), new HashSet<>(sList.getSelectedValuesList())));
                System.out.println(bList.getSelectedValuesList());
                System.out.println(sList.getSelectedValuesList());
            });


            warunekList.addListSelectionListener((e) -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                warunkiMap.computeIfPresent(warunekList.getSelectedValue(), (key, value) -> {
                    automaton.setWarunek(value);
                    return value;
                });

            });


            next.addActionListener((e) -> {
                if (automatonThread.isRunning()) {
                    return;
                }
                nextStep.run();
            });
            stop.addActionListener((e) -> automatonThread.stop());


            start.addActionListener((e) -> automatonThread.start());


            frame.add(container);


            frame.setSize(600, 600);
            frame.setResizable(false);
            frame.setVisible(true);


        });
//

    }

    public enum State {
        DEAD,
        ALIVE
    }

    static class Stoppable {

        private final Runnable runnable;
        private Thread thread = null;
        private boolean shouldRun = false;

        public Stoppable(Runnable runnable) {
            this.runnable = runnable;
        }

        public void stop() {
            this.shouldRun = false;
        }

        private void setThread(Thread thread) {
            this.thread = thread;
        }

        public void start() {
            if (isRunning()) {
                return;
            }

            this.shouldRun = true;

            this.thread = new Thread(() -> {
                while (isShouldRun()) {
                    System.out.println("running");
                    runnable.run();
                }
                setThread(null);
            });

            this.thread.start();

        }

        private boolean isShouldRun() {
            return shouldRun;
        }


        public boolean isRunning() {
            return this.thread != null;
        }

    }


}