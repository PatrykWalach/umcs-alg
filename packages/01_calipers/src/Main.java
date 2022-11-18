import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Point2D> points = new ArrayList2<>();
        points.add(new Point2D.Double(2, 4));
        points.add(new Point2D.Double(4, 4));
        points.add(new Point2D.Double(5, 2));
        points.add(new Point2D.Double(3, 1));
        points.add(new Point2D.Double(2, 1));
        points.add(new Point2D.Double(1, 3));


        System.out.println(new MaxDistance(points).find());


    }

    static class ArrayList2<T> extends ArrayList<T> {
        @Override
        public T get(int index) {
            return super.get(index % size());
        }
    }

    static class MaxDistance {
        ArrayList<Point2D> points;

        public MaxDistance(ArrayList<Point2D> points) {
            this.points = points;
        }


        private double distance(int i, int j) {
            return points.get(i).distance(points.get(j));
        }


        private double angle(@NotNull Point2D p) {
            return Math.atan2(p.getY(), p.getX());
        }

        @Contract("_, _ -> new")
        private @NotNull Point2D sub(@NotNull Point2D p, @NotNull Point2D q) {
            return new Point2D.Double(p.getX() - q.getX(), p.getY() - q.getY());
        }

        public double angle(int m, int n) {
            Point2D pm = sub(points.get(m), points.get(m + 1));
            Point2D pn = sub(points.get(n), points.get(n + 1));

            return angle(pm) - angle(pn);
        }

        public double find() {
            int i = 0;
            int j = 1;

            while (angle((i), (j)) < Math.PI) {
                j++;
            }

            double max = distance(i, j);

            while (j < points.size()) {
                double l = 2 * Math.PI - angle(i, j);
                if (l < Math.PI) {
                    //Pi first
                    max = Math.max(max, distance(i + 1, j));
                    i++;
                    continue;
                }
                if (l > Math.PI) {
                    //Pj first
                    max = Math.max(max, distance(i, j + 1));
                    j++;
                    continue;
                }
                //parallel
                max = Math.max(max, distance(i + 1, j));
                max = Math.max(max, distance(i, j + 1));
                max = Math.max(max, distance(i + 1, j + 1));
                i++;
                j++;

            }

            return max;
        }
    }


}