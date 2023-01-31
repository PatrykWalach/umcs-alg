import java.util.Collection;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Randoms {

    private static final Random random = new Random();

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    public static IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        return random.ints(randomNumberOrigin, randomNumberBound);
    }

    public static DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        return random.doubles(randomNumberOrigin, randomNumberBound);
    }

    public static int nextInt(int lowerInclusive, int upperExclusive) {
        return random.ints(lowerInclusive, upperExclusive).findFirst().orElseThrow(RuntimeException::new);
    }


    public static double nextDouble(double lowerInclusive, double upperExclusive) {
        return random.doubles(lowerInclusive, upperExclusive).findFirst().orElseThrow(RuntimeException::new);
    }

    public static int index(Collection<?> collection) {
        return nextInt(0, collection.size());
    }
}
