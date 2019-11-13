import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private ArrayList<LineSegment> lineSegments;
    private ArrayList<Point[]> ends;

    public BruteCollinearPoints(Point[] points) {
        checkArgument(points);

        this.points = points;
        lineSegments = new ArrayList<>();
        ends = new ArrayList<>();
        computeSegments();
    }

    private boolean isSameSegment(Point a, Point b) {
        for (Point[] ps : ends) {
            if (ps[0].compareTo(a) == 0 && ps[1].compareTo(b) == 0) {
                return true;
            }
        }
        return false;
    }

    private void checkArgument(Point[] ps) {
        if (ps == null) {
            throw new IllegalArgumentException();
        }

        for (Point p : ps) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] pointsCopy = Arrays.copyOf(ps, ps.length);
        Arrays.sort(pointsCopy);

        for (int i = 0; i < pointsCopy.length; i++) {
            for (int j = i + 1; j < pointsCopy.length; j++) {
                if (pointsCopy[i].compareTo(pointsCopy[j]) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    private boolean isCollinear(Point a, Point b, Point c, Point d) {
        double ab = a.slopeTo(b);
        double ac = a.slopeTo(c);
        double ad = a.slopeTo(d);
        return ab == ac && ac == ad;
    }

    private void computeSegments() {
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int g = k + 1; g < points.length; g++) {
                        Point[] ps = new Point[] { points[i], points[j], points[k], points[g] };
                        Arrays.sort(ps);
                        if (isCollinear(points[i], points[j],
                                        points[k], points[g]) && !isSameSegment(ps[0],
                                                                                ps[ps.length
                                                                                        - 1])) {
                            ends.add(new Point[] { ps[0], ps[ps.length - 1] });
                            lineSegments.add(new LineSegment(ps[0], ps[ps.length - 1]));
                        }
                    }
                }
            }
        }
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

    }
}
