import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        List<Integer> list = fill(1_000_000);



        List<Integer> bubble = new ArrayList<>(list);
        List<Integer> comb = new ArrayList<>(list);
        List<Integer> quick = new ArrayList<>(list);
        List<Integer> radix = new ArrayList<>(list);

//

        long millisActualTime = System.currentTimeMillis();
        Bubble.sort(bubble );
        long executionTime = System.currentTimeMillis() - millisActualTime;
        System.out.println("Sortowanie bąbelkowe: " + executionTime);



//
         millisActualTime = System.currentTimeMillis();
        Comb.sort(comb );
         executionTime = System.currentTimeMillis() - millisActualTime;
        System.out.println("Sortowanie grzebieniowe: " + executionTime);

//
         millisActualTime = System.currentTimeMillis();
        Quick.sort(quick );
         executionTime = System.currentTimeMillis() - millisActualTime;
        System.out.println("Sortowanie quick sort: " + executionTime);

//
        millisActualTime = System.currentTimeMillis();
        Radix.sort(radix );
        executionTime = System.currentTimeMillis() - millisActualTime;
        System.out.println("Sortowanie pozycyjne: " + executionTime);


        list.sort(Comparator.comparingInt(Integer::intValue));

//        if(IntStream.range(0, list.size()).parallel().anyMatch((i)-> !Objects.equals(list.get(i), bubble.get(i)))){
//            System.out.println("Bąbel nie sortuje");
//        };
//
//        if(IntStream.range(0, list.size()).parallel().anyMatch((i)-> !Objects.equals(list.get(i), comb.get(i)))){
//            System.out.println("Grzebień nie sortuje");
//        };
//
//        if(IntStream.range(0, list.size()).parallel().anyMatch((i)-> !Objects.equals(list.get(i), quick.get(i)))){
//            System.out.println("Quick nie sortuje");
//        };
//
//        if(IntStream.range(0, list.size()).parallel().anyMatch((i)-> !Objects.equals(list.get(i), radix.get(i)))){
//            System.out.println("Radix nie sortuje");
//        };
//
//                System.out.println(list);
//        System.out.println(bubble );
//                System.out.println(comb );
//                System.out.println(quick );
//        System.out.println(radix);
    }


    public static List<Integer> fill( int size) {
        Random random = new Random();

        return          IntStream.range(0, size).map((i)-> random.nextInt()).boxed().collect(Collectors.toList());
    }
    public static class  Bubble {
        public static void sort(List<Integer> list) {
            boolean swapped = true;

            while (swapped) {
                swapped=false;
                for(int i =1; i < list.size(); i++){
                    if (list.get(i-1) > list.get(i)){
                        Collections.swap(list, i-1, i);
                        swapped=true;
                    }
                }
            }

        }
    }

    public static class  Radix{



        public static void sort(List<Integer> list) {
            int m = list.stream().parallel().max(Integer::compareTo).orElse(0);
            List<Integer> out = new ArrayList<>(list);
            List<Integer> in = list;


            for (int e =1 ; m/e > 0;e*=10 ){


                int[] count = new int[19];

                for (Integer integer : in) {
                    count[(integer / e) % 10 + 9]++;
                }


                for( int i =1; i< count.length;i++){
                    count[i]+=count[i-1];
                }


                for (int i=in.size()-1;i>=0;i--){
                    out.set(--count[(in.get(i)/e)%10 + 9], in.get(i));
                }



                List<Integer> tmp1 = in;
                in = out;
                out =tmp1;
            }

            if(list != in){
                Collections.copy(list, in);
            }
        }
    }

    public static class  Comb {

    public static void sort(List<Integer> list) {
        double shrink = 1.3;
        int gap = list.size();
        boolean sorted = false;

        while(!sorted){
            gap = new Double(gap / shrink).intValue();


            if (gap <= 1) {
                sorted = true;
                gap = 1;
            }

            int finalGap = gap;

            for(int i =0; i< list.size()-gap;i++){
                if (list.get(i) > list.get(i+ finalGap)) {
                    Collections.swap(list, i, i + finalGap);
                    sorted = false;
                }
            }




        }

    }}

    public static class  Quick{
    public static void sort(List<Integer> A) {
        int p = partition(A);

            if( 0 < p ) {
                sort(A.subList(0, p));
            }

            if (p + 1 < A.size()) {
                sort(A.subList(p + 1, A.size()));
            }
            
    }

    private static int partition(List<Integer> A) {
        int pivot = A.get(A.size()-1); // Choose the last element as the pivot

        int i = -1;

        for(int j = 0; j < A.size()-1; j++){
            if(A.get(j) <= pivot){
                i++;
                Collections.swap(A, i , j );
            }
        }

        i++;

        Collections.swap(A, i , A.size()-1 );

        return  i;

    }
    }
}