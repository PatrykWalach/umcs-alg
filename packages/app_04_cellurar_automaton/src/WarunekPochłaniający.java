public class WarunekPochłaniający<T> implements Warunek<T> {
    private final T value;

    public WarunekPochłaniający(T value) {
        this.value = value;
    }

    @Override
    public T apply(T t, Vertex<Integer> integerVertex) {
        return value;
    }
}
