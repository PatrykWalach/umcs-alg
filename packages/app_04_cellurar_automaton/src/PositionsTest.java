import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

class PositionsTest {


    @org.junit.jupiter.api.Test
    void toVertex() {
        //        given
        List<Integer> shape = Arrays.asList(3, 3);
//        when
        Vertex<Integer> i = Vertex.fromIndex(1, shape);
        Vertex<Integer> j = Vertex.fromIndex(3, shape);

//    then
        Assertions.assertArrayEquals(new Integer[]{0, 1}, i.toArray());
        Assertions.assertArrayEquals(new Integer[]{1, 0}, j.toArray());
    }

    @org.junit.jupiter.api.Test
    void toIndex() {
//        given
        List<Integer> shape = Arrays.asList(2, 2, 2);
//        when
//    then
        Assertions.assertEquals(0, Vertex.toIndex(Arrays.asList(0, 0, 0), shape));
        Assertions.assertEquals(1, Vertex.toIndex(Arrays.asList(0, 0, 1), shape));
        Assertions.assertEquals(2, Vertex.toIndex(Arrays.asList(0, 1, 0), shape));
        Assertions.assertEquals(3, Vertex.toIndex(Arrays.asList(0, 1, 1), shape));
        Assertions.assertEquals(4, Vertex.toIndex(Arrays.asList(1, 0, 0), shape));
        Assertions.assertEquals(5, Vertex.toIndex(Arrays.asList(1, 0, 1), shape));
        Assertions.assertEquals(6, Vertex.toIndex(Arrays.asList(1, 1, 0), shape));
        Assertions.assertEquals(7, Vertex.toIndex(Arrays.asList(1, 1, 1), shape));

    }
}