import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CrossoversTest {

    @BeforeEach
    public void setup() {
        Randoms.setSeed(0);
    }


    @ParameterizedTest
    @CsvSource({
            "TwoPoint,0,11111111,0,11110011,1100",
            "TwoPoint,10,11111111,0,11011111,100000",

            "OnePoint,0,11111111,0,11111,11100000",
            "OnePoint,-2,11111111,0,11,11111100"
    })
    public void crossover(CrossoverType strategy, int seed, @ConvertWith(BinaryIntConverter.class) int a, @ConvertWith(BinaryIntConverter.class) int b, String c, String d) {
        Randoms.setSeed(seed);

        List<Integer> points = getCrossover(strategy).points(a, b);

        Assertions.assertArrayEquals(new String[]{c, d}, points.stream().map(Integer::toBinaryString).toArray());
    }

    private <T> Crossover<Integer> getCrossover(CrossoverType selectionType) {
        switch (selectionType) {
            case TwoPoint:
                return new CrossoverAdapter(Crossovers.newTwoPoint());
            case OnePoint:
                return new CrossoverAdapter(Crossovers.newOnePoint());
        }
        throw new ArgumentConversionException("");
    }

    enum CrossoverType {
        TwoPoint, OnePoint
    }

    private static class CrossoverAdapter implements Crossover<Integer> {
        private final Crossover<List<String>> crossover;

        public CrossoverAdapter(Crossover<List<String>> crossover) {
            this.crossover = crossover;
        }

        @Override
        public List<Integer> points(Integer a, Integer b) {


            List<String> aBits = Arrays.stream(Integer.toBinaryString(a).split("")).collect(Collectors.toList());
            List<String> bBits = Arrays.stream(Integer.toBinaryString(b).split("")).collect(Collectors.toList());

            IntStream.range(0, aBits.size() - bBits.size()).forEach(i -> {
                bBits.add(0, "0");
            });

            IntStream.range(0, bBits.size() - aBits.size()).forEach(i -> {
                aBits.add(0, "0");
            });

            Collections.reverse(aBits);
            Collections.reverse(bBits);

            List<List<String>> points = crossover.points(aBits, bBits);

            return points.stream().map((s) -> {
                Collections.reverse(s);

                return String.join("", s);
            }).map(i -> Integer.parseInt(i, 2)).collect(Collectors.toList());
        }
    }

    public static class BinaryIntConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {

            if (source == null) {
                return null;
            }

            if (!int.class.isAssignableFrom(targetType) || !(source instanceof String)) {
                throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
                        + targetType + " not supported.");
            }


            return Integer.parseInt((String) source, 2);

        }
    }

}