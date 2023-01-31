import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

class SelectionTest {
    @BeforeEach
    public void setup() {
        Randoms.setSeed(0);
    }

    @ParameterizedTest
    @CsvSource({
//            Tournament selects for all values (1,0,-1)
//            Roulette selects none for -1,0
//            Rank selects for all values (1,0,-1)

            "Roulette,1,'1,2,3,4,5,6,7,8','1,2,3,4,5,7'",
            "Tournament,1,'1,2,3,4,5,6,7,8','2,5,7'",
            "Rank,1,'1,2,3,4,5,6,7,8','3,4,5,6,7'",

            "Roulette,1,'1,1,1,1,1,1,1,1','1,2,3,4,5,7'",
            "Tournament,1,'1,1,1,1,1,1,1,1','2,5,7'",
            "Rank,1,'1,1,1,1,1,1,1,1','3,4,5,6,7'",

            "Roulette,-1,'1,2,3,4,5,6,7,8',",
            "Tournament,-1,'1,2,3,4,5,6,7,8','2,5,7'",
            "Rank,-1,'1,2,3,4,5,6,7,8','3,4,5,6,7'",

            "Roulette,0,'1,2,3,4,5,6,7,8',",
            "Tournament,0,'1,2,3,4,5,6,7,8','2,5,7'",
            "Rank,0,'1,2,3,4,5,6,7,8','3,4,5,6,7'",
    })

    public void selection(SelectionType selectionType, int fitness, @ConvertWith(IntArrayConverter.class) Integer[] in, @ConvertWith(IntArrayConverter.class) Integer[] crossExpected) {
        Selection<Integer> selection = getSelection(selectionType, (i) -> fitness);

        List<Integer> keep = Arrays.stream(in).sequential().collect(Collectors.toList());
        List<Integer> cross = selection.select(keep).limit(8).distinct().boxed().collect(Collectors.toList());
        Assertions.assertArrayEquals(crossExpected, cross.stream().sorted().toArray());

    }

    private <T> Selection<T> getSelection(SelectionType selectionType, ToDoubleFunction<T> fitness) {
        switch (selectionType) {
            case Rank:
                return new SelectionRank<>(fitness);
            case Roulette:
                return new SelectionRoulette<>(fitness);
            case Tournament:
                return new SelectionTournament<>(5, fitness);
        }
        throw new ArgumentConversionException("");
    }

    enum SelectionType {
        Roulette, Tournament, Rank,
    }

    public static class IntArrayConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {

            if (source == null) {
                return new Integer[]{};
            }

            if (!Integer[].class.isAssignableFrom(targetType) || !(source instanceof String)) {
                throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
                        + targetType + " not supported.");
            }


            return Arrays.stream(((String) source).split("\\s*,\\s*")).sequential().map(Integer::parseInt).toArray(Integer[]::new);


        }
    }


}